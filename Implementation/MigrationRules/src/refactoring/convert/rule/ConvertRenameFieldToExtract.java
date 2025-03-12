package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ConvertRenameFieldToExtract {
	public static void convertRenameFieldToExtract(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException {

		// 遍历 AST，找到目标变量声明
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(VariableDeclarationStatement node) {
				if (isTargetVariable(node)) { // 自定义方法判断目标变量
					performExtractField(cu, node);
				}
				return super.visit(node);
			}
		});
	}

	private static void performExtractField(CompilationUnit cu, VariableDeclarationStatement targetNode) {
		AST ast = cu.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);

		// 提取目标变量信息
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) targetNode.fragments().get(0);
		String fieldName = fragment.getName().getIdentifier();
		Expression initializer = fragment.getInitializer();

		// 创建新的字段声明
		FieldDeclaration newField = ast.newFieldDeclaration(ast.newVariableDeclarationFragment());
		((VariableDeclaration) newField.fragments().get(0)).setName(ast.newSimpleName(fieldName));
		((VariableDeclaration) newField.fragments().get(0))
				.setInitializer((Expression) ASTNode.copySubtree(ast, initializer));
		newField.setType(ast.newPrimitiveType(PrimitiveType.INT));
		newField.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD));

		// 在类的开头插入字段声明
		TypeDeclaration parentClass = (TypeDeclaration) ASTNodes.getParent(targetNode, TypeDeclaration.class);
		ListRewrite listRewrite = rewriter.getListRewrite(parentClass, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertFirst(newField, null);

		// 替换局部变量声明为字段赋值语句
		Assignment assignment = ast.newAssignment();
		assignment.setLeftHandSide(ast.newSimpleName(fieldName));
		assignment.setRightHandSide((Expression) ASTNode.copySubtree(ast, initializer));

		ExpressionStatement assignmentStatement = ast.newExpressionStatement(assignment);
		rewriter.replace(targetNode, assignmentStatement, null);

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

	private static boolean isTargetVariable(VariableDeclarationStatement node) {
		// 判断是否为目标变量 (例如变量名或行号匹配)
		return node.toString().contains("temp");
	}
}
