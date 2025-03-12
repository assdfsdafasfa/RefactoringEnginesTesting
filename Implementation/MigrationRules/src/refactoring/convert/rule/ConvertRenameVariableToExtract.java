package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.CompilationUnit;

import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.ParseFile;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertRenameVariableToExtract {
	String variableType = null;

	// 选择重命名变量右边赋值表达式进行提取，名称与新的变量名一致
	public static void convertRenameVariableToExtract(IProject project, List<IFile> javaFiles, IFile testCaseFile) {
		for (IFile javaFile : javaFiles) {// 遍历所有文件路径
			if (javaFile.toString().contains("RenameVariable")) {// RenameMethod包所以文件
//				System.out.print("解析CompilationUnit");
				String fileName = FindIdentifier.getClassFromFilePath(javaFile);// 获取当前文件类名
				CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);
				String sourceCode = cu.toString();
				SaveFile.saveContentToFile(
						Constant.CONVERT_PROGRAM_SAVE_PATH + "convertRenameVariableToExtract/" + "Bug" + fileName
								+ ".java",
						sourceCode);
			}
		}
//		cu.accept(new ASTVisitor() {
//			public boolean visit(VariableDeclarationFragment node) {
//				if (node.getName().getIdentifier().equals(oldVariableName)) {
//					variableType = node.getParent().getParent().toString();
//				}
//				return false;
//			}
//		});
//
//		AST ast = cu.getAST();
//		ASTRewrite rewrite = ASTRewrite.create(ast);

//		if (variableType.equals("int")) {
//			VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
//			fragment.setName(ast.newSimpleName("i"));
//			fragment.setInitializer(ast.newNumberLiteral("0"));
//			VariableDeclarationStatement variableDeclarationStatement = ast.newVariableDeclarationStatement(fragment);
//			variableDeclarationStatement.setType(ast.newPrimitiveType(PrimitiveType.INT));
//		} else if (variableType.equals("String")) {
//
//		} else if (variableType.equals("boolean")) {
//
//		}
	}

}

