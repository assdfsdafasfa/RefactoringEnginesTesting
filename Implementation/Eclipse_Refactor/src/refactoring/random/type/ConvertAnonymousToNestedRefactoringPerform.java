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
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseConvertAnonymousToNestedRefactoring;
import refactoring.random.select.Constant;
import refactoring.random.select.SelectRefactoring;

public class ConvertAnonymousToNestedRefactoringPerform {
	public static void performConvertAnonymousToNestedRefactoring(IFile file, CompilationUnit cUnit)
			throws InvocationTargetException, InterruptedException, CoreException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		AnonymousClassFinder finder = new AnonymousClassFinder();
		cUnit.accept(finder);// get node type list
		List<AnonymousClassDeclaration> anonymousClassList = finder.getAnonymousClassDeclaration();
		if (anonymousClassList.size() > 0) {

			System.out.println("anonymous:" + anonymousClassList.get(0));
		} else {
			System.out.println("---null---");
		}
		selecetRefactoring(anonymousClassList);
	}

	public static void selecetRefactoring(List<AnonymousClassDeclaration> anonymousClassList)
			throws InvocationTargetException, InterruptedException, CoreException {
		while (anonymousClassList.size() > 0) {
			if (anonymousClassList.size() == 1) {
				EclipseConvertAnonymousToNestedRefactoring refactoring = new EclipseConvertAnonymousToNestedRefactoring(
						anonymousClassList.get(0));
				String newName = GptApi.printMessage(Constant.RENAME_TYPT_PROMPT);
				refactoring.setClassName(newName);

				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
				System.out.println("status:" + status);
				if (status.isOK()) {
					System.out.println("222");
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);
				}

				break;
			} else {
//				Collections.shuffle(anonymousClassList);
//				for (int i = 0; i < anonymousClassList.size(); i++) {
				int pos = SelectRefactoring.randomRefactoringIdentifier(anonymousClassList.size());
				EclipseConvertAnonymousToNestedRefactoring refactoring = new EclipseConvertAnonymousToNestedRefactoring(
						anonymousClassList.get(pos));
				String newName = GptApi.printMessage(Constant.RENAME_TYPT_PROMPT);
				refactoring.setClassName(newName);
				try {
					RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
					if (status.isOK()) {
						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

						RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
								RefactoringCore.getConditionCheckingFailedSeverity(),
								RefactoringSaveHelper.SAVE_NOTHING, shell,
								PlatformUI.getWorkbench().getActiveWorkbenchWindow());
						helper.perform(false, false);
						break;
					} else {
						anonymousClassList.remove(pos);
						selecetRefactoring(anonymousClassList);
					}
				} catch (CoreException | InvocationTargetException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			}
		}

	}

}

class AnonymousClassFinder extends ASTVisitor {
//	List<Integer> anonymousStartPositionList = new ArrayList<>();
	List<AnonymousClassDeclaration> anonymousClassList = new ArrayList<>();

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// 判断是否为匿名类
//		if (node.getAnonymousClassDeclaration() != null) {
//			anonymousStartPositionList.add(node.getStartPosition());
//			anonymousLengthList.add(node.getLength());
//		}
		anonymousClassList.add(node);
		return super.visit(node);
	}

	public List<AnonymousClassDeclaration> getAnonymousClassDeclaration() {
		return anonymousClassList;
	}

//	public List<Integer> getStartPosition() {
//		return anonymousStartPositionList;
//	}
//
//	public List<Integer> getLength() {
//		return anonymousLengthList;
//	}
}