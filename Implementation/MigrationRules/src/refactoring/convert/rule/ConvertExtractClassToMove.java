package refactoring.convert.rule;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ConvertExtractClassToMove {
	public static void convertExtractClassToMove(ICompilationUnit sourceUnit, CompilationUnit sourceAST,
			String methodName, String targetPackageName) {
		try {
			// Parse the source compilation unit
//			CompilationUnit sourceAST = parseCompilationUnit(sourceUnit);
			// Get the package declaration node
			PackageDeclaration packageDecl = sourceAST.getPackage();
			// Prepare ASTRewrite for modification
			ASTRewrite rewrite = ASTRewrite.create(sourceAST.getAST());
			if (packageDecl == null) {
				// If no package declaration exists, create a new one
				PackageDeclaration newPackage = sourceAST.getAST().newPackageDeclaration();
				newPackage.setName(sourceAST.getAST().newName(targetPackageName));
				rewrite.set(sourceAST, CompilationUnit.PACKAGE_PROPERTY, newPackage, null);
			} else {
				// Update the existing package declaration
				rewrite.set(packageDecl, PackageDeclaration.NAME_PROPERTY,
						sourceAST.getAST().newName(targetPackageName), null);
			}
			// Apply the changes
			applyChanges(sourceUnit, sourceAST, rewrite);
			System.out.println("Class moved to package: " + targetPackageName);
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
