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

import refactoring.convert.rule.ConvertExtractClassToMove;
import refactoring.convert.rule.ConvertExtractFieldToMove;
import refactoring.convert.rule.ConvertExtractMethodToMove;
import refactoring.convert.rule.ConvertInlineFieldToMove;
import refactoring.convert.rule.ConvertInlineMethodToMove;
import refactoring.convert.rule.ConvertRenameClassToMove;
import refactoring.convert.rule.ConvertRenameFieldToMove;
import refactoring.convert.rule.ConvertRenameMethodToMove;
import refactoring.file.parse.ParseFile;
import utils.Constant;

public class Convert2MoveProgram {
	public static void convert2MovePrograms(IProject project, List<IFile> javaFiles, IFile testCaseFile)
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
						if (javaFile.toString().contains("RnameMethod")) {
							ConvertRenameMethodToMove toRename = new ConvertRenameMethodToMove();
							toRename.convertRenameToMove(project, javaFiles, testCaseFile, cu, sourceClass, refName,
									targetClass);
						} else if (javaFile.toString().contains("RenameField")) {
							ConvertRenameFieldToMove toRename = new ConvertRenameFieldToMove();
							toRename.convertRenameFieldToMove(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("RenameClass")) {
							ConvertRenameClassToMove toMove = new ConvertRenameClassToMove();
							toMove.convertRenameClassToMove(project, javaFiles, testCaseFile, cu, sourceClass, refName,
									targetClass);
						} else if (javaFile.toString().contains("ExtractClass")) {
							ConvertExtractClassToMove toMove = new ConvertExtractClassToMove();
							toMove.convertExtractClassToMove(icu, cu, refName, targetClass);
						} else if (javaFile.toString().contains("ExtractMethod")) {
							ConvertExtractMethodToMove toMove = new ConvertExtractMethodToMove();
							toMove.convertExtractMethodToMove(icu, cu, refName, targetClass);
						} else if (javaFile.toString().contains("ExtractField")) {
							ConvertExtractFieldToMove toMove = new ConvertExtractFieldToMove();
							toMove.convertExtractFieldToMove(icu, cu, refName, targetClass);
						} else if (javaFile.toString().contains("InlineMethod")) {
							ConvertInlineMethodToMove toMove = new ConvertInlineMethodToMove();
							toMove.convertInlineMethodToMove(icu, cu, refName, targetClass);
						} else if (javaFile.toString().contains("InlineField")) {
							ConvertInlineFieldToMove toMove = new ConvertInlineFieldToMove();
							toMove.convertInlineFieldToMove(icu, cu, refName, targetClass);
						}
					}
				}
			}

		}
	}

}
