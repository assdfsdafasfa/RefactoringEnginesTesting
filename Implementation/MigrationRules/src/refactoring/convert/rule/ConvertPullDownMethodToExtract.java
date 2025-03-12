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
import org.eclipse.jdt.core.dom.FieldAccess;
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

public class ConvertPullDownMethodToExtract {
	public void convertPullDownMethodToExtract(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String fileName, String methodName)
			throws JavaModelException, IllegalArgumentException {
		MethodDeclaration methodDeclaration = FindIdentifier.findMethodDeclaration(cu, methodName);
		List<String> methodParaType = new ArrayList<>();
		for (Object parameter : methodDeclaration.parameters()) {
			SingleVariableDeclaration variableDeclaration = (SingleVariableDeclaration) parameter;
			String parameterType = variableDeclaration.getType().toString();
			methodParaType.add(parameterType);
		}

		AST ast = cu.getAST();
		ASTRewrite rewrite = ASTRewrite.create(ast);

		for (Object type : cu.types()) {
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				if (methodParaType.size() == 0) {
					typeDeclaration.accept(new ASTVisitor() {
						@Override
						public boolean visit(MethodDeclaration node) {
							if (node.getName().getIdentifier().equals(methodName)) {
								MethodDeclaration methodDec = ast.newMethodDeclaration();
								Random r = new Random();
								String createMethodName = Constant.RANDOM_SELECT_NEW_METHOD_NAME[r
										.nextInt(Constant.RANDOM_SELECT_NEW_METHOD_NAME.length)];
								SimpleName name = ast.newSimpleName(createMethodName);
								methodDec.setName(name);
								methodDec.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
								methodDec.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

								VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
								fragment.setName(ast.newSimpleName("i"));
								fragment.setInitializer(ast.newNumberLiteral("0"));
								VariableDeclarationStatement variableDeclarationStatement = ast
										.newVariableDeclarationStatement(fragment);
								variableDeclarationStatement.setType(ast.newPrimitiveType(PrimitiveType.INT));
								// 将变量声明语句添加到方法体的第一个位置
								Block methodBody = ast.newBlock();
								methodBody.statements().add(0, variableDeclarationStatement);
								methodDec.setBody(methodBody);
								System.out.print("fileName----------；" + fileName + "\n");
								for (Object obj : cu.types()) {
									if (obj instanceof TypeDeclaration) {
										TypeDeclaration t = (TypeDeclaration) obj;
										System.out.print("t----------；" + t.getSuperclassType() + "\n");
										if (t.getSuperclassType() != null
												&& t.getSuperclassType().toString().equals(fileName)) {// ----------------需要修改----------------
											t.bodyDeclarations().add(methodDec);

											ASTRewrite rewrite = ASTRewrite.create(ast);

											try {
												TextEdit edits = rewrite.rewriteAST();

												String sourceCode = cu.toString();
												// 应用修改
												Document document = new Document(sourceCode);
												edits.apply(document);
												String modifiedSourceCode = document.get();
//												System.out.println(modifiedSourceCode);
												SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH
														+ "convertPullDownMethodToExtract/" + "Bug" + fileName
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
							return super.visit(node);
						}
					});

				} else {// 方法参数不为0
					typeDeclaration.accept(new ASTVisitor() {
						@Override
						public boolean visit(MethodDeclaration node) {
							if (node.getName().getIdentifier().equals(methodName)) {
								MethodDeclaration methodDec = ast.newMethodDeclaration();
								Random r = new Random();
								String createMethodName = Constant.RANDOM_SELECT_NEW_METHOD_NAME[r
										.nextInt(Constant.RANDOM_SELECT_NEW_METHOD_NAME.length)];
								SimpleName name = ast.newSimpleName(createMethodName);
								methodDec.setName(name);
								methodDec.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
								methodDec.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

								List<String> paraNameList = new ArrayList<>();
								for (int i = 0; i < methodParaType.size(); i++) {
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
										Block methodBody = methodDeclaration.getBody();
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
										Block methodBody = ast.newBlock();
										methodBody.statements().add(variableDeclarationStatement);
										methodDec.setBody(methodBody);
									}
								}
								for (String n : paraNameList) {// 为所有类型的声明，添加输出语句
									// 创建变量名节点
									SimpleName variableName = ast.newSimpleName(n);
									// 创建方法调用节点
									MethodInvocation methodInvocation = ast.newMethodInvocation();
									methodInvocation.setName(ast.newSimpleName("println"));
									// 创建访问表达式节点
									FieldAccess fieldAccess = ast.newFieldAccess();
									fieldAccess.setExpression(ast.newSimpleName("System"));
									fieldAccess.setName(ast.newSimpleName("out"));
									// 设置方法调用节点的表达式为访问表达式节点
									methodInvocation.setExpression(fieldAccess);
									// 创建参数节点
									methodInvocation.arguments().add(variableName);
									// 创建表达式语句节点
									ExpressionStatement expressionStatement = ast
											.newExpressionStatement(methodInvocation);
									Block methodBody = ast.newBlock();
									methodBody.statements().add(expressionStatement);
									methodDec.setBody(methodBody);
									ASTRewrite rewrite = ASTRewrite.create(ast);
									System.out.print("fileName----------；" + fileName + "\n");
									for (Object obj : cu.types()) {
										if (obj instanceof TypeDeclaration) {
											TypeDeclaration t = (TypeDeclaration) obj;
											System.out.print("t----------；" + t.getSuperclassType() + "\n");
											if (t.getSuperclassType() != null
													&& t.getSuperclassType().toString().equals(fileName)) {// ----------------需要修改----------------
												t.bodyDeclarations().add(methodDec);

												try {
													TextEdit edits = rewrite.rewriteAST();

													String sourceCode = cu.toString();
													// 应用修改
													Document document = new Document(sourceCode);
													edits.apply(document);
													String modifiedSourceCode = document.get();
//													System.out.println(modifiedSourceCode);
													SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH
															+ "convertPullDownMethodToExtract/" + fileName + ".java",
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
							return false;
						}
					});
				}
			}
		}
	}
}
