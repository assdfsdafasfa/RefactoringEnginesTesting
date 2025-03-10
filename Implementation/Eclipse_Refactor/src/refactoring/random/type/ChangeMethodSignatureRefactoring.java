package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseChangeSignatureProcessor;
import refactoring.random.select.Constant;
import refactoring.random.select.SelectRefactoring;

public class ChangeMethodSignatureRefactoring {
	public static void performChangeMethodSignature(IFile file, CompilationUnit cUnit)
			throws JavaModelException, InvocationTargetException, InterruptedException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;

		IType[] allTypes = iCompilationUnit.getAllTypes();
		List<IMethod> iMethodList = getIMethod(allTypes);
		try {
			selectRefactoring(iMethodList);
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void selectRefactoring(List<IMethod> iMethodList)
			throws OperationCanceledException, CoreException, InvocationTargetException, InterruptedException {
		while (iMethodList.size() > 0) {
			System.out.println("change method");
			if (iMethodList.size() == 1) {
				IMethod iMethod = iMethodList.get(0);
				String newName = GptApi.printMessage(Constant.RENAME_METRHOD_PROMPT + iMethod.toString());
//				System.out.println("newName:" + newName);
				EclipseChangeSignatureProcessor processor = new EclipseChangeSignatureProcessor(iMethod);
				Refactoring refactoring = new ProcessorBasedRefactoring(processor);
				processor.setDelegateUpdating(true);
				processor.canChangeNameAndReturnType();
				processor.setNewMethodName(newName);// modifier
//			processor.setNewReturnTypeName(iMethod.getReturnType());// modifier
				RefactoringStatus initialConditions = refactoring.checkInitialConditions(new NullProgressMonitor());
				if (initialConditions.isOK()) {
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);
					System.out.println("Change Method Signature Refactoring");
					break;
				}
			} else {
				int pos = SelectRefactoring.randomRefactoringIdentifier(iMethodList.size());
				IMethod iMethod = iMethodList.get(pos);
				String newName = GptApi.printMessage(Constant.RENAME_METRHOD_PROMPT + iMethod.toString());
//				System.out.println("newName:" + newName);
				EclipseChangeSignatureProcessor processor = new EclipseChangeSignatureProcessor(iMethod);
				Refactoring refactoring = new ProcessorBasedRefactoring(processor);
				processor.setDelegateUpdating(true);
				processor.canChangeNameAndReturnType();
				processor.setNewMethodName(newName);// modifier
//			processor.setNewReturnTypeName(iMethod.getReturnType());// modifier
				RefactoringStatus initialConditions = refactoring.checkInitialConditions(new NullProgressMonitor());
				if (initialConditions.isOK()) {
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);
					System.out.println("Change Method Signature Refactoring");
					break;
				} else {
					iMethodList.remove(pos);
					selectRefactoring(iMethodList);
				}
			}
		}
	}

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

}
