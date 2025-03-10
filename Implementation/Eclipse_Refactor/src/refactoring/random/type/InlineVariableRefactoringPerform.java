package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.model.EclipseInlineTempRefactoring;
import refactoring.random.select.ParseJavaFile;
import refactoring.random.select.PerformRefactoring;

public class InlineVariableRefactoringPerform {

	public static void performInlineVariableRefactoring(IFile file, CompilationUnit cUnit)
			throws CoreException, InvocationTargetException, InterruptedException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		List<SimpleName> vDeclarations = getVariableDeclaration(cUnit);
		if (vDeclarations.size() > 0) {
			Collections.shuffle(vDeclarations);
//			int num = SelectRefactoring.selectRefactoringNumber(vDeclarations.size());

			selectRefactoring(vDeclarations, iCompilationUnit, cUnit);
		}
	}

	public static void selectRefactoring(List<SimpleName> simpleNameList, ICompilationUnit iCompilationUnit,
			CompilationUnit cUnit) throws CoreException, InvocationTargetException, InterruptedException {
//		while (assignmentList.size() > 0) {

		System.out.println("inline variable");
//			if (assignmentList.size() == 0) {
//				System.out.println("不包含可重构inline variable");
//			}
//			if (assignmentList.size() == 1) {
//		SimpleName simpleName = assignmentList.get(0);
//				System.out.println("exp:" + expression);
		for (int a = 0; a < simpleNameList.size(); a++) {
			SimpleName simpleName = simpleNameList.get(a);
			MethodDeclaration mdDeclaration = findMethodDeclaration(simpleName);
			List<SimpleName> occureNames = getAllOccurceName(mdDeclaration, simpleName);
			SimpleName[] simpleNames = new SimpleName[occureNames.size()];
			for (int i = 0; i < occureNames.size(); i++) {
				simpleNames[i] = occureNames.get(i);
			}
			int offset = simpleName.getStartPosition();
			int length = simpleName.getLength();
			EclipseInlineTempRefactoring refactoring = new EclipseInlineTempRefactoring(iCompilationUnit, cUnit,
					simpleName.toString(), simpleNames, offset, length);
			RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
			RefactoringStatus status1 = refactoring.checkFinalConditions(new NullProgressMonitor());
//				System.out.println("status:" + status1);
//				System.out.println("SimpleName:" + expression);
			if (status.isOK() && status1.isOK()) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
						RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				helper.perform(false, false);
				System.out.println("perform inline variable refactoring");

				PerformRefactoring.no += 1;
				RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa, PerformRefactoring.no,
						simpleName.toString(), simpleName.toString(), PerformRefactoring.filePath, offset, length,
						PerformRefactoring.className, "inline variable");
				SaveData saveData = new SaveData(record);
				break;
			}
		}
//				break;
//			} else {
//				int n = SelectRefactoring.randomRefactoringIdentifier(assignmentList.size());
////				Assignment assignment = assignmentList.get(n);
////				Expression expression = assignmentList.get(0).getLeftHandSide();
//				SimpleName expression = assignmentList.get(n);
//
//				MethodDeclaration mdDeclaration = findMethodDeclaration(expression);
//				List<SimpleName> occureNames = getAllOccurceName(mdDeclaration, expression);
//				SimpleName[] simpleNames = new SimpleName[occureNames.size()];
//				for (int i = 0; i < occureNames.size(); i++) {
//					simpleNames[i] = occureNames.get(i);
//				}
//
//				int offset = expression.getStartPosition();
//				int length = expression.getLength();
//				EclipseInlineTempRefactoring refactoring = new EclipseInlineTempRefactoring(iCompilationUnit, cUnit,
//						expression.toString(), simpleNames, offset, length);
//				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
//				RefactoringStatus status1 = refactoring.checkFinalConditions(new NullProgressMonitor());
//				System.out.println("status:" + status1);
//				System.out.println("SimpleName:" + expression);
//				if (status.isOK() && status1.isOK()) {
//					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
//							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//					helper.perform(false, false);
//					System.out.println("perform inline variable refactoring");
//					break;
//				} else {
//					assignmentList.remove(n);
//					selectRefactoring(assignmentList, iCompilationUnit, cUnit);
//				}
//			}
//		}
	}

	public static List<SimpleName> getVariableDeclaration(CompilationUnit cUnit) {
		List<SimpleName> declarations = ParseJavaFile.getVariableDeclaration(cUnit);
		return declarations;
	}

	public static List<Assignment> getExpression(CompilationUnit cUnit) {
		List<Assignment> assignments = new ArrayList<>();
		List<VariableDeclarationFragment> variableDeclarationExpressions = ParseJavaFile
				.getVariableDeclarationFragment(cUnit);
		for (VariableDeclarationFragment exp : variableDeclarationExpressions) {
			List<Assignment> tempAssignments = ParseJavaFile.getAssignment(exp);
			assignments.addAll(tempAssignments);
		}
		System.out.println("11111111111");
		return assignments;
	}

	// All Occurce Name位置
	public static List<SimpleName> getAllOccurceName(MethodDeclaration cUnit, SimpleName selectName) {
		List<SimpleName> declarations = new ArrayList<>();
		cUnit.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(SimpleName node) {
				if (selectName.getStartPosition() != node.getStartPosition()
						&& node.toString().equals(selectName.toString())) {
					declarations.add(node);
				}
				return true;
			}
		});
		return declarations;
	}

	// 递归查找 SimpleName 所在的 MethodDeclaration
	private static MethodDeclaration findMethodDeclaration(SimpleName simpleName) {
		ASTNode parent = simpleName.getParent();
		while (parent != null) {
			if (parent instanceof MethodDeclaration) {
				return (MethodDeclaration) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}
}
