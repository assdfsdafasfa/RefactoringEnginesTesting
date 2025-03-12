package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
public class ConvertExtractMethodToRename {
	public static void convertExtractMethodToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException {
		// 遍历 AST，找到需要提取的代码块
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration node) {
				if (node.getName().getIdentifier().equals("calculate")) {
					try {
						performExtractMethod(cu, node);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return super.visit(node);
			}
		});
	}

	private static void performExtractMethod(CompilationUnit cu, MethodDeclaration methodNode) throws Exception {
		// 获取方法体
		Block methodBody = methodNode.getBody();
		List<?> statements = methodBody.statements();

		// 假设提取第 2 行代码 (int sum = a + b;)
		ASTNode targetNode = (ASTNode) statements.get(2);

		// 配置重构引擎
		ExtractMethodRefactoring refactoring = new ExtractMethodRefactoring(cu, targetNode.getStartPosition(),
				targetNode.getLength());
		refactoring.setMethodName("calculateSum");
		refactoring.setVisibility(Modifier.PRIVATE);

		NullProgressMonitor monitor = new NullProgressMonitor();
		if (refactoring.checkInitialConditions(monitor).isOK() && refactoring.checkFinalConditions(monitor).isOK()) {
//			TextEdit edits = refactoring.createChange(monitor).getEdit();
//			Document document = new Document(cu.toString());
//			edits.apply(document);
//			System.out.println("Refactored Code:\n" + document.get());
		} else {
			System.out.println("Refactoring conditions not met.");
		}
	}
}
