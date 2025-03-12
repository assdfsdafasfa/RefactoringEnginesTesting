package refactoring.convert.rule;

import java.util.List;

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

import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.SaveFile;
import utils.Constant;

//Rename -> Move 全转换，任意都可转化
public class ConvertRenameMethodToMove {
	// testCaseFile 为测试用例所在文件
	public static void convertRenameToMove(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String fileName, String oldMethodName, String newMethodName)
			throws JavaModelException, IllegalArgumentException {

//						System.out.print("newMethodName:" + newMethodName);
		MethodDeclaration methodDec = FindIdentifier.findMethodDeclaration(cu, oldMethodName);
//		System.out.println("methodDec:" + methodDec);

		AST ast = cu.getAST();
		ASTRewrite rewrite = ASTRewrite.create(ast);

		TypeDeclaration newClass = ast.newTypeDeclaration();
		newClass.setName(ast.newSimpleName("NewClassTest1"));

		MethodDeclaration newMethod = ast.newMethodDeclaration();
		System.out.print("newMethodName:" + newMethodName);

		SimpleName name = ast.newSimpleName(newMethodName);
		newMethod.setName(name);
		newMethod.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
		newMethod.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

		Block methodBody = ast.newBlock();
		newMethod.setBody(methodBody);
//						methodDec.setName(ast.newSimpleName(newMethodName));
		newClass.bodyDeclarations().add(newMethod);

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
		SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH + "moveMethod/" + "Bug" + fileName + ".java",
				modifiedSourceCode);

//				CompilationUnit ast = ParseFile.parseJavaFileToAST(javaFile);
	}
}
