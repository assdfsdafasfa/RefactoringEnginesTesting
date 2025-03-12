package refactoring.convert.test;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import refactoring.convert.rule.ConvertRenameMethodToExtract;
import refactoring.convert.rule.ConvertRenameMethodToInline;
import refactoring.convert.rule.ConvertRenameMethodToMove;
import refactoring.convert.rule.ConvertRenameMethodToPullDown;
import refactoring.convert.rule.ConvertRenameMethodToPullUp;
import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.ParseFile;

public class ConvertRenameMethod {
	public static void convertRenameMethod(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException {
//		System.out.println("project:" + project);
//		System.out.println("javaFiles:" + javaFiles);
//		System.out.println("testCaseFile:" + testCaseFile);
		if (testCaseFile != null) {
			for (IFile javaFile : javaFiles) {// 遍历所有文件路径
				if (javaFile.toString().contains("RenameMethod")) {// RenameMethod包文件
//					System.out.print("解析CompilationUnit");
					String fileName = FindIdentifier.getClassFromFilePath(javaFile);// 获取当前文件类名
					CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);

					CompilationUnit testCaseCU = ParseFile.parseJavaFileToAST(testCaseFile);
					List<String> arguments = FindIdentifier.getMethodArguments(testCaseCU, fileName);
					if (arguments.size() == 0) {
						System.out.print("arguments is null");
					} else if (arguments.size() >= 2) {
//						System.out.print("RenameMethod:" + javaFile);
						String oldMethodName = arguments.get(0).replace("\"", "");
						String newMethodName = arguments.get(1).replace("\"", "");

						ConvertRenameMethodToMove toMove = new ConvertRenameMethodToMove();
						toMove.convertRenameToMove(project, javaFiles, testCaseFile, cu, fileName, oldMethodName,
								newMethodName);

						ConvertRenameMethodToInline toInline = new ConvertRenameMethodToInline();
						toInline.convertRenameToInline(project, javaFiles, testCaseFile, cu, fileName, oldMethodName,
								newMethodName);

						ConvertRenameMethodToPullDown toPullDown = new ConvertRenameMethodToPullDown();
						toPullDown.convertRenameMethodToPullDown(project, javaFiles, testCaseFile, cu, fileName,
								oldMethodName, newMethodName);

						ConvertRenameMethodToPullUp toPullUp = new ConvertRenameMethodToPullUp();
						toPullUp.convertRenameMethodToPullUp(project, javaFiles, testCaseFile, cu, fileName,
								oldMethodName, newMethodName);

						ConvertRenameMethodToExtract toExtract = new ConvertRenameMethodToExtract();
						toExtract.convertRenameMethodToExtract(project, javaFiles, testCaseFile, cu, fileName,
								oldMethodName, newMethodName);

					}
				}
			}
		}

	}
}
