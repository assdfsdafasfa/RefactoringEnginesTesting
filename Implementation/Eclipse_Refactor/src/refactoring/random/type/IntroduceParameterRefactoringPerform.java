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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.random.model.EclipseIntroduceParameterRefactoring;
import refactoring.random.select.SelectRefactoring;

public class IntroduceParameterRefactoringPerform {
	public static void performIntroduceParameter(IFile file, CompilationUnit astRoot)
			throws CoreException, InvocationTargetException, InterruptedException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		List<Expression> expressions = getExpressions(astRoot);
		for (Expression exp : expressions) {
			System.out.println("exp:" + exp);
		}
		selectRefactoring(iCompilationUnit, expressions);// parameter 应该选择什么，位置信息，选什么，需要修改
	}

	public static void selectRefactoring(ICompilationUnit cu, List<Expression> variables)
			throws CoreException, InvocationTargetException, InterruptedException {
		while (variables.size() > 0) {
			if (variables.size() == 1) {
				Expression variable = variables.get(0);
				int start = variable.getStartPosition();
				int length = variable.getLength();
				EclipseIntroduceParameterRefactoring refactoring = new EclipseIntroduceParameterRefactoring(cu, start,
						length);
				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
				if (status.isOK()) {
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					System.out.println("status：" + status);
					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);
				}
				break;
			} else {
				int n = SelectRefactoring.randomRefactoringIdentifier(variables.size());
				Expression variable = variables.get(n);
				int start = variable.getStartPosition();
				int length = variable.getLength();
				EclipseIntroduceParameterRefactoring refactoring = new EclipseIntroduceParameterRefactoring(cu, start,
						length);
				RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
				if (status.isOK()) {
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					System.out.println("status：" + status);
					RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
							RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING,
							shell, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					helper.perform(false, false);
					break;
				} else {
					variables.remove(n);
					selectRefactoring(cu, variables);
				}
			}
		}
	}

	public static List<Expression> getExpressions(ASTNode cuu) {
		List<Expression> expressions = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(BooleanLiteral node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(CharacterLiteral node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(NullLiteral node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(StringLiteral node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(ArrayAccess node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(ArrayInitializer node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(Assignment node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(MethodInvocation node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(FieldAccess node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(SuperFieldAccess node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(LambdaExpression node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(ParenthesizedExpression node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(ConditionalExpression node) {
				expressions.add(node);
				return true;
			}

			public boolean visit(SimpleName node) {
				expressions.add(node);
				return true;
			}

		});

		if (expressions.size() == 1 && expressions.get(0).toString().equals(cuu.toString())) {
			return null;
		}
		return expressions;
	}
}
