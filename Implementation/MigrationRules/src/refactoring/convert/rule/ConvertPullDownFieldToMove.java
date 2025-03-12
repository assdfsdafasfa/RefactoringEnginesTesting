package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertPullDownFieldToMove {
	public static void convertPullDownFieldToMove(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceName, String fieldName)
			throws JavaModelException, IllegalArgumentException {

		AST ast = cu.getAST();
		ASTRewrite rewrite = ASTRewrite.create(ast);

		TypeDeclaration newClass = ast.newTypeDeclaration();
		newClass.setName(ast.newSimpleName("NewClassTest1"));

		cu.types().add(newClass);
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
//						System.out.println(modifiedSourceCode);
		SaveFile.saveContentToFile(
				Constant.CONVERT_PROGRAM_SAVE_PATH + "convertPullDownFieldToMove/" + "Bug" + sourceName + ".java",
				modifiedSourceCode);

//				CompilationUnit ast = ParseFile.parseJavaFileToAST(javaFile);
	}
}
