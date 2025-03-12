package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.convert.test.ConvertRuleTest;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertPullUpMethodToRename {
	String parentClassName = "";

	public void convertPullUpMethodToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException {

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

		for (Object type : cu.types()) {
			AST ast = cu.getAST();
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				if (typeDeclaration.getName().toString().equals(sourceClass)) {
					for (MethodDeclaration m : typeDeclaration.getMethods()) {
						if (m.getName().toString().equals(methodName)) {

							Type t = typeDeclaration.getSuperclassType(); // 父类名
							for (Object obj : cu.types()) {
								if (obj instanceof TypeDeclaration) {
									TypeDeclaration typeDec = (TypeDeclaration) obj;
									if (typeDec.getName().toString().equals(t.toString())) {
										ASTRewrite rewrite = ASTRewrite.create(ast);
//										Random r = new Random();
//										String createMethodName = Constant.RANDOM_SELECT_NEW_METHOD_NAME[r
//												.nextInt(Constant.RANDOM_SELECT_NEW_METHOD_NAME.length)];
										List<String> methodNameList = ConvertRuleTest.getAllMethodName(cu);
//										Random r = new Random();
										String createMethodName = ConvertRuleTest.getNewMethodName(methodNameList);
										MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
										SimpleName name = ast.newSimpleName(createMethodName);
										methodDeclaration.setName(name);
										methodDeclaration.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
										methodDeclaration.modifiers()
												.add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
										Block methodBody = ast.newBlock();
										methodDeclaration.setBody(methodBody);
										typeDec.bodyDeclarations().add(methodDeclaration);
										try {
											TextEdit edits = rewrite.rewriteAST();

											String sourceCode = cu.toString();
											// 应用修改
											Document document = new Document(sourceCode);
											edits.apply(document);
											String modifiedSourceCode = document.get();
//										System.out.println(modifiedSourceCode);
											SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH
													+ "pullupToRenameMethod/" + "Bug" + sourceClass + ".java",
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
										return;
									}
								}
							}

						}
					}
				}

			}
		}

	}
}
