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

public class ConvertInlineVariableToRename {
	public static void convertInlineVariableToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException, MalformedTreeException, BadLocationException {
		// Locate the target variable
		VariableDeclarationFragment[] targetVariable = { null };
		cu.accept(new ASTVisitor() {
			public boolean visit(VariableDeclarationFragment node) {
				if (node.getName().getIdentifier().equals("temp")) {
					targetVariable[0] = node;
				}
				return super.visit(node);
			}
		});

		if (targetVariable[0] == null) {
			throw new IllegalStateException("Target variable 'temp' not found.");
		}

		// Create Inline Variable Refactoring
//        InlineTempRefactoring refactoring = new InlineTempRefactoring(
//            cu.getJavaElement(), targetVariable[0].getStartPosition()
//        );
//        // Apply refactoring
//        Document document = new Document(sourceCode);
//        TextEdit edits = refactoring.createChange(null).getEdit();
//        edits.apply(document);
	}
}
