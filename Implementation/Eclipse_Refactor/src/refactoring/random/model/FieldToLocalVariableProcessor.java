package refactoring.random.model;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class FieldToLocalVariableProcessor {

//	private void processCompilationUnit(ICompilationUnit compilationUnit) throws JavaModelException {
//		// Parse the compilation unit
//		ASTParser parser = ASTParser.newParser(AST.JLS18);
//		parser.setSource(compilationUnit);
//		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setResolveBindings(true);
//
//		ASTNode astRoot = parser.createAST(null);
//		if (astRoot instanceof org.eclipse.jdt.core.dom.CompilationUnit) {
//			CompilationUnit cu = (CompilationUnit) astRoot;
//			cu.accept(node -> {
//				if (node instanceof TypeDeclaration) {
//					TypeDeclaration typeDeclaration = (TypeDeclaration) node;
//					processTypeDeclaration(typeDeclaration);
//				}
//				return true;
//			});
//		}
//	}
//
//	public void processTypeDeclaration(TypeDeclaration typeDeclaration) {
//		FieldDeclaration[] fieldDeclarations = typeDeclaration.getFields();
//		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
//			if (Modifier.isPrivate(fieldDeclaration.getModifiers())) {
//				convertFieldToLocalVariable(typeDeclaration, fieldDeclaration);
//			}
//		}
//	}

	public void convertFieldToLocalVariable(TypeDeclaration typeDeclaration, FieldDeclaration fieldDeclaration) {
		AST ast = fieldDeclaration.getAST();
		SimpleName fieldName = ast.newSimpleName(fieldDeclaration.fragments().get(0).toString());
		SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
		variableDeclaration.setType(ast.newSimpleType(ast.newSimpleName(fieldDeclaration.getType().toString())));
		variableDeclaration.setName(fieldName);

		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.setName(ast.newSimpleName("convertFieldToLocalVariable_" + fieldName));
		methodDeclaration.setReturnType2(null);
//				ast.newPrimitiveType(ASTNode.PRIMITIVE_TYPE_INT));
		methodDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

		typeDeclaration.bodyDeclarations().add(0, methodDeclaration);
		typeDeclaration.bodyDeclarations().remove(fieldDeclaration);
	}
}
