package refactoring.convert.rule;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ConvertInlineMethodToMove {
	// 方法调用的内联方法移动到其他类中
	public static void convertInlineMethodToMove(ICompilationUnit sourceUnit, CompilationUnit sourceAST,
			String methodName, String targetPackageName) {
		try {
			// Parse the source and target compilation units
//			CompilationUnit sourceAST = parseCompilationUnit(sourceUnit);
//			CompilationUnit targetAST = parseCompilationUnit(targetUnit);

			// Get the source and target type declarations (first top-level class)
			TypeDeclaration sourceClass = (TypeDeclaration) sourceAST.types().get(0);
//			TypeDeclaration targetClass = (TypeDeclaration) targetAST.types().get(0);

			// Find the method in the source class
			MethodDeclaration methodToMove = null;
			for (MethodDeclaration method : sourceClass.getMethods()) {
				if (method.getName().getIdentifier().equals(methodName)) {
					methodToMove = method;
					break;
				}
			}

			if (methodToMove == null) {
				System.out.println("Method not found: " + methodName);
				return;
			}

			// Prepare ASTRewrite for source and target
			ASTRewrite sourceRewrite = ASTRewrite.create(sourceAST.getAST());
//			ASTRewrite targetRewrite = ASTRewrite.create(targetAST.getAST());

			// Remove the method from the source class
			sourceRewrite.remove(methodToMove, null);

			// Add the method to the target class
//			MethodDeclaration copiedMethod = (MethodDeclaration) ASTNode.copySubtree(targetAST.getAST(), methodToMove);
//			targetRewrite.getListRewrite(targetClass, TypeDeclaration.BODY_DECLARATIONS_PROPERTY)
//					.insertLast(copiedMethod, null);

			// Apply changes to the source unit
			applyChanges(sourceUnit, sourceAST, sourceRewrite);

			// Apply changes to the target unit
//			applyChanges(targetUnit, targetAST, targetRewrite);

			System.out.println("Method '" + methodName + "' moved successfully.");
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

	private static void applyChanges(ICompilationUnit unit, CompilationUnit ast, ASTRewrite rewrite) throws Exception {
		Document document = new Document(unit.getSource());
		TextEdit edits = rewrite.rewriteAST(document, unit.getJavaProject().getOptions(true));
		edits.apply(document);
		unit.getBuffer().setContents(document.get());
		unit.save(null, true);
	}
}
