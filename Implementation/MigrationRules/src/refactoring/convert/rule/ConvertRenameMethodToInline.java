package refactoring.convert.rule;

import java.util.List;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertRenameMethodToInline {

	public void convertRenameToInline(IProject project, List<IFile> javaFiles, IFile testCaseFile, CompilationUnit cu,
			String fileName, String oldMethodName, String newMethodName)
			throws JavaModelException, IllegalArgumentException {
		// TODO Auto-generated method stub
		// 判断是否存在方法调用语句
		FindIdentifier identify = new FindIdentifier();
		boolean containsMethodCall = identify.containsMethodCall(cu, newMethodName);
//		boolean sameClass = identify.containsMethodCall(cu, newMethodName);

		if (containsMethodCall) {
			Random r = new Random();
			String createMethodName = Constant.RANDOM_SELECT_NEW_METHOD_NAME[r
					.nextInt(Constant.RANDOM_SELECT_NEW_METHOD_NAME.length)];
			for (Object type : cu.types()) {
				AST ast = cu.getAST();
				if (type instanceof TypeDeclaration) {
					TypeDeclaration typeDeclaration = (TypeDeclaration) type;

					// 获取类型声明中的所有方法声明
					for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
						// 替换所有方法调用表达式
						methodDeclaration.accept(new ASTVisitor() {
							@Override
							public boolean visit(MethodInvocation node) {
								if (node.getName().getIdentifier().equals(newMethodName)) {
									node.getName().setIdentifier(createMethodName);
								}
								return super.visit(node);
							}
						});
					}

					for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {

						// 处理新的方法声明
						if (methodDeclaration.getName().getIdentifier().toString().equals(newMethodName)) {
							// 创建一个 AST

							ASTRewrite rewrite = ASTRewrite.create(ast);

							MethodDeclaration fMethod = ast.newMethodDeclaration();
							fMethod.setName(ast.newSimpleName(createMethodName));
							fMethod.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
							typeDeclaration.bodyDeclarations().add(fMethod);

							// 创建方法调用表达式
							MethodInvocation methodInvocation = ast.newMethodInvocation();
							methodInvocation.setName(ast.newSimpleName(newMethodName));

							// 创建方法调用语句
							ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);

							// 将方法调用语句添加到 f 方法的方法体中
							Block fMethodBody = ast.newBlock();
							fMethodBody.statements().add(expressionStatement);
							fMethod.setBody(fMethodBody);
//							System.out.print("fMethod；" + fMethod + "\n");
//							System.out.print("typeDeclaration；" + typeDeclaration + "\n");
//							typeDeclaration.bodyDeclarations().add(fMethod);

							TextEdit edits = rewrite.rewriteAST();

							String sourceCode = cu.toString();
							// 应用修改
							Document document = new Document(sourceCode);
							try {
								edits.apply(document);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}

							String modifiedSourceCode = document.get();
//											System.out.println(modifiedSourceCode);
							SaveFile.saveContentToFile(
									Constant.CONVERT_PROGRAM_SAVE_PATH + "inlineMethod/" + "Bug" + fileName + ".java",
									modifiedSourceCode);

							return;
						}
					}

					// 获取类型声明中的所有方法声明
					for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
						// 替换old method name 为新的new method name
						if (methodDeclaration.getName().toString().equals(oldMethodName)) {
							System.out.print("oldMethodName；" + oldMethodName + "\n");
							System.out.print("newMethodName；" + newMethodName + "\n");
							SimpleName name = ast.newSimpleName(newMethodName);
							methodDeclaration.setName(name);
						}
					}

				}
			}
		}
	}
}
