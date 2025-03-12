package refactoring.convert.rule;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ConvertMoveFieldToExtract {
	public static void convertMoveFieldToExtract(ICompilationUnit sourceUnit, ICompilationUnit targetUnit,
			String fieldName) {
		try {
			// Parse the source and target compilation units
			CompilationUnit sourceAST = parseCompilationUnit(sourceUnit);
			CompilationUnit targetAST = parseCompilationUnit(targetUnit);

			// Get the source and target type declarations (first top-level class)
			TypeDeclaration sourceClass = (TypeDeclaration) sourceAST.types().get(0);
			TypeDeclaration targetClass = (TypeDeclaration) targetAST.types().get(0);

			// Find the field in the source class
			FieldDeclaration fieldToMove = null;
			for (FieldDeclaration field : sourceClass.getFields()) {
				for (Object fragment : field.fragments()) {
					if (((VariableDeclarationFragment) fragment).getName().getIdentifier().equals(fieldName)) {
						fieldToMove = field;
						break;
					}
				}
				if (fieldToMove != null)
					break;
			}
			if (fieldToMove == null) {
				System.out.println("Field not found: " + fieldName);
				return;
			}

			// Prepare ASTRewrite for source and target
			ASTRewrite sourceRewrite = ASTRewrite.create(sourceAST.getAST());
			ASTRewrite targetRewrite = ASTRewrite.create(targetAST.getAST());

			// Remove the field from the source class
			sourceRewrite.remove(fieldToMove, null);

			// Add the field to the target class
			FieldDeclaration copiedField = (FieldDeclaration) ASTNode.copySubtree(targetAST.getAST(), fieldToMove);
			targetRewrite.getListRewrite(targetClass, TypeDeclaration.BODY_DECLARATIONS_PROPERTY)
					.insertLast(copiedField, null);

			// Apply changes to the source unit
			applyChanges(sourceUnit, sourceAST, sourceRewrite);

			// Apply changes to the target unit
			applyChanges(targetUnit, targetAST, targetRewrite);

			System.out.println("Field '" + fieldName + "' moved successfully.");
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
