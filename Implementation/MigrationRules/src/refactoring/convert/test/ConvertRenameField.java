package refactoring.convert.test;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import refactoring.convert.rule.ConvertRenameFieldToPullUp;
import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.ParseFile;

public class ConvertRenameField {
	public static void convertRenameField(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException {
		if (testCaseFile != null) {
			for (IFile javaFile : javaFiles) {// 遍历所有文件路径
				if (javaFile.toString().contains("RenameField")) {// RenameMethod包所以文件
//					System.out.print("解析CompilationUnit");
					String fileName = FindIdentifier.getClassFromFilePath(javaFile);// 获取当前文件类名
					CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);

					CompilationUnit testCaseCU = ParseFile.parseJavaFileToAST(testCaseFile);
					List<String> arguments = FindIdentifier.getMethodArguments(testCaseCU, fileName);
					if (arguments.size() == 0) {
						System.out.print("arguments is null");
					} else if (arguments.size() >= 2) {
//						System.out.print("RenameMethod:" + javaFile);
						String oldFieldName = arguments.get(0).replace("\"", "");
						String newFieldNamme = arguments.get(1).replace("\"", "");
//
//						ConvertRenameFieldToMove toMove = new ConvertRenameFieldToMove();
//						toMove.convertRenameFieldToMove(project, javaFiles, testCaseFile, cu, fileName, oldFieldName,
//								newFieldNamme);
//
//						ConvertRenameFieldToPullDown toPullDown = new ConvertRenameFieldToPullDown();
//						toPullDown.convertRenameFieldToPullDown(project, javaFiles, testCaseFile, cu, fileName,
//								oldFieldName, newFieldNamme);

						ConvertRenameFieldToPullUp toPullUp = new ConvertRenameFieldToPullUp();
						toPullUp.convertRenameFieldToPullUp(project, javaFiles, testCaseFile, cu, fileName,
								oldFieldName, newFieldNamme);
					}
				}
			}
		}

	}
}
