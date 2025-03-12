package refactoring.convert.test;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import refactoring.convert.rule.ConvertMoveMethodToRename;
import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.ParseFile;

public class ConvertMoveMethod {
	public static void convertMoveMethod(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException {
		if (testCaseFile != null) {
			for (IFile javaFile : javaFiles) {// 遍历所有文件路径
				if (javaFile.toString().contains("MoveMethod")) {// RenameMethod包所以文件
//					System.out.print("解析CompilationUnit");
					String fileName = FindIdentifier.getClassFromFilePath(javaFile);// 获取当前文件类名
					CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);

					CompilationUnit testCaseCU = ParseFile.parseJavaFileToAST(testCaseFile);
					List<String> arguments = FindIdentifier.getMethodArguments(testCaseCU, fileName);
					if (arguments.size() == 0) {
						System.out.print("arguments is null");
					} else if (arguments.size() >= 2) {
//						System.out.print("RenameMethod:" + javaFile);
						String methodName = arguments.get(0).replace("\"", "");
						String targetClass = arguments.get(2).replace("\"", "");
//						String targetClass = "";
//						ConvertMoveMethodToExtract toExtract = new ConvertMoveMethodToExtract();
//						toExtract.convertMoveMethodToExtract(project, javaFiles, testCaseFile, cu, fileName, methodName,
//								targetClass);

//						ConvertMoveMethodToInline toInline = new ConvertMoveMethodToInline();
//						toInline.convertMoveMethodToInline(project, javaFiles, testCaseFile, cu, fileName, methodName,
//								targetClass);

//						ConvertMoveMethodToPullDown toPullDown = new ConvertMoveMethodToPullDown();
//						toPullDown.convertMoveMethodToPullDown(project, javaFiles, testCaseFile, cu, fileName,
//								methodName, targetClass);
//
//						ConvertMoveMethodToPullUp toPullUp = new ConvertMoveMethodToPullUp();
//						toPullUp.convertMoveMethodToPullUp(project, javaFiles, testCaseFile, cu, fileName, methodName,
//								targetClass);

						ConvertMoveMethodToRename toRename = new ConvertMoveMethodToRename();
						toRename.convertMoveMethodToRename(project, javaFiles, testCaseFile, cu, fileName, methodName,
								targetClass);

					}
				}
			}
		}

	}
}
