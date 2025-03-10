package refactoring.random.type;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.gpt.GptApi;
import refactoring.random.select.Constant;
import refactoring.random.select.ParseJavaFile;
import refactoring.random.select.PerformRefactoring;
import refactoring.visitor.EclipseExtractTempRefactoring;

public class ExtractVariableRefactoring {
	static Set<Expression> finalList = new HashSet();

	public static void performExtractVariableRefactoring(IFile file)
			throws OperationCanceledException, InvocationTargetException, CoreException, InterruptedException {
		CompilationUnit astRoot = ParseJavaFile.getCompilationUnit(file);
		getExpressions(astRoot);
		if (finalList.size() > 0) {
			List<Expression> expList = new ArrayList<>(finalList);
			Collections.shuffle(expList);
//			int num = SelectRefactoring.selectRefactoringNumber(expList.size());

			selectRefactoring(expList, astRoot);
			finalList.clear();
		} else {
			System.out.println("没有表达式符合");
		}
	}

	public static void selectRefactoring(List<Expression> expList, CompilationUnit astRoot)
			throws OperationCanceledException, InvocationTargetException, CoreException, InterruptedException {
		for (int a = 0; a < expList.size(); a++) {
			System.out.println("extract variable");
//		for (Expression exp : expList) {
//			System.out.println("exp:" + exp);
//		}
//		if (finalList.size() == 1) {
//			Expression expression = expList.get(0);
//			System.out.println("expression:" + expression);
//			int offset = expression.getStartPosition();
//			int length = expression.getLength();
//			isExtractedEclipseApproach(astRoot, offset, length);
//
//		} else {
//			for (Expression exp : expList) {
//				System.out.println("expression:" + exp);
			Expression exp = expList.get(a);
			int offset = exp.getStartPosition();
			int length = exp.getLength();
			String newName = GptApi.printMessage(Constant.RENAME_METRHOD_PROMPT + exp.toString());
			newName = newName.replaceAll(Constant.regEx, "");
			if (newName != null) {
				System.out.println("select expression:" + exp);
				EclipseExtractTempRefactoring extractTempRefactoring = new EclipseExtractTempRefactoring(astRoot,
						offset, length);
				extractTempRefactoring.setTempName(newName);

				extractTempRefactoring.setReplaceAllOccurrences(true);
				RefactoringStatus status = extractTempRefactoring.checkAllConditions(new NullProgressMonitor());
				if (status.isOK()) {
					preformExtractVariable(extractTempRefactoring);
					System.out.println("perform extract variable");

					PerformRefactoring.no += 1;
					RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa,
							PerformRefactoring.no, null, newName, PerformRefactoring.filePath, offset, length,
							PerformRefactoring.className, "extract variable");
					SaveData saveData = new SaveData(record);
					break;
				}
			}
		}
	}

//		}
//	}

//	public static List<Expression> getAllExpressions(CompilationUnit astRoot) throws JavaModelException {
//
//		ExpressionVisitor expressionVisitor = new ExpressionVisitor();
//		astRoot.accept(expressionVisitor);
//		return expressionVisitor.getExpression();
//	}

	public static void isExtractedEclipseApproach(CompilationUnit compilationUnit, int offset, int length)
			throws OperationCanceledException, CoreException, InvocationTargetException, InterruptedException {

		EclipseExtractTempRefactoring extractTempRefactoring = new EclipseExtractTempRefactoring(compilationUnit,
				offset, length);
//		extractTempRefactoring.setTempName(newName);
		extractTempRefactoring.setReplaceAllOccurrences(false);
//		RefactoringStatus status = extractTempRefactoring.checkAllConditions(new NullProgressMonitor());
//		if (status.isOK()) {
		preformExtractVariable(extractTempRefactoring);
		System.out.println("perform extract variable");
//		}
	}

	public static void preformExtractVariable(EclipseExtractTempRefactoring extractTempRefactoring)
			throws InvocationTargetException, InterruptedException {

		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

			RefactoringExecutionHelper helper = new RefactoringExecutionHelper(extractTempRefactoring,
					RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			helper.perform(false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveCode(String filePath, String code) throws IOException {
		Path path = Paths.get(filePath);
		// 检查目录是否存在，如果不存在则创建
		if (!Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		// 创建 BufferedWriter
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		// 写入字符串
		writer.write(code);
		// 关闭 BufferedWriter
		writer.close();
	}

	public static void getExpressions(ASTNode cu) {// 获取expression
		List<Assignment> assignments = ParseJavaFile.getAssignment(cu);
		List<Expression> expressions = new ArrayList<>();
		for (Assignment asg : assignments) {
			Expression expression = asg.getRightHandSide();
			expressions.add(expression);
			System.out.println("ass:" + asg);
		}

		for (Expression exp : expressions) {
			finalList.add(exp);
			getChildExpressions(exp);
//			System.out.println("exp:" + exp);
		}
	}

	public static List<Expression> getChildExpressions(ASTNode cu) {
		List<Expression> expressions = ParseJavaFile.getExpressions(cu);
		finalList.addAll(expressions);
		while (true) {
			List<Expression> tempFinalList = new ArrayList();
			for (int i = 0; i < expressions.size(); i++) {
				List<Expression> tempList = getChildExpressions(expressions.get(i));
				if (tempList != null) {
					tempFinalList.addAll(tempList);
					finalList.addAll(tempList);
				}
			}
			if (tempFinalList.isEmpty()) {
				break;
			}
		}
		return null;
	}
}

//class ExpressionVisitor extends ASTVisitor {
//
//	private List<Expression> expressions = new ArrayList<>();
//
//	public List<Expression> getExpression() {
//		return expressions;
//	}
//
//	@Override
//	public boolean visit(MethodDeclaration node) {
//		// 遍历方法中的所有语句
//		if (node != null && node.getBody() != null && !node.getBody().statements().isEmpty()) {
//			for (Object obj : node.getBody().statements()) {
//				if (obj instanceof Statement) {
//					Statement statement = (Statement) obj;
//					statement.accept(new ASTVisitor() {
//						@Override
//						public boolean visit(ExpressionStatement node) {
//							// 收集当前节点的表达式
//							Expression expression = node.getExpression();
//
//							expressions.add(node.getExpression());
//							// 递归调用相同的方法，以处理嵌套的表达式
//							node.getExpression().accept(this);
//
//							return true;
//						}
//
//					});
//				}
//			}
//		}
//		return true;
//	}
//
//}

class MethodVisitor extends ASTVisitor {

	private List<MethodDeclaration> methods = new ArrayList<>();

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	@Override
	public boolean visit(MethodDeclaration methodDeclaration) {
		methods.add(methodDeclaration);
		return true;
	}

}
