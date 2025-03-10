package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.random.model.EclipsePromoteTempToFieldRefactoring;
import refactoring.random.select.ParseJavaFile;

public class ConvertVariableToFieldRefactoring {

	public static void performRefactoring(IFile file, CompilationUnit astRoot)
			throws OperationCanceledException, InvocationTargetException, CoreException, InterruptedException {
		List<SimpleName> variableDeclarations = ParseJavaFile.getVariableDeclarationList(astRoot);
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		if (variableDeclarations.size() > 0) {
//			for (SimpleName vd : variableDeclarations) {
//				System.out.println("vd:" + variableDeclarations);
//			}
			selectRefactoring(variableDeclarations, iCompilationUnit);
		} else {
			System.out.println("is null");
		}

	}

	public static void selectRefactoring(List<SimpleName> declarations, ICompilationUnit iCompilationUnit)
			throws OperationCanceledException, CoreException, InvocationTargetException, InterruptedException {
//		while (declarations.size() > 0) {
//			if (declarations.size() == 1) {
		Collections.shuffle(declarations);
		for (int a = 0; a < declarations.size(); a++) {
			SimpleName name = declarations.get(a);
			int start = name.getStartPosition();
			int length = name.getLength();
			EclipsePromoteTempToFieldRefactoring refactoring = new EclipsePromoteTempToFieldRefactoring(
					iCompilationUnit, start, length);
			refactoring.setDeclareStatic(true);
			RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
			if (status.isOK()) {
				System.out.println("1111");
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
						RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				helper.perform(false, false);
				break;
			}
		}
//			} else {
//				int n = SelectRefactoring.randomRefactoringIdentifier(declarations.size());
//				SimpleName name = declarations.get(n);
//				int start = name.getStartPosition();
//				int length = name.getLength();
//				EclipsePromoteTempToFieldRefactoring refactoring = new EclipsePromoteTempToFieldRefactoring(
//						iCompilationUnit, start, length);
//				refactoring.setDeclareStatic(true);
//				RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
//				if (status.isOK()) {
//					System.out.println("1111");
//					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//
//					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
//							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//					helper.perform(false, false);
//					break;
//				} else {
//					declarations.remove(n);
//					selectRefactoring(declarations, iCompilationUnit);
//				}
//			}
//		}

	}
}
