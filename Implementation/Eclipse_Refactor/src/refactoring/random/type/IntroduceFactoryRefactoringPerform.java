package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.random.model.EclipseIntroduceFactoryRefactoring;
import refactoring.random.select.SelectRefactoring;

public class IntroduceFactoryRefactoringPerform {
	public static void performIntroduceFactoryRefactoring(IFile file, CompilationUnit astRoot)
			throws CoreException, InvocationTargetException, InterruptedException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		List<MethodDeclaration> expressions = getExpressions(astRoot);
		selectRefactoring(iCompilationUnit, expressions);
	}

	public static void selectRefactoring(ICompilationUnit iCompilationUnit, List<MethodDeclaration> expressions)
			throws CoreException, InvocationTargetException, InterruptedException {
		while (expressions.size() > 0) {
			if (expressions.size() > 1) {
				MethodDeclaration exp = expressions.get(0);
				int start = exp.getStartPosition();
				int length = exp.getLength();
				EclipseIntroduceFactoryRefactoring refactoring = new EclipseIntroduceFactoryRefactoring(
						iCompilationUnit, start, length);
				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
				System.out.println("status：" + status);
				if (status.isOK()) {
					System.out.println("1111");
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);
				}
				break;
			} else {
				int n = SelectRefactoring.randomRefactoringIdentifier(expressions.size());
				MethodDeclaration exp = expressions.get(n);
				int start = exp.getStartPosition();
				int length = exp.getLength();
				EclipseIntroduceFactoryRefactoring refactoring = new EclipseIntroduceFactoryRefactoring(
						iCompilationUnit, start, length);
				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
				System.out.println("status：" + status);
				if (status.isOK()) {
					System.out.println("1111");
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);
					break;
				} else {
					expressions.remove(n);
					selectRefactoring(iCompilationUnit, expressions);
				}
			}
		}
	}

	private static List<MethodDeclaration> getExpressions(CompilationUnit astRoot) {
		List<Integer> objects = new ArrayList<>();
		// TODO Auto-generated method stub
		List<MethodDeclaration> expressions = new ArrayList<>();
		astRoot.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				if (node.isConstructor()) {
					System.out.println("md:" + node);
					expressions.add(node);
				}
				return true;
			}
		});
		return expressions;
	}
}
