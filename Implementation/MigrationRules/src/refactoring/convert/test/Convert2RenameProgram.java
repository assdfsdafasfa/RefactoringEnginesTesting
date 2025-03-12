package refactoring.convert.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import refactoring.convert.rule.ConvertExtractClassToRename;
import refactoring.convert.rule.ConvertExtractFieldToRename;
import refactoring.convert.rule.ConvertExtractMethodToRename;
import refactoring.convert.rule.ConvertExtractVariableToRename;
import refactoring.convert.rule.ConvertInlineFieldToRename;
import refactoring.convert.rule.ConvertInlineMethodToRename;
import refactoring.convert.rule.ConvertInlineVariableToRename;
import refactoring.convert.rule.ConvertMoveFieldToRename;
import refactoring.convert.rule.ConvertMoveMethodToRename;
import refactoring.convert.rule.ConvertPullDownFieldToRename;
import refactoring.convert.rule.ConvertPullDownMethodToRename;
import refactoring.convert.rule.ConvertPullUpFieldToRename;
import refactoring.convert.rule.ConvertPullUpMethodToRename;
import refactoring.file.parse.ParseFile;
import utils.Constant;

public class Convert2RenameProgram {
	public static void convert2RenamePrograms(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException, MalformedTreeException, BadLocationException,
			FileNotFoundException, IOException {
//		System.out.println("testCaseFile:" + testCaseFile);
		if (javaFiles != null) {
			for (IFile javaFile : javaFiles) {// 遍历所有文件路径
//				System.out.println("javaFile:" + javaFile);
				CompilationUnit cu = ParseFile.parseJavaFileToAST(javaFile);
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(new FileInputStream(Constant.CSV_MOVE_FILE_PATH), "UTF-8"))) {
					String line;
					while ((line = br.readLine()) != null) {
						String[] columns = line.split(",");
						String sourceClass = columns[0]; // 第一列
						String refName = columns[1]; // 第二列
						String targetClass = columns[2]; // 第三列
						String id = columns[3]; // 第三lie
//						System.out.println("sourceClass:" + sourceClass);
//						System.out.println("refName:" + refName);
//						System.out.println("targetClass:" + targetClass);
//						System.out.println("id:" + id);
						if (javaFile.toString().contains("MoveMethod") && javaFile.toString().contains(id)) {
							ConvertMoveMethodToRename toRename = new ConvertMoveMethodToRename();
							toRename.convertMoveMethodToRename(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("MoveField") && javaFile.toString().contains(id)) {
							ConvertMoveFieldToRename toRename = new ConvertMoveFieldToRename();
							toRename.convertMoveFieldToRename(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("PullDownMethod") && javaFile.toString().contains(id)) {
							ConvertPullDownMethodToRename toRename = new ConvertPullDownMethodToRename();
							toRename.convertPullDownMethodToRename(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("PullDownField") && javaFile.toString().contains(id)) {
							ConvertPullDownFieldToRename toRename = new ConvertPullDownFieldToRename();
							toRename.convertPullDownFieldToRename(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("PullUpField") && javaFile.toString().contains(id)) {
							ConvertPullUpFieldToRename toRename = new ConvertPullUpFieldToRename();
							toRename.convertPullUpFieldToRename(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("PullUpMethod") && javaFile.toString().contains(id)) {
							ConvertPullUpMethodToRename toRename = new ConvertPullUpMethodToRename();
							toRename.convertPullUpMethodToRename(project, javaFiles, testCaseFile, cu, sourceClass,
									refName, targetClass);
						} else if (javaFile.toString().contains("MoveClass") && javaFile.toString().contains(id)) {
							ConvertMoveMethodToRename.convertMoveMethodToRename(project, javaFiles, testCaseFile, cu,
									sourceClass, refName, targetClass);
						} else if (javaFile.toString().contains("ExtractClass") && javaFile.toString().contains(id)) {
							ConvertExtractClassToRename.convertExtractClassToRename(project, javaFiles, testCaseFile,
									cu, sourceClass, refName, targetClass);
						} else if (javaFile.toString().contains("ExtractMethod") && javaFile.toString().contains(id)) {
							ConvertExtractMethodToRename.convertExtractMethodToRename(project, javaFiles, testCaseFile,
									cu, sourceClass, refName, targetClass);
						} else if (javaFile.toString().contains("ExtractField") && javaFile.toString().contains(id)) {
							ConvertExtractFieldToRename.convertExtractFieldToRename(project, javaFiles, testCaseFile,
									cu, sourceClass, refName, targetClass);
						} else if (javaFile.toString().contains("ExtractVariable")
								&& javaFile.toString().contains(id)) {
							ConvertExtractVariableToRename.convertExtractVariableToRename(project, javaFiles,
									testCaseFile, cu, sourceClass, refName, targetClass);
						} else if (javaFile.toString().contains("InlineMethod") && javaFile.toString().contains(id)) {
							ConvertInlineMethodToRename.convertInlineMethodToRename(project, javaFiles, testCaseFile,
									cu, sourceClass, refName, targetClass);
						} else if (javaFile.toString().contains("InlineField") && javaFile.toString().contains(id)) {
							ConvertInlineFieldToRename.convertInlineFieldToRename(project, javaFiles, testCaseFile, cu,
									sourceClass, refName, targetClass);
						} else if (javaFile.toString().contains("InlineVariable") && javaFile.toString().contains(id)) {
							ConvertInlineVariableToRename.convertInlineVariableToRename(project, javaFiles,
									testCaseFile, cu, sourceClass, refName, targetClass);
						}
					}
				}
			}
		}
	}
}
