package refactoring.convert.rule;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ConvertRenameFieldToInline {
	public static void convertRenameFieldToInline(ICompilationUnit sourceUnit, ICompilationUnit targetUnit,
			String expression) {
		try {
			// Parse source and target units to get ASTs
			CompilationUnit sourceAST = parseCompilationUnit(sourceUnit);
			CompilationUnit targetAST = parseCompilationUnit(targetUnit);

			// Find the expression in source class
			ExpressionStatement expressionStatement = findExpressionStatement(sourceAST, expression);

			// Create the method in target class
			MethodDeclaration targetMethod = createTargetMethod(targetAST, expressionStatement);

			// Apply changes to target unit by adding the method
			applyChangesToTarget(targetUnit, targetAST, targetMethod);

			// In source class, remove the expression and call target method
			ASTRewrite rewrite = ASTRewrite.create(sourceAST.getAST());
			Block sourceMethodBlock = findSourceMethodBlock(sourceAST);
			ListRewrite listRewrite = rewrite.getListRewrite(sourceMethodBlock, Block.STATEMENTS_PROPERTY);
			listRewrite.remove(expressionStatement, null);

			// Create method invocation in source class
			MethodInvocation methodInvocation = sourceAST.getAST().newMethodInvocation();
			methodInvocation.setName(sourceAST.getAST().newSimpleName("setX"));
			methodInvocation.arguments().add(sourceAST.getAST().newNumberLiteral("5"));

			// Insert method invocation into the block
			listRewrite.insertLast(methodInvocation, null);

			// Apply changes to source unit
			applyChangesToSource(sourceUnit, sourceAST, rewrite);

			System.out.println("Expression moved successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static CompilationUnit parseCompilationUnit(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS17);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}

	private static ExpressionStatement findExpressionStatement(CompilationUnit ast, String expression) {
		for (Object type : ast.types()) {
			TypeDeclaration typeDecl = (TypeDeclaration) type;
			for (MethodDeclaration method : typeDecl.getMethods()) {
				for (Object stmt : method.getBody().statements()) {
					if (stmt instanceof ExpressionStatement) {
						ExpressionStatement exprStmt = (ExpressionStatement) stmt;
						if (exprStmt.getExpression().toString().equals(expression)) {
							return exprStmt;
						}
					}
				}
			}
		}
		return null;
	}

	private static MethodDeclaration createTargetMethod(CompilationUnit ast, ExpressionStatement expressionStatement) {
		MethodDeclaration methodDecl = ast.getAST().newMethodDeclaration();
		methodDecl.setName(ast.getAST().newSimpleName("setX"));
		methodDecl.setReturnType2(ast.getAST().newPrimitiveType(PrimitiveType.VOID));
		methodDecl.modifiers().add(ast.getAST().newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

		Block methodBody = ast.getAST().newBlock();
		methodDecl.setBody(methodBody);

		// Copy the statement to target method body
		methodBody.statements().add(expressionStatement);

		return methodDecl;
	}

	private static Block findSourceMethodBlock(CompilationUnit ast) {
		for (Object type : ast.types()) {
			TypeDeclaration typeDecl = (TypeDeclaration) type;
			for (MethodDeclaration method : typeDecl.getMethods()) {
				if (method.getName().toString().equals("methodA")) {
					return method.getBody();
				}
			}
		}
		return null;
	}

	private static void applyChangesToTarget(ICompilationUnit targetUnit, CompilationUnit targetAST,
			MethodDeclaration methodDecl) throws Exception {
		ASTRewrite rewrite = ASTRewrite.create(targetAST.getAST());
		TypeDeclaration targetType = (TypeDeclaration) targetAST.types().get(0); // assuming the first type is the
																					// target class
		ListRewrite listRewrite = rewrite.getListRewrite(targetType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertLast(methodDecl, null);

		applyChanges(targetUnit, targetAST, rewrite);
	}

	private static void applyChangesToSource(ICompilationUnit sourceUnit, CompilationUnit sourceAST, ASTRewrite rewrite)
			throws Exception {
		applyChanges(sourceUnit, sourceAST, rewrite);
	}

	private static void applyChanges(ICompilationUnit unit, CompilationUnit ast, ASTRewrite rewrite) throws Exception {
		Document document = new Document(unit.getSource());
		TextEdit edits = rewrite.rewriteAST(document, unit.getJavaProject().getOptions(true));
		edits.apply(document);
		unit.getBuffer().setContents(document.get());
		unit.save(null, true);
	}
}
