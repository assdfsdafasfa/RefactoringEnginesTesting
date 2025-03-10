package refactoringPatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import refactoring.file.parse.GetProject;
import refactoring.random.select.Constant;
import refactoring.random.select.ParseJavaFile;
import refactoring.visitor.EclipseExtractTempRefactoring;

public class RandomSelectExpression {
	static boolean isRefactoring = false;

	public static void handlerExtractVariable(ExecutionEvent event) throws CoreException, OperationCanceledException,
			InvocationTargetException, InterruptedException, IOException {
		String projectName = "MC322";

		IJavaProject javaProject = GetProject.getJavaProject(projectName);
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IPath path = Path.fromOSString("file path");
//		IFile file = workspace.getRoot().getFileForLocation(path);
		System.out.println(projectName);
		List<IFile> allIJavaFiles = ParseJavaFile.getAllJavaFiles(javaProject);
		selectExpression(allIJavaFiles);
		System.out.println("2222");
	}

	public static void selectExpression(List<IFile> allJavaFiles) throws OperationCanceledException,
			InvocationTargetException, CoreException, InterruptedException, IOException {
		for (int i = 0; i < allJavaFiles.size(); i++) {
			IFile javaFile = allJavaFiles.get(i);
			// convert code format
//			String javaCode = readJavaCodeFromFile(javaFile);
//			String formattedCode = formatJavaCode(javaCode);
//			writeFormattedCodeToFile(javaFile, formattedCode);

			IJavaElement element = JavaCore.create(javaFile);
			ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
			ASTParser parser = ASTParser.newParser(AST.JLS18);
			parser.setSource(iCompilationUnit);
			CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);

			List<Expression> expressionList = getAllExpressions(astRoot);
			if (expressionList.size() == 1) {
				Random random = new Random();
				int num = random.nextInt(0);
				Expression exp = expressionList.get(0);

				int offset = exp.getStartPosition();
				int length = exp.getLength();
				EclipseExtractTempRefactoring ref = isExtractedEclipseApproach(astRoot, offset, length);
//				if (isRefactoring) {
				preformExtractVariable(ref); // 执行重构
				saveCode(Constant.CODE_SAVE_PATH_REFACTORING_AFTER + "extractVariable/" + "i" + ".txt",
						astRoot.toString());
			} else if (expressionList.size() > 1) {
				Random random = new Random();
				int num = random.nextInt(expressionList.size() - 1);
				Expression exp = expressionList.get(0);
				System.out.println("exp:" + exp);
				int offset = exp.getStartPosition();
				int length = exp.getLength();
				EclipseExtractTempRefactoring ref = isExtractedEclipseApproach(astRoot, offset, length);
//				if (isRefactoring) {
				preformExtractVariable(ref); // 执行重构
				saveCode(Constant.CODE_SAVE_PATH_REFACTORING_AFTER + "extractVariable/" + "i" + ".txt",
						astRoot.toString());
			}
//			for (Expression e : expressionList) {
//				System.out.println("exp:" + e);
//			}
//			Expression exp = expressionList.get(0);
//
//			int offset = exp.getStartPosition();
//			int length = exp.getLength();
//			EclipseExtractTempRefactoring ref = isExtractedEclipseApproach(astRoot, offset, length);
//			if (isRefactoring) {
//			preformExtractVariable(ref); // 执行重构
//			saveCode(Constant.CODE_SAVE_PATH + "extractVariable/" + "i" + ".txt", astRoot.toString());
//			}

		}
	}

	private static String readJavaCodeFromFile(IFile ifile) {
		try (BufferedReader reader = new BufferedReader(new FileReader(ifile.getLocation().toFile()))) {
			StringBuilder codeBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				codeBuilder.append(line).append("\n");
			}
			return codeBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

//	private static String formatJavaCode(String javaCode) {
//		CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
//		TextEdit textEdit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, javaCode, 0, javaCode.length(), 0,
//				null);
//		IDocument document = new Document(javaCode);
//		try {
//			if (textEdit != null) {
//				textEdit.apply(document);
//			}
//		} catch (MalformedTreeException | BadLocationException e) {
//			e.printStackTrace();
//		}
//
//		return document.get();
//	}

	private static void writeFormattedCodeToFile(IFile ifile, String formattedCode) {
		try (FileWriter writer = new FileWriter(ifile.getLocation().toFile())) {
			writer.write(formattedCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<MethodDeclaration> getAllMethods(CompilationUnit astRoot) {
		MethodVisitor methodVisitor = new MethodVisitor();
		astRoot.accept(methodVisitor);
		return methodVisitor.getMethods();
	}

	public static List<Expression> getAllExpressions(CompilationUnit astRoot) throws JavaModelException {

		ExpressionVisitor expressionVisitor = new ExpressionVisitor();
		astRoot.accept(expressionVisitor);
		return expressionVisitor.getExpression();
	}

	public static EclipseExtractTempRefactoring isExtractedEclipseApproach(CompilationUnit compilationUnit, int offset,
			int length)
			throws OperationCanceledException, CoreException, InvocationTargetException, InterruptedException {

		EclipseExtractTempRefactoring extractTempRefactoring = new EclipseExtractTempRefactoring(compilationUnit,
				offset, length);
//		RefactoringStatus status = extractTempRefactoring.checkAllConditions(new NullProgressMonitor());
//		if (status.isOK()) {
//			isRefactoring = true;
//		}

		return extractTempRefactoring;
	}

	public static void preformExtractVariable(EclipseExtractTempRefactoring extractTempRefactoring)
			throws InvocationTargetException, InterruptedException {

		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

			RefactoringExecutionHelper helper = new RefactoringExecutionHelper(extractTempRefactoring,
					RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			helper.perform(true, true);
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
}

class ExpressionVisitor extends ASTVisitor {

	private List<Expression> expressions = new ArrayList<>();

	public List<Expression> getExpression() {
		return expressions;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// 遍历方法中的所有语句
		if (node != null && node.getBody() != null && !node.getBody().statements().isEmpty()) {
			for (Object obj : node.getBody().statements()) {
				if (obj instanceof Statement) {
					Statement statement = (Statement) obj;
					statement.accept(new ASTVisitor() {
						@Override
						public boolean visit(ExpressionStatement node) {
							// 收集当前节点的表达式
							expressions.add(node.getExpression());
							// 递归调用相同的方法，以处理嵌套的表达式
							node.getExpression().accept(this);

							return true;
						}

					});
				}
			}
		}
		return true;
	}

}

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
