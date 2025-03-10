package refactoring.random.type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class ConvertFieldToVariableRefactoringPerform {
	public static void performFieldToVariableRefactoring(IFile file, CompilationUnit astRoot)
			throws OperationCanceledException, InvocationTargetException, CoreException, InterruptedException {
//		List<SimpleName> variableDeclarations = ParseJavaFile.getVariableDeclarationList(astRoot);
		List<FieldDeclaration> fieldDeclarations = getDeclarations(astRoot);
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		List<SimpleName> sList = getSimpleName(astRoot);
		if (fieldDeclarations.size() > 0 && sList.size() > 0) {
			selectRefactoring(fieldDeclarations, iCompilationUnit, astRoot, sList);
		} else {
			System.out.println("is null");
		}

	}

	private static void selectRefactoring(List<FieldDeclaration> fieldDeclarations, ICompilationUnit iCompilationUnit,
			CompilationUnit astRoot, List<SimpleName> sList) throws JavaModelException, IllegalArgumentException {
		// TODO Auto-generated method stub
		Collections.shuffle(fieldDeclarations);
		FieldToLocalVariableRewriter localVariableRewriter = new FieldToLocalVariableRewriter(iCompilationUnit, astRoot,
				fieldDeclarations.get(0));
		astRoot.accept(localVariableRewriter);
		localVariableRewriter.applyChanges(iCompilationUnit);

	}

	public static List<FieldDeclaration> getDeclarations(CompilationUnit cUnit) {
		List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
		cUnit.accept(new ASTVisitor() {
			public boolean visit(FieldDeclaration node) {
				fieldDeclarations.add(node);
				return true;
			}
		});
		return fieldDeclarations;
	}

	public static List<SimpleName> getSimpleName(CompilationUnit cUnit) {
		List<SimpleName> simpleNames = new ArrayList<>();
		cUnit.accept(new ASTVisitor() {
			public boolean visit(SimpleName node) {
				simpleNames.add(node);
				return true;
			}
		});
		return simpleNames;
	}
}

class FieldToLocalVariableRewriter extends ASTVisitor {
	private ASTRewrite rewrite;
	private CompilationUnit cu;
	private FieldDeclaration fdDeclaration;
	private ICompilationUnit iCompilationUnit;

	public FieldToLocalVariableRewriter(ICompilationUnit iUnit, CompilationUnit cu, FieldDeclaration fieldDeclaration) {
		this.cu = cu;
		this.rewrite = ASTRewrite.create(cu.getAST());
		this.fdDeclaration = fieldDeclaration;
		iCompilationUnit = iUnit;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		MethodDeclaration methodDeclaration = node;
		VariableDeclarationFragment variableFragment = (VariableDeclarationFragment) fdDeclaration.fragments().get(0);
//		Expression expression = variableFragment.getInitializer();
		SimpleName fieldName = variableFragment.getName();
		FieldUsageChecker checker = new FieldUsageChecker(fieldName.toString());
		methodDeclaration.accept(checker);
		if (checker.isFieldUsed()) {
			// Create a new local variable declaration

			AST ast = cu.getAST();
			VariableDeclarationFragment variable = ast.newVariableDeclarationFragment();
			variable.setName(ast.newSimpleName(fieldName.toString()));

			Expression initializerExpression = variableFragment.getInitializer();
			variableFragment.setInitializer((Expression) ASTNode.copySubtree(ast, initializerExpression));
//			variableFragment.setInitializer(expression);
			rewrite.remove(fdDeclaration, null);// remove field
			VariableDeclarationStatement localVarDeclaration = ast.newVariableDeclarationStatement(variable);
			localVarDeclaration.setType((Type) ASTNode.copySubtree(ast, fdDeclaration.getType()));
//			System.out.println("expression:" + expression);
//			SimpleName initializer = ast.newSimpleName("1");
//			Assignment assignment = ast.newAssignment();
//			assignment.setLeftHandSide(variableFragment.getName());
//			assignment.setRightHandSide(initializer);
//			ExpressionStatement expressionStatement = ast.newExpressionStatement(assignment);
			Block block = methodDeclaration.getBody();
			ListRewrite listRewrite = rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);
			listRewrite.insertFirst(localVarDeclaration, null);
			try {
				TextEdit edits = rewrite.rewriteAST();
				Document document = new Document(iCompilationUnit.getSource());
				edits.apply(document);
				iCompilationUnit.getBuffer().setContents(document.get());
				TextFileChange change = new TextFileChange("", (IFile) iCompilationUnit.getResource());
				change.setEdit(edits);
//				change.perform(null);
			} catch (JavaModelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MalformedTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.visit(node);
	}

	public void applyChanges(ICompilationUnit cu) throws JavaModelException, IllegalArgumentException {
		TextEdit edits = rewrite.rewriteAST();
		try {
			Document document = new Document(cu.getSource());
			edits.apply(document);
			cu.getBuffer().setContents(document.get());
		} catch (MalformedTreeException | BadLocationException e) {
			e.printStackTrace();
		}
	}
}

class FieldUsageChecker extends ASTVisitor {
	private String fieldName;
	private boolean fieldUsed = false;

	public FieldUsageChecker(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isFieldUsed() {
		return fieldUsed;
	}

	@Override
	public boolean visit(SimpleName node) {
		if (node.getIdentifier().equals(fieldName)) {
			// The field is used in the method body
			fieldUsed = true;
		}
		return super.visit(node);
	}
}