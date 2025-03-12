package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.convert.test.ConvertRuleTest;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertMoveMethodToRename {
	public static void convertMoveMethodToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException {
		AST ast = cu.getAST();
		ASTRewrite rewrite = ASTRewrite.create(ast);
		for (Object type : cu.types()) {
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				if (typeDeclaration.getName().toString().equals(targetClass)) {
//					System.out.println("methodName:" + methodName);
					List<String> methodNameList = ConvertRuleTest.getAllMethodName(cu);
//					Random r = new Random();
					String createMethodName = ConvertRuleTest.getNewMethodName(methodNameList);
					SimpleName name = ast.newSimpleName(createMethodName);
					MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
					methodDeclaration.setName(name);
					methodDeclaration.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
					methodDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
					typeDeclaration.bodyDeclarations().add(methodDeclaration);
//					cu.accept(new ASTVisitor() {
//						public boolean visit(MethodDeclaration method) {
//							if (method.getName().getIdentifier().equals(methodName)) {
//								method.getName().setIdentifier(createMethodName);
//							}
//							return super.visit(method);
//						}
//					});
					try {
						TextEdit edits = rewrite.rewriteAST();

						String sourceCode = cu.toString();
						// 应用修改
						Document document = new Document(sourceCode);
						edits.apply(document);
						String modifiedSourceCode = document.get();
//											System.out.println(modifiedSourceCode);
						SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH + "convertMoveMethodToRename/"
								+ "Bug" + sourceClass + ".java", modifiedSourceCode);

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
