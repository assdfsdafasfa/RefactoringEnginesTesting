package refactoring.random.type;

import java.beans.Expression;
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
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseExtractMethodRefactoring;
import refactoring.random.select.Constant;
import refactoring.random.select.ParseJavaFile;
import refactoring.random.select.PerformRefactoring;
import refactoring.random.select.SelectRefactoring;

public class ExtractMethodRefactoringPerform {
	static String code = "";

	public static void performExtractMethodRefactoring(IFile file, CompilationUnit compilationUnit)
			throws JavaModelException, InvocationTargetException, InterruptedException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		List<MethodDeclaration> methodDeclarations = selectExtractMethodRange(compilationUnit);
		if (methodDeclarations.size() > 0) {
			Collections.shuffle(methodDeclarations);
//			int num = SelectRefactoring.selectRefactoringNumber(methodDeclarations.size());
			inputMethod(methodDeclarations, compilationUnit);
		}
	}

	public static void inputMethod(List<MethodDeclaration> methodDeclarations, CompilationUnit compilationUnit)
			throws InvocationTargetException, InterruptedException {
		for (int a = 0; a < methodDeclarations.size(); a++) {
			List<Statement> statements = getBodyStatments(methodDeclarations.get(a));
			List<Statement> extractList = new ArrayList<>();
			Block methodBlock = methodDeclarations.get(a).getBody();
			if (statements != null && statements.size() == 1) {
				int i = statements.get(0).getStartPosition();
				int j = statements.get(0).getStartPosition() + statements.get(0).getLength();

				String newName = GptApi.printMessage(Constant.EXTRACT_METHOD_PROMPT + statements.get(0));
//			String newName = "newName";
				newName = newName.replace("(", "").replace(")", "").replace(";", "");
				System.out.println("newName:" + newName);
//					GptApi.printMessage(Constant.EXTRACT_METHOD_PROMPT + code);
				EclipseExtractMethodRefactoring refactoring = new EclipseExtractMethodRefactoring(compilationUnit,
						methodDeclarations.get(a), i, j, statements, methodBlock);
				refactoring.setMethodName(newName);
				try {
					RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
//				RefactoringStatus status1 = refactoring.checkFinalConditions(new NullProgressMonitor());
					System.out.println("status:" + status);
					if (status.isOK()) {
//						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//						RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//								RefactoringCore.getConditionCheckingFailedSeverity(),
//								RefactoringSaveHelper.SAVE_NOTHING, shell,
//								PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//						helper.perform(false, false);

						PerformRefactoring.no += 1;
						RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
								PerformRefactoring.no, statements.get(0).toString(), newName,
								PerformRefactoring.filePath, i, j, PerformRefactoring.className, "extract method");
						SaveData saveData = new SaveData(record);
						break;
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (statements != null && statements.size() > 1) {
				int i = statements.get(0).getStartPosition();
				int k = SelectRefactoring.randomRefactoringIdentifier(statements.size());
				int j = statements.get(k).getStartPosition() + statements.get(k).getLength();
				for (int t = 0; t <= k; t++) {
					extractList.add(statements.get(t));
				}
//			System.out.println("i:" + i + "  " + "j:" + j);
				String newName = GptApi.printMessage(Constant.EXTRACT_METHOD_PROMPT + statements.get(0));
//			GptApi.printMessage(Constant.EXTRACT_METHOD_PROMPT + code);
				newName = newName.replace("(", "").replace(")", "");
				String str = statements.get(0).toString();

				EclipseExtractMethodRefactoring refactoring = new EclipseExtractMethodRefactoring(compilationUnit,
						methodDeclarations.get(a), i, j, extractList, methodBlock);
				refactoring.setMethodName(newName);
				try {
					RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
//				RefactoringStatus status1 = refactoring.checkFinalConditions(new NullProgressMonitor());
					System.out.println("status:" + status);
					if (status.isOK()) {
//						System.out.println("22");
//						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//						RefactoringExecutionHelper helper = new RefactoringExecutionHelper(refactoring,
//								RefactoringCore.getConditionCheckingFailedSeverity(),
//								RefactoringSaveHelper.SAVE_NOTHING, shell,
//								PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//						helper.perform(false, false);

						PerformRefactoring.no += 1;
						RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
								PerformRefactoring.no, str, newName, PerformRefactoring.filePath, i, j,
								PerformRefactoring.className, "extract method");
						SaveData saveData = new SaveData(record);
						break;
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void inputRange(ICompilationUnit iCompilationUnit, int start, int length) {
		if (iCompilationUnit != null) {
			String newName = GptApi.printMessage(Constant.EXTRACT_METHOD_PROMPT + code);
			ExtractMethodRefactoring refactoring = new ExtractMethodRefactoring(iCompilationUnit, start, length);
			refactoring.setMethodName(newName);
			try {
				if (refactoring.checkAllConditions(new NullProgressMonitor()).isOK()) {
					final PerformRefactoringOperation op = new PerformRefactoringOperation(refactoring, 0);
					JavaCore.run(op, new NullProgressMonitor());
					System.out.println("extract method refactoring");
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static List<Statement> getBodyStatments(MethodDeclaration randomMethod) {
		if (randomMethod != null && randomMethod.getBody() != null) {
			List<Statement> statements = randomMethod.getBody().statements();
			return statements;
		}
		return null;
	}

	public static List<MethodDeclaration> selectExtractMethodRange(CompilationUnit cUnit) throws JavaModelException {
		List<MethodDeclaration> md = ParseJavaFile.getMethodDeclaration(cUnit);
		return md;
	}

	public static List<Expression> getMethodDeclaration(ASTNode cuu) {
		List<Expression> expression = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(Expression node) {
				System.out.println("Expression:" + node);
				expression.add(node);
				return true;
			}

//			public boolean visit(BooleanLiteral node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(CharacterLiteral node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(ArrayCreation node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(ArrayInitializer node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(LambdaExpression node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(MethodInvocation node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(SwitchExpression node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(StringLiteral node) {
//				expression.add(node);
//				return true;
//			}
//
//			public boolean visit(VariableDeclarationExpression node) {
//				expression.add(node);
//				return true;
//			}
		});
		return expression;
	}
}
