package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

public class ConvertInlineFieldToRename {
	public static void convertInlineFieldToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException, MalformedTreeException, BadLocationException {

		// Locate the target field
		VariableDeclarationFragment[] targetField = { null };
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(VariableDeclarationFragment node) {
				if (node.getName().getIdentifier().equals("VALUE")) {
					targetField[0] = node;
				}
				return super.visit(node);
			}
		});

		if (targetField[0] == null) {
			throw new IllegalStateException("Target field 'VALUE' not found.");
		}

		// Create Inline Field Refactoring
//        InlineFieldRefactoring refactoring = new InlineFieldRefactoring(
//            cu.getJavaElement(), targetField[0].getStartPosition(), true
//        );
//        // Apply refactoring
//        Document document = new Document(sourceCode);
//        TextEdit edits = refactoring.createChange(null).getEdit();
//        edits.apply(document);

		// Output the modified source code
//		System.out.println("Modified Code:\n" + document.get());
	}
}
