package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ConvertExtractVariableToRename {

	public static void convertExtractVariableToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException {

		// 目标表达式的行号或位置
		int targetLine = 3;

		// 遍历 AST，找到目标表达式
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(InfixExpression node) {
				if (isTargetExpression(node)) { // 自定义方法判断目标表达式
					performExtractVariable(cu, node);
				}
				return super.visit(node);
			}
		});
	}

	private static void performExtractVariable(CompilationUnit cu, Expression targetExpression) {
		AST ast = cu.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);

		// 创建新变量声明
		VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
		fragment.setName(ast.newSimpleName("temp"));
		fragment.setInitializer((Expression) ASTNode.copySubtree(ast, targetExpression));

		VariableDeclarationStatement newVariable = ast.newVariableDeclarationStatement(fragment);
		newVariable.setType(ast.newPrimitiveType(PrimitiveType.INT));

		// 在目标表达式前插入变量声明
		Block parentBlock = (Block) ASTNodes.getParent(targetExpression, Block.class);
		ListRewrite listRewrite = rewriter.getListRewrite(parentBlock, Block.STATEMENTS_PROPERTY);
//		listRewrite.insertBefore(newVariable, ASTNodes.getParentStatement(targetExpression), null);

		// 替换原始表达式为变量名
		rewriter.replace(targetExpression, ast.newSimpleName("temp"), null);

		// 应用修改
		applyRewrite(cu, rewriter);
	}

	private static void applyRewrite(CompilationUnit cu, ASTRewrite rewriter) {
		Document document = new Document(cu.toString());
		try {
			TextEdit edits = rewriter.rewriteAST(document, null);
			edits.apply(document);
			System.out.println("Modified Code:\n" + document.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isTargetExpression(Expression node) {
		// 判断是否为目标表达式 (例如行号或内容匹配)
		return node.toString().equals("10 + 20");
	}
}
