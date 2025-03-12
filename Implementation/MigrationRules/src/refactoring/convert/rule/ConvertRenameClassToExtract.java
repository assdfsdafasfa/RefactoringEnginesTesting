package refactoring.convert.rule;

import java.util.List;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
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

import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertRenameClassToExtract {
	public static void convertRenameClassToExtractInterface(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String oldClassName, String newClassName)
			throws JavaModelException, IllegalArgumentException {
		for (Object type : cu.types()) {
			AST ast = cu.getAST();
			ASTRewrite rewrite = ASTRewrite.create(ast);
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				if (typeDeclaration.getName().toString().equals(oldClassName)) {
					MethodDeclaration newMethod = ast.newMethodDeclaration();
//					System.out.print("newMethodName:" + newMethodName);
					Random r = new Random();
					String createMethodName = Constant.RANDOM_SELECT_NEW_METHOD_NAME[r
							.nextInt(Constant.RANDOM_SELECT_NEW_METHOD_NAME.length)];
					SimpleName name = ast.newSimpleName(createMethodName);
					newMethod.setName(name);
					newMethod.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
					newMethod.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

					Block methodBody = ast.newBlock();
					newMethod.setBody(methodBody);
//									methodDec.setName(ast.newSimpleName(newMethodName));
					typeDeclaration.bodyDeclarations().add(newMethod);

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
//									System.out.println(modifiedSourceCode);
					SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH
							+ "convertRenameClassToExtractInterface/" + "Bug" + oldClassName + ".java",
							modifiedSourceCode);

					return;
				}

			}

		}
	}
}
