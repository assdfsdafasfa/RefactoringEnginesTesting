package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.internal.corext.refactoring.code.InlineMethodRefactoring;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.model.EclipseInlineMethodRefactoring;
import refactoring.random.select.ParseJavaFile;
import refactoring.random.select.PerformRefactoring;
import refactoring.random.select.SelectRefactoring;

// 获取所有method, 获取所有方法调用，只有方法调用了method，才放入列表
public class InlineMethodRefactoringPerform {
	public static void performInlineMethodRefactoring(IFile file, CompilationUnit compilationUnit)
			throws OperationCanceledException, CoreException, InvocationTargetException, InterruptedException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		if (iCompilationUnit != null) {
			IType[] allType = iCompilationUnit.getAllTypes();
			List<MethodDeclaration> iMethodList = ParseJavaFile.getMethodInvocationsName(compilationUnit);
			List<String> methodInvocations = getInvocations(compilationUnit);
			for (int a = 0; a < iMethodList.size(); a++) {
				String methodName = iMethodList.get(0).getName().toString();
				if (!methodInvocations.contains(methodName)) {
					iMethodList.remove(a);
				}
			}
			if (iMethodList.size() == 0 || methodInvocations.size() == 0) {
				System.out.println("没有method可以inline");
			} else {
				Collections.shuffle(iMethodList);
//				int num = SelectRefactoring.selectRefactoringNumber(iMethodList.size());

				selectRefactoring(iMethodList, iCompilationUnit, compilationUnit);

			}
		}
	}

	public static void selectRefactoring(List<MethodDeclaration> iMethodList, ICompilationUnit iCompilationUnit,
			CompilationUnit compilationUnit)
			throws OperationCanceledException, CoreException, InvocationTargetException, InterruptedException {
//		while (iMethodList.size() > 0) {
//			if (iMethodList.size() == 1) {
//				MethodDeclaration mDeclaration = iMethodList.get(0);
		for (int a = 0; a < iMethodList.size(); a++) {
			MethodDeclaration mDeclaration = iMethodList.get(a);
			SimpleName name = mDeclaration.getName();
			int modifiers = mDeclaration.getModifiers();
			EclipseInlineMethodRefactoring refactoring = EclipseInlineMethodRefactoring.create(iCompilationUnit,
					compilationUnit, mDeclaration, mDeclaration.getStartPosition(), mDeclaration.getLength());
			RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
//				System.out.println("status:" + status);// && status1.isOK()
			if (status.isOK()) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				System.out.println("mDeclaration：" + mDeclaration);
				RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
						RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				helper.perform(false, false);
				System.out.println("inline method refactoring");

				PerformRefactoring.no += 1;
				RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa, PerformRefactoring.no,
						name.toString(), name.toString(), PerformRefactoring.filePath, name.getStartPosition(),
						name.getLength(), PerformRefactoring.className, "inline method");
				SaveData saveData = new SaveData(record);
				break;
			}
		}
//		break;
//	}else if(iMethodList.size()>1)
//
//	{
//		int pos = SelectRefactoring.randomRefactoringIdentifier(iMethodList.size());
//		MethodDeclaration mDeclaration = iMethodList.get(pos);
//		SimpleName name = mDeclaration.getName();
//		int modifiers = mDeclaration.getModifiers();
//		EclipseInlineMethodRefactoring refactoring = EclipseInlineMethodRefactoring.create(iCompilationUnit,
//				compilationUnit, mDeclaration, mDeclaration.getStartPosition(), mDeclaration.getLength());
////				refactoring.create(iCompilationUnit, compilationUnit, mDeclaration.getStartPosition(),
////						mDeclaration.getLength());
//		RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
////				RefactoringStatus status1 = refactoring.checkFinalConditions(new NullProgressMonitor());&& status1.isOK()
////				System.out.println("status:" + status);
//		if (status.isOK()) {
////				final PerformRefactoringOperation op = new PerformRefactoringOperation(refactoring, 0);
////				op.run(new NullProgressMonitor());
//			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
////					System.out.println("status：" + status);
//			System.out.println("mDeclaration：" + mDeclaration);
//			RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//					RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
//					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//			helper.perform(false, false);
//			System.out.println("inline method refactoring");
//			break;
//		} else {
//			iMethodList.remove(pos);
//			selectRefactoring(iMethodList, iCompilationUnit, compilationUnit);
//		}
//	}
//	}

	}

	public static void getInlineMethodIdentifier(List<MethodInvocation> iMethods, ICompilationUnit iCompilationUnit,
			CompilationUnit compilationUnit) throws CoreException {
		List<Integer> excludes = new ArrayList<>();
		boolean isRefactor = false;
		int selectNumber = 0;
		MethodInvocation iMethod = null;
		while (isRefactor == false) {
			int pos = SelectRefactoring.randomInlineMethod(iMethods.size(), excludes);
			iMethod = iMethods.get(pos);
			excludes.add(pos);
			InlineMethodRefactoring refactoring = InlineMethodRefactoring.create(iCompilationUnit, compilationUnit,
					iMethod.getStartPosition(), iMethod.getLength());
			RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
			if (status.isOK()) {
				final PerformRefactoringOperation op = new PerformRefactoringOperation(refactoring, 3);
				JavaCore.run(op, new NullProgressMonitor());
				isRefactor = true;
				break;
			}
			selectNumber += 1;
			if (selectNumber >= iMethods.size()) {
				break;
			}
		}
	}

	public static List<String> getInvocations(CompilationUnit compilationUnit) {
		List<String> invocations = new ArrayList<>();
		compilationUnit.accept(new ASTVisitor() {
			public boolean visit(MethodInvocation node) {
				invocations.add(node.getName().toString());
				return true;
			}
		});
		return invocations;
	}
//	public static List<MethodInvocation> getInlineMethodRefactoring(IType[] allType, CompilationUnit cUnit)
//			throws JavaModelException {
//
//		List<MethodInvocation> mehtodIvocationList = ParseJavaFile.getMethodInvocationsName(cUnit);
//		if (mehtodIvocationList.size() < 0) {
//			return null;
//		}
//
//		List<String> iMethods = new ArrayList<>();
//		for (int i = 0; i < allType.length; i++) {
//			IType iType = allType[i];
//			for (IMethod method : iType.getMethods()) {
//				iMethods.add(method.getElementName()); // all method
//			}
//		}
//		if (iMethods.size() < 0) {
//			return null;
//		}
//		List<MethodDeclaration> existInvocations = new ArrayList<>();
//		for (MethodDeclaration mInvocation : mehtodIvocationList) {
//			if (iMethods.contains(mInvocation.getName().toString())) {
//				existInvocations.add(mInvocation);
//			}
//		}
//
//		return existInvocations;
//	}

}
