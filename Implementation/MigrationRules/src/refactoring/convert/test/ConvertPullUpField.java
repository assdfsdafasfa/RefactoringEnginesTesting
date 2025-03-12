package refactoring.convert.test;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.ParseFile;

public class ConvertPullUpField {
	public static void convertPullUpField(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException {
		if (testCaseFile != null) {
			for (IFile javaFile : javaFiles) {// 遍历所有文件路径
				if (javaFile.toString().contains("PullUpField")) {// RenameMethod包所以文件
//					System.out.print("解析CompilationUnit");
					String fileName = FindIdentifier.getClassFromFilePath(javaFile);// 获取当前文件类名
					CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);

					CompilationUnit testCaseCU = ParseFile.parseJavaFileToAST(testCaseFile);
					List<String> arguments = FindIdentifier.getMethodArguments(testCaseCU, fileName);
					if (arguments.size() == 0) {
						System.out.print("arguments is null");
					} else if (arguments.size() >= 2) {
//						System.out.print("RenameMethod:" + javaFile);
						String fieldName = arguments.get(0).replace("\"", "");
//						String newMethodNamme = arguments.get(1).replace("\"", "");

//						ConvertPullUpFieldToMove toMove = new ConvertPullUpFieldToMove();
//						toMove.convertPullUpFieldToMove(project, javaFiles, testCaseFile, cu, fileName, fieldName);

//						ConvertPullUpFieldToRename toRename = new ConvertPullUpFieldToRename();
//						toRename.convertPullUpFieldToRename(project, javaFiles, testCaseFile, cu, fileName, fieldName);
					}
				}
			}
		}

	}
}
