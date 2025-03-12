package refactoring.convert.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import utils.Constant;

public class ConvertRuleTest {
	public void run(IProject project, List<IFile> javaFiles, IFile testCaseFile)
			throws JavaModelException, IllegalArgumentException, MalformedTreeException, FileNotFoundException,
			BadLocationException, IOException {
//		System.out.println("testing");
		Convert2RenameProgram.convert2RenamePrograms(project, javaFiles, testCaseFile);

		Convert2MoveProgram.convert2MovePrograms(project, javaFiles, testCaseFile);

		Convert2InlineProgram.convert2InlinePrograms(project, javaFiles, testCaseFile);

		Convert2ExtractProgram.convert2ExtractPrograms(project, javaFiles, testCaseFile);
//		ConvertRenameMethod.convertRenameMethod(project, javaFiles, testCaseFile);

//		ConvertRenameField.convertRenameField(project, javaFiles, testCaseFile);
//
//		ConvertRenameClass.convertRenameClass(project, javaFiles, testCaseFile);
//
//		ConvertPullUpMethod.convertPullUpMethod(project, javaFiles, testCaseFile);
//
//		ConvertPullUpField.convertPullUpField(project, javaFiles, testCaseFile);
//
//		ConvertPullDownMethod.convertPullDownMethod(project, javaFiles, testCaseFile);
//
//		ConvertPullDownField.convertPullDownField(project, javaFiles, testCaseFile);
//
//		ConvertMoveField.convertMoveField(project, javaFiles, testCaseFile);
//
//		ConvertMoveMethod.convertMoveMethod(project, javaFiles, testCaseFile);
//
//		ConvertRenameVariableToExtract.convertRenameVariableToExtract(project, javaFiles, testCaseFile);

//		ConvertMoveMethod.convertPullUpMethod(project, javaFiles, testCaseFile);
	}

	public static List<String> getAllMethodName(ASTNode cuu) {
		List<String> methodNameList = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				methodNameList.add(((MethodDeclaration) node).getName().toString());
				return true;
			}
		});
		return methodNameList;
	}

	public static List<String> getAllFieldName(ASTNode cuu) {
		List<String> fieldNameList = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(FieldDeclaration node) {
				VariableDeclarationFragment fragment = (VariableDeclarationFragment) ((FieldDeclaration) node)
						.fragments().get(0);
				String fieldName = fragment.getName().getIdentifier(); // 获取字段名
				fieldNameList.add(fieldName);
				return true;
			}
		});
		return fieldNameList;
	}

	public static String getNewMethodName(List<String> methodNameList) {
		String createMethodName = null;
		for (String name : Constant.RANDOM_SELECT_NEW_METHOD_NAME) {
//			System.out.println(num);
			if (!methodNameList.contains(name)) {
				createMethodName = name;
				return createMethodName;
			}
		}
		return null;
	}

	public static String getNewFieldName(List<String> fieldNameList) {
		String createMethodName = null;
		for (String name : Constant.RANDOM_SELECT_NEW_FIELD_NAME) {
//			System.out.println(num);
			if (!fieldNameList.contains(name)) {
				createMethodName = name;
				return createMethodName;
			}
		}
		return null;
	}
}
