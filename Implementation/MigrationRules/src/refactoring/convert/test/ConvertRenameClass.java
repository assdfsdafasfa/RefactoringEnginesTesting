package refactoring.convert.test;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import refactoring.convert.rule.ConvertRenameClassToExtractSuperclass;
import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.ParseFile;

public class ConvertRenameClass {
	public static void convertRenameClass(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException {
		if (testCaseFile != null) {
			for (IFile javaFile : javaFiles) {// 遍历所有文件路径
				if (javaFile.toString().contains("RenameClass")) {// RenameMethod包所以文件
//					System.out.print("解析CompilationUnit");
					String fileName = FindIdentifier.getClassFromFilePath(javaFile);// 获取当前文件类名
					CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);

					CompilationUnit testCaseCU = ParseFile.parseJavaFileToAST(testCaseFile);
					List<String> arguments = FindIdentifier.getMethodArguments(testCaseCU, fileName);
					if (arguments.size() == 0) {
						System.out.print("arguments is null");
					} else if (arguments.size() >= 2) {
//						System.out.print("RenameMethod:" + javaFile);
						String oldClassName = arguments.get(0).replace("\"", "");
						String newClassNamme = arguments.get(1).replace("\"", "");
//

//						ConvertRenameClassToExtractInterface extractInterface = new ConvertRenameClassToExtractInterface();
//						extractInterface.convertRenameClassToExtractInterface(project, javaFiles, testCaseFile, cu,
//								oldClassName, newClassNamme);

						ConvertRenameClassToExtractSuperclass extractSuperClass = new ConvertRenameClassToExtractSuperclass();
						extractSuperClass.convertRenameClassToExtractSuperclass(project, javaFiles, testCaseFile, cu,
								oldClassName, newClassNamme);
					}
				}
			}
		}

	}
}
