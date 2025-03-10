package refactoring.random.type;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import refactoring.random.select.SelectRefactoring;

public class FieldToLocalVariableRefactoringPerform {
	public static void performRefactoring(CompilationUnit astRoot) {
		TypeDeclaration typeDeclaration = (TypeDeclaration) astRoot.types().get(0);
		FieldDeclaration[] fieldDeclarations = typeDeclaration.getFields();
		if (fieldDeclarations.length != 0) {
			int pos = SelectRefactoring.randomRefactoringIdentifier(fieldDeclarations.length);
			FieldDeclaration fDeclaration = fieldDeclarations[pos];
			convertFieldToLocalVariable(astRoot, typeDeclaration, fDeclaration);
		} else {
			System.out.println("no found fields");
		}
	}

	public static void convertFieldToLocalVariable(CompilationUnit astRoot, TypeDeclaration typeDeclaration,
			FieldDeclaration fieldDeclaration) {
		AST ast = fieldDeclaration.getAST();
		SimpleName fieldName = ast.newSimpleName(fieldDeclaration.fragments().get(0).toString());
		SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
		variableDeclaration.setType(ast.newSimpleType(ast.newSimpleName(fieldDeclaration.getType().toString())));
		variableDeclaration.setName(fieldName);
		// 将variable添加到body中

		List<MethodDeclaration> methodDeclarations = getMethodDeclaration(astRoot);
		for (MethodDeclaration md : methodDeclarations) {
			Block block = md.getBody();
			block.statements().add(0, variableDeclaration);
		}

//		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
//		methodDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
//		typeDeclaration.bodyDeclarations().add(methodDeclaration);
		typeDeclaration.bodyDeclarations().remove(fieldDeclaration);
	}

	public static List<MethodDeclaration> getMethodDeclaration(CompilationUnit unit) {
		List<MethodDeclaration> methods = new ArrayList<>();
		unit.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				methods.add(node);
				return true;
			}
		});
		return methods;
	}

	public static void getSimpleName(CompilationUnit unit) {
		List<MethodDeclaration> methods = new ArrayList<>();
		unit.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				methods.add(node);
				return true;
			}
		});
	}
}
