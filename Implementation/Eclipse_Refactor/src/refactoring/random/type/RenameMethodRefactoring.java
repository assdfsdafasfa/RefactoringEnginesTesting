package refactoring.random.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseRenameMethodProcessor;
import refactoring.random.select.Constant;
import refactoring.random.select.PerformRefactoring;

public class RenameMethodRefactoring {

	public static void performRenameMethodRefactoring(IFile file) throws CoreException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;

		IType[] allTypes = iCompilationUnit.getAllTypes();
		List<IMethod> iMethodList = getIMethod(allTypes);
		if (iMethodList.size() > 0) {
			Collections.shuffle(iMethodList);
//			int num = SelectRefactoring.selectRefactoringNumber(iMethodList.size());

			selectRefactoring(iMethodList);

		} else {
			System.out.println("没有可以rename的method");
		}
	}

	public static void selectRefactoring(List<IMethod> iMethodList) {
//		while (iMethodList.size() > 0) {
//		System.out.println("rename method");
//		for (IMethod md : iMethodList) {
//			System.out.println("method:" + md.getElementName());
//		}
//		if (iMethodList.size() == 1) {
		for (int a = 0; a < iMethodList.size(); a++) {
			IMethod iMethod = iMethodList.get(a);
			String newName = GptApi.printMessage(Constant.RENAME_METRHOD_PROMPT + iMethod.getElementName());
//		System.out.println("rename metho newName:" + newName);
			newName = newName.replaceAll(Constant.regEx, "");
			EclipseRenameMethodProcessor processor = new EclipseRenameMethodProcessor(iMethod);
			processor.setNewElementName(newName);
			processor.setUpdateReferences(true);
			RenameRefactoring refactoring = new RenameRefactoring(processor);
			try {
//				refactoring = (RenameRefactoring) createRefactoring(descriptor);
				RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
				try {
					System.out.println("iMethod:" + iMethod.getElementName());
					if (status.isOK()) {
						Change change = refactoring.createChange(new NullProgressMonitor());
						change.perform(new NullProgressMonitor());
						System.out.println("Method renamed successfully.");
//
//						ISourceRange sourceRange = iMethod.getSourceRange();
//						int startPosition = sourceRange.getOffset();
//						int length = sourceRange.getLength();
						PerformRefactoring.no += 1;
						RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
								PerformRefactoring.no, iMethod.getElementName(), newName, PerformRefactoring.filePath,
								0, 1, PerformRefactoring.className, "Rename method");
						SaveData saveData = new SaveData(record);
						break;
					}
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//			break;
//		}
//		else {
//			int pos = SelectRefactoring.randomRefactoringIdentifier(iMethodList.size());
//			IMethod iMethod = iMethodList.get(pos);
//			String newName = GptApi.printMessage(Constant.RENAME_METRHOD_PROMPT + iMethod.getElementName());
//			System.out.println("newName:" + newName);
//			EclipseRenameMethodProcessor processor = new EclipseRenameMethodProcessor(iMethod);
//			processor.setNewElementName(newName);
//			processor.setUpdateReferences(true);
//			RenameRefactoring refactoring = new RenameRefactoring(processor);
//			try {
////				refactoring = (RenameRefactoring) createRefactoring(descriptor);
//				RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
//				try {
//					System.out.println("iMethod:" + iMethod.getElementName());
//					if (status.isOK()) {
//						Change change = refactoring.createChange(new NullProgressMonitor());
//						change.perform(new NullProgressMonitor());
//						System.out.println("Method renamed successfully.");
//						break;
//					} else {
//						iMethodList.remove(pos);
//						selectRefactoring(iMethodList);
//					}
//				} catch (CoreException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			} catch (CoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
//	}

	private static List<IMethod> getIMethod(IType[] allTypes) throws JavaModelException {
		List<IMethod> iMethods = new ArrayList<>();
		IMethod iMethod = null;
		if (allTypes.length > 0) {
			for (IType iType : allTypes) {
				for (IMethod method : iType.getMethods()) {
					iMethods.add(method);
				}
			}
//			if (iMethods.size() == 1) {
//				return iMethods.get(0);
//			} else if (iMethods.size() > 1) {
//				int pos = SelectRefactoring.randomRefactoringIdentifier(iMethods.size());
//				iMethod = iMethods.get(pos);
//				return iMethod;
//			}
		}
		return iMethods;
	}

//	public final static Refactoring createRefactoring(RefactoringDescriptor descriptor) throws CoreException {
//		RefactoringStatus status = new RefactoringStatus();
//		Refactoring refactoring = descriptor.createRefactoring(status);
//		return refactoring;
//	}

}
