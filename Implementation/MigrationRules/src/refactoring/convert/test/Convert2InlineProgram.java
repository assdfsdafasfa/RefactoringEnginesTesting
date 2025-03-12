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

import refactoring.convert.rule.ConvertMoveFieldToInline;
import refactoring.convert.rule.ConvertMoveMethodToInline;
import refactoring.convert.rule.ConvertRenameFieldToInline;
import refactoring.convert.rule.ConvertRenameMethodToInline;
import refactoring.convert.rule.ConvertRenameVariableToinline;
import refactoring.file.parse.ParseFile;
import utils.Constant;

public class Convert2InlineProgram {
	public static void convert2InlinePrograms(IProject project, List<IFile> javaFiles, IFile testCaseFile)
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
							ConvertMoveMethodToInline toInline = new ConvertMoveMethodToInline();
							toInline.convertMoveMethodToInline(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("MoveField")) {
							ConvertMoveFieldToInline toInline = new ConvertMoveFieldToInline();
							toInline.convertMoveFieldToInline(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("RenameMethod")) {
							ConvertRenameMethodToInline toInline = new ConvertRenameMethodToInline();
							toInline.convertRenameToInline(project, javaFiles, testCaseFile, cu, sourceClass, refName,
									targetClass);
						} else if (javaFile.toString().contains("RenameField")) {
							ConvertRenameFieldToInline toInline = new ConvertRenameFieldToInline();
							toInline.convertRenameFieldToInline(null, null, null);
						} else if (javaFile.toString().contains("RenameVariable")) {
							ConvertRenameVariableToinline toInline = new ConvertRenameVariableToinline();
							toInline.convertRenameVariableToinline(null, null, null);
						}
					}
				}
			}
		}
	}
}
