package refactoring.random.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringAvailabilityTester;
//import org.eclipse.jdt.internal.corext.refactoring.sef.SelfEncapsulateFieldRefactoring;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.model.SelfEncapsulateFieldRefactoring;
import refactoring.random.select.PerformRefactoring;

public class EncapsulateFieldRefactoring {

	public static void performEncapsulateFieldRefactoring(IFile file) {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;

		try {
			List<IField> iFieldList = getField(iCompilationUnit.getAllTypes());

			if (iFieldList.size() > 0) {
				Collections.shuffle(iFieldList);
//				int num = SelectRefactoring.selectRefactoringNumber(iFieldList.size());
				selectRefactoring(iFieldList);

			} else {
				System.out.println("没有可以Encapsulate field");
			}
		} catch (

		JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void selectRefactoring(List<IField> iFieldList) throws OperationCanceledException, CoreException {
//		while (iFieldList.size() > 0) {
//			System.out.println("encapsulate field");
//			for (IField fd : iFieldList) {
//				System.out.println("fd:" + fd);
//			}
//			if (iFieldList.size() == 1) {
//				IField field = iFieldList.get(0);
//				System.out.println("one:");
//				if (field != null) {
		for (int a = 0; a < iFieldList.size(); a++) {
			System.out.println("select encapsulate field");
			IField iField = iFieldList.get(a);
			SelfEncapsulateFieldRefactoring refactoring = ((!RefactoringAvailabilityTester
					.isSelfEncapsulateAvailable(iField)) ? null : new SelfEncapsulateFieldRefactoring(iField));
			System.out.println("111");
			if (refactoring != null) {
				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
				// perform factoring
				if (status.isOK()) {
					final PerformRefactoringOperation op = new PerformRefactoringOperation(refactoring, 3);
					op.run(new NullProgressMonitor());
//						JavaCore.run(op, new NullProgressMonitor());
					System.out.println("perform encapsulate field refactoring");

					ISourceRange sourceRange = iField.getSourceRange();
					int startPosition = sourceRange.getOffset();
					int length = sourceRange.getLength();
					PerformRefactoring.no += 1;
					RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
							PerformRefactoring.no, iField.getElementName(), iField.getElementName(),
							PerformRefactoring.filePath, startPosition, length, PerformRefactoring.className,
							"excapsulate field");
					SaveData saveData = new SaveData(record);
					break;
				}
			}
		}
	}
//	break;}else{
//
//	int n = SelectRefactoring.randomRefactoringIdentifier(iFieldList.size());
//	IField field = iFieldList.get(n);// List中结果为(int LIMITE_EMPRESTIMO)，包含type,需要解决
//	if(field!=null)
//	{
//		System.out.println("two:");
//		SelfEncapsulateFieldRefactoring refactoring = ((!RefactoringAvailabilityTester
//				.isSelfEncapsulateAvailable(field)) ? null : new SelfEncapsulateFieldRefactoring(field));
//		if (refactoring != null) {
//			RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
//			// perform factoring
//			System.out.println("111");
//			if (status.isOK()) {
//				final PerformRefactoringOperation op = new PerformRefactoringOperation(refactoring, 3);
//				op.run(new NullProgressMonitor());
////						JavaCore.run(op, new NullProgressMonitor());
//				System.out.println("encapsulate field refactoring");
//				break;
//			}
//		} else {
//			iFieldList.remove(n);
//			selectRefactoring(iFieldList);
//		}
//	}
//	}}}

	public static List<IField> getField(IType[] allTypes) throws JavaModelException {
		List<IField> iFieldList = new ArrayList<>();
		for (IType type : allTypes) {
			IField[] iField = type.getFields();
			for (IField i : iField) {
				System.out.println("field:" + i + " " + "modifier:" + i);
				if (Flags.isPublic(i.getFlags())) {
					iFieldList.add(i);
				}
			}
//			if (iField.length == 1) {
//				return iField[0];
//			} else if (iField.length > 1) {
//				int n = SelectRefactoring.randomRefactoringIdentifier(iField.length);
//				return iField[n];
//			}
		}
		return iFieldList;
	}
}
