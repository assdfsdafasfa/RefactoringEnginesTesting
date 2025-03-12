package refactoring.convert.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertPullDownMethodToInline {
	String parentClassName = "";

	public void convertPullDownMethodToInline(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName)
			throws JavaModelException, IllegalArgumentException {

		// 获取method的参数类型列表
		MethodDeclaration methodDec = FindIdentifier.findMethodDeclaration(cu, methodName);
		List<String> methodParaType = new ArrayList<>();
		for (Object parameter : methodDec.parameters()) {
			SingleVariableDeclaration variableDeclaration = (SingleVariableDeclaration) parameter;
			String parameterType = variableDeclaration.getType().toString();
			methodParaType.add(parameterType);
		}

		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(TypeDeclaration node) {
				if (node.getName().getIdentifier().equals(sourceClass)) {
					if (node.getSuperclassType() != null) {
						parentClassName = node.getSuperclassType().toString();
					}
					return false; // 停止遍历
				}
				return super.visit(node);
			}
		});
		System.out.print("1111111111111111111；");
		for (Object type : cu.types()) {
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				typeDeclaration.accept(new ASTVisitor() {
					@Override
					public boolean visit(MethodDeclaration node) {
						if (((MethodDeclaration) node).getName().toString().equals(methodName)) {
							AST ast = cu.getAST();
							ASTRewrite rewrite = ASTRewrite.create(ast);

							Random r = new Random();
							String createMethodName = Constant.RANDOM_SELECT_NEW_METHOD_NAME[r
									.nextInt(Constant.RANDOM_SELECT_NEW_METHOD_NAME.length)];
							MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
							SimpleName name = ast.newSimpleName(createMethodName);
							methodDeclaration.setName(name);
							methodDeclaration.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
							methodDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
							Block methodBody = ast.newBlock();

							if (methodParaType.size() > 0) {
								List<String> paraNameList = new ArrayList<>();
								for (int i = 0; i < methodParaType.size(); i++) {// 生成参数类型声明；
									String paraType = methodParaType.get(i);
									String paraName = "i" + i;
									paraNameList.add(paraName);
									if (paraType.equals("int")) { // 判断类型是否为int
										VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
										fragment.setName(ast.newSimpleName(paraName));
										fragment.setInitializer(ast.newNumberLiteral("0"));
										VariableDeclarationStatement variableDeclarationStatement = ast
												.newVariableDeclarationStatement(fragment);
										variableDeclarationStatement.setType(ast.newPrimitiveType(PrimitiveType.INT));
										// 将变量声明语句添加到方法体的第一个位置
										methodBody.statements().add(variableDeclarationStatement);
									} else {// 如果类型不为int
										SimpleType variableType = ast.newSimpleType(ast.newSimpleName(paraType));
										// 创建变量名节点
										SimpleName variableName = ast.newSimpleName(paraType);
										// 创建变量声明节点
										VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
										fragment.setName(variableName);

										// 创建变量声明语句节点
										VariableDeclarationStatement variableDeclarationStatement = ast
												.newVariableDeclarationStatement(fragment);
										variableDeclarationStatement.setType(variableType);

										// 创建赋值表达式节点
										Assignment assignment = ast.newAssignment();
										assignment.setLeftHandSide(variableName);
										assignment.setRightHandSide(ast.newNullLiteral());
										// 将赋值表达式设置为变量声明节点的初始值
										fragment.setInitializer(assignment);
									}
								}

								MethodInvocation methodInvocation = ast.newMethodInvocation();
								methodInvocation.setName(ast.newSimpleName(methodName));
								for (String p : paraNameList) {// 添加调用语句的实参
									SimpleName argument = ast.newSimpleName(p);
									methodInvocation.arguments().add(argument);
								}

								ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);
								methodBody.statements().add(expressionStatement);
								methodDeclaration.setBody(methodBody);
								System.out.print("----------；");
								for (Object obj : cu.types()) {
									if (obj instanceof TypeDeclaration) {
										TypeDeclaration t = (TypeDeclaration) obj;
										System.out.print("t----------；" + t.getSuperclassType() + "\n");
										if (t.getSuperclassType() != null
												&& t.getSuperclassType().toString().equals(sourceClass)) {// ----------------需要修改----------------
											t.bodyDeclarations().add(methodDeclaration);

											try {
												TextEdit edits = rewrite.rewriteAST();

												String sourceCode = cu.toString();
												// 应用修改
												Document document = new Document(sourceCode);
												edits.apply(document);
												String modifiedSourceCode = document.get();
//													System.out.println(modifiedSourceCode);
												SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH
														+ "convertPullDownMethodToInline/" + "Bug" + sourceClass
														+ ".java",
														modifiedSourceCode);

											} catch (BadLocationException e) {
												e.printStackTrace();
											} catch (JavaModelException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IllegalArgumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
								}

							} else {

								MethodInvocation methodInvocation = ast.newMethodInvocation();
								methodInvocation.setName(ast.newSimpleName(methodName));
								// 创建方法调用的参数节点
//						        SimpleName argument = ast.newSimpleName("arg");
//								methodInvocation.arguments().add(argument);
								ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);

								methodBody.statements().add(expressionStatement);

								// 将方法体节点设置为方法节点的方法体
								methodDeclaration.setBody(methodBody);
								for (Object obj : cu.types()) {
									if (obj instanceof TypeDeclaration) {
										TypeDeclaration t = (TypeDeclaration) obj;
										System.out.print("t----------；" + t.getSuperclassType() + "\n");
										if (t.getSuperclassType() != null
												&& t.getSuperclassType().toString().equals(sourceClass)) {// ----------------需要修改----------------
											t.bodyDeclarations().add(methodDeclaration);

											try {
												TextEdit edits = rewrite.rewriteAST();

												String sourceCode = cu.toString();
												// 应用修改
												Document document = new Document(sourceCode);
												edits.apply(document);
												String modifiedSourceCode = document.get();
//													System.out.println(modifiedSourceCode);
												SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH
														+ "convertPullDownMethodToInline/" + "Bug" + sourceClass
														+ ".java",
														modifiedSourceCode);

											} catch (BadLocationException e) {
												e.printStackTrace();
											} catch (JavaModelException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IllegalArgumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
								}
							}
						}
						return super.visit(node);
					}
				});

			}
		}
	}

}
