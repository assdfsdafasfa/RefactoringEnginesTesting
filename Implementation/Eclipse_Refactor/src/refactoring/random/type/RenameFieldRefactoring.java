package refactoring.random.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseRenameFieldProcessor;
import refactoring.random.select.Constant;
import refactoring.random.select.PerformRefactoring;

public class RenameFieldRefactoring {
	public static void performFieldRenameRefactoring(IFile file) throws CoreException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		IType[] allTypes = iCompilationUnit.getAllTypes();

		List<IField> iFieldList = getIField(allTypes);
		if (iFieldList.size() > 0) {
			Collections.shuffle(iFieldList);
//			int num = SelectRefactoring.selectRefactoringNumber(iFieldList.size());
			selectRefactoring(iFieldList);

		} else {
			System.out.println("没有可以rename的field");
		}
	}

	public static void selectRefactoring(List<IField> iFieldList) throws CoreException {
//		while (iFieldList.size() > 0) {
//		System.out.println("rename field");
//		for (IField fd : iFieldList) {
//			System.out.println("fd:" + fd.getElementName());
//		}
//		if (iFieldList.size() == 0) {
//			System.out.println("没有field可以重命名");
//		}

//		if (iFieldList.size() == 1) {
//		IField iField = iFieldList.get(0);
		for (int a = 0; a < iFieldList.size(); a++) {
			IField iField = iFieldList.get(a);
			System.out.println("rename field:" + iField);
			String newName = GptApi.printMessage(Constant.RENAME_FIELD_PROMPT + iField.getElementName());
			newName = newName.replaceAll(Constant.regEx, "");
			System.out.println("rename field newName:" + newName);
			if (newName != null) {
				EclipseRenameFieldProcessor processor = new EclipseRenameFieldProcessor(iField);
				processor.setNewElementName(newName);
				processor.setDelegateUpdating(true);

				RenameRefactoring refactoring = new RenameRefactoring(processor);
				if (refactoring != null) {
					RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor()); // 检查所有条件
//			System.out.println("111");
					if (status.isOK()) {
//				System.out.println("rename fiel");
						Change change = refactoring.createChange(new NullProgressMonitor());
						change.perform(new NullProgressMonitor()); // 执行重构
						System.out.println("rename field rename field refactoring");

//					ISourceRange sourceRange = iField.getSourceRange();
//					int startPosition = sourceRange.getOffset();
//					int length = sourceRange.getLength();
						PerformRefactoring.no += 1;
						RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
								PerformRefactoring.no, iField.getElementName(), newName, PerformRefactoring.filePath, 0,
								1, PerformRefactoring.className, "Rename field");
						SaveData saveData = new SaveData(record);
						break;
					}
				}
			}
		}
//			break;
//		} else {
////			if (iField != null) {
//			int pos = SelectRefactoring.randomRefactoringIdentifier(iFieldList.size());
//			IField iField = iFieldList.get(pos);
//			String newName = GptApi.printMessage(Constant.RENAME_FIELD_PROMPT + iField.getElementName());
//			System.out.println("newName:" + newName);
//			if (newName != null) {
//				RenameFieldProcessor processor = new RenameFieldProcessor(iField);
//				processor.setNewElementName(newName);
//				processor.checkInitialConditions(null);
//
//				Refactoring refactoring = new ProcessorBasedRefactoring(processor);
//				RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor()); // 检查所有条件
//				if (status.isOK()) {
//					Change change = refactoring.createChange(new NullProgressMonitor());
//					change.perform(new NullProgressMonitor()); // 执行重构
//					System.out.println("perform rename field refactoring");
//					break;
//				} else {
//					iFieldList.remove(pos);
//					selectRefctoring(iFieldList);
//				}
//			}
//		}
//	}

	}

	public static List<IField> getIField(IType[] iTypes) throws JavaModelException {
		List<IField> fields = new ArrayList<>();
//		IField iField = null;
		for (IType type : iTypes) {
			for (IField field : type.getFields()) {
				fields.add(field);
			}
		}
//		if (fields.size() == 1) {
//			return fields.get(0);
//		} else if (fields.size() > 1) {
//			int pos = SelectRefactoring.randomRefactoringIdentifier(fields.size());
//			iField = fields.get(pos);
//		}
		return fields;
	}
}
