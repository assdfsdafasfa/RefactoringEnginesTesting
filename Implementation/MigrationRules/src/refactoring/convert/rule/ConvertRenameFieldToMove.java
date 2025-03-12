package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertRenameFieldToMove {
	public static void convertRenameFieldToMove(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String fileName, String oldFieldName, String newFieldName)
			throws JavaModelException, IllegalArgumentException {

//						System.out.print("newMethodName:" + newMethodName);
		FieldDeclaration fieldDec = FindIdentifier.findFieldDeclaration(cu, oldFieldName);
//		System.out.println("methodDec:" + methodDec);

		AST ast = cu.getAST();
		ASTRewrite rewrite = ASTRewrite.create(ast);

		TypeDeclaration newClass = ast.newTypeDeclaration();
		newClass.setName(ast.newSimpleName("NewClassTest1"));

		Type fieldType = fieldDec.getType();
		FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(ast.newVariableDeclarationFragment());

//		String fieldType1 = fieldType.toString().replace(" ", "");
//		System.out.println("TYPE:" + fieldType1.toString() + "\n");
		fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));// ------------------------需要修改------------------------------
		fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		((VariableDeclarationFragment) fieldDeclaration.fragments().get(0)).setName(ast.newSimpleName(newFieldName));

//						methodDec.setName(ast.newSimpleName(newMethodName));
		newClass.bodyDeclarations().add(fieldDeclaration);

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
				Constant.CONVERT_PROGRAM_SAVE_PATH + "renameFieldToMove/" + "Bug" + fileName + ".java",
				modifiedSourceCode);

//				CompilationUnit ast = ParseFile.parseJavaFileToAST(javaFile);
	}
}
