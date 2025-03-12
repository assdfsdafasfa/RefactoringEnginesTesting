package refactoring.convert.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import refactoring.convert.rule.ConvertMoveFieldToExtract;
import refactoring.convert.rule.ConvertMoveMethodToExtract;
import refactoring.convert.rule.ConvertRenameClassToExtract;
import refactoring.convert.rule.ConvertRenameFieldToExtract;
import refactoring.convert.rule.ConvertRenameMethodToExtract;
import refactoring.convert.rule.ConvertRenameVariableToExtract;
import refactoring.file.parse.ParseFile;
import utils.Constant;

public class Convert2ExtractProgram {
	public static void convert2ExtractPrograms(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException, FileNotFoundException, IOException {
		if (testCaseFile != null) {
			for (IFile javaFile : javaFiles) {// 遍历所有文件路径
				CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);
				try (BufferedReader br = new BufferedReader(new FileReader(Constant.CSV_MOVE_FILE_PATH))) {
					String line;
					while ((line = br.readLine()) != null) {
						String[] columns = line.split(",");
						String sourceClass = columns[0]; // 第一列
						String refName = columns[1]; // 第二列
						String targetClass = columns[2]; // 第三列
						String id = columns[3]; // 第三列
						ICompilationUnit icu = JavaCore.createCompilationUnitFrom(javaFile);
						if (javaFile.toString().contains("MoveMethod")) {
							ConvertMoveMethodToExtract toExtract = new ConvertMoveMethodToExtract();
							toExtract.convertMoveMethodToExtract(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("MoveField")) {
							ConvertMoveFieldToExtract toExtract = new ConvertMoveFieldToExtract();
							toExtract.convertMoveFieldToExtract(null, null, null);
						} else if (javaFile.toString().contains("RenameClass")) {
							ConvertRenameClassToExtract toExtract = new ConvertRenameClassToExtract();
							toExtract.convertRenameClassToExtractInterface(project, javaFiles, testCaseFile, cu, null,
									null);
						} else if (javaFile.toString().contains("RenameMethod")) {
							ConvertRenameMethodToExtract toExtract = new ConvertRenameMethodToExtract();
							toExtract.convertRenameMethodToExtract(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("RenameField")) {
							ConvertRenameFieldToExtract toExtract = new ConvertRenameFieldToExtract();
							toExtract.convertRenameFieldToExtract(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("RenameVariable")) {
							ConvertRenameVariableToExtract toExtract = new ConvertRenameVariableToExtract();
							toExtract.convertRenameVariableToExtract(project, javaFiles, testCaseFile);
						}
					}
				}
			}

		}
	}

}
