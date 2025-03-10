package refactoring.random.type;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseRenameTypeProcessor;
import refactoring.random.select.Constant;
import refactoring.random.select.PerformRefactoring;

public class RenameTypeRefactoring {
	public static void performRenameTypeRefactoring(IFile file) throws CoreException {

		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		IType[] allTypes = iCompilationUnit.getAllTypes();
		if (allTypes.length == 0) {
			System.out.println("没有type可以重构");
		} else {
			selectTypeRefactoring(allTypes[0]);
		}
	}

//		IType iType = getIType(allTypes);
	public static void selectTypeRefactoring(IType selecType) throws CoreException {
//		while (allTypes.length > 0) {
//			System.out.println("rename type");
//			for (IType i : allTypes) {
//				System.out.println("type:" + i.getElementName());
//			}
//			if (allTypes.length == 1) {
//				IType selecType = allTypes[0];
		String newName = GptApi.printMessage(Constant.RENAME_TYPT_PROMPT + selecType.getElementName());

		newName = newName.replaceAll(Constant.regEx, "");
//				RenameJavaElementDescriptor descriptor = RefactoringSignatureDescriptorFactory
//						.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_TYPE);
		EclipseRenameTypeProcessor processor = new EclipseRenameTypeProcessor(selecType);
		processor.setNewElementName(newName);
//		processor.setUpdateReferences(true);

//				RefactoringStatus status = new RefactoringStatus();
		RenameRefactoring refactoring = new RenameRefactoring(processor);
//				RenameTypeProcessor refactoring = (RenameTypeProcessor) ((RenameRefactoring) ref).getProcessor();
//		System.out.println("111");
		System.out.println("refactoring:" + refactoring);
		if (refactoring != null && refactoring.checkInitialConditions(new NullProgressMonitor()).isOK()) {
//			System.out.println("222");
			Change change = refactoring.createChange(new NullProgressMonitor());
			change.perform(new NullProgressMonitor()); // 执行重构
			System.out.println("type renamed successfully.");

			ISourceRange sourceRange = selecType.getSourceRange();
			int startPosition = sourceRange.getOffset();
			int length = sourceRange.getLength();
			PerformRefactoring.no += 1;
			RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa, PerformRefactoring.no,
					selecType.getElementName(), newName, PerformRefactoring.filePath, startPosition, length,
					PerformRefactoring.className, "Rename class");
			SaveData saveData = new SaveData(record);
//			record.setNo(0);
//			record.setProjectName();
//			record.setPath();
//			record.setClassName();
//			record.setRefactoringType();
//			record.setOldName();
//			record.setNewName();
//			record.setOffset();
//			record.setLength();
		}
//		break;
//	}else
//
//	{
//		int n = SelectRefactoring.randomRefactoringIdentifier(allTypes.length);
//		IType selecType = allTypes[n];
//		if (selecType != null) {
//			String newName = GptApi.printMessage(Constant.RENAME_TYPT_PROMPT + selecType.getElementName());
//			System.out.println("newName:" + newName);
//			RenameJavaElementDescriptor descriptor = RefactoringSignatureDescriptorFactory
//					.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_TYPE);
//			descriptor.setJavaElement(selecType);
//			descriptor.setNewName(newName);
//			descriptor.setUpdateReferences(true);
//
//			RefactoringStatus status = new RefactoringStatus();
//			Refactoring ref = descriptor.createRefactoring(status);
//
//			RenameTypeProcessor refactoring = (RenameTypeProcessor) ((RenameRefactoring) ref).getProcessor();
//			if (ref.checkAllConditions(new NullProgressMonitor()).isOK()) {
//				Change change = refactoring.createChange(new NullProgressMonitor());
//				change.perform(new NullProgressMonitor()); // 执行重构
//				System.out.println("type renamed successfully.");
//				break;
//			} else {
//				IType[] newArray = removeElement(allTypes, selecType);
//				selectTypeRefactoring(newArray);
//			}
//		}
//	}
	}
//	}

	public static IType[] removeElement(IType[] array, IType element) {
		int newSize = array.length - 1;
		IType[] newArray = new IType[newSize];
		int newIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != element) {
				newArray[newIndex] = array[i];
				newIndex++;
			}
		}
		return newArray;
	}
}

//	public static IType getIType(IType[] allTypes) throws CoreException {
//		List<IType> types = new ArrayList();
//		if (allTypes.length == 1) {
//			return allTypes[0];
//		} else if (allTypes.length > 1) {
//			int n = SelectRefactoring.randomRefactoringIdentifier(allTypes.length);
//			IType iType = allTypes[n];
//			return iType;
//		}
//		return null;
//	}
