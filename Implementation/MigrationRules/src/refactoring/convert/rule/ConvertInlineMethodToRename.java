package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class ConvertInlineMethodToRename {
	public static void convertInlineMethodToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException, MalformedTreeException, BadLocationException {
        ASTRewrite rewriter = ASTRewrite.create(cu.getAST());

        // Traverse the AST to find the method
        cu.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration node) {
                if (node.getName().getIdentifier().equals("oldMethod")) {
                    // Change the method name
                    SimpleName newName = node.getAST().newSimpleName("newMethod");
                    rewriter.set(node, MethodDeclaration.NAME_PROPERTY, newName, null);
                }
                return super.visit(node);
            }
        });

		String sourceCode = null;
		// Apply the changes
        Document document = new Document(sourceCode);
        TextEdit edits = rewriter.rewriteAST(document, null);
        edits.apply(document);

        // Output the modified source code
//        System.out.println("Modified Code:\n" + document.get());
		// Locate the target method
		MethodDeclaration[] targetMethod = { null };
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration node) {
				if (node.getName().getIdentifier().equals("helper")) {
					targetMethod[0] = node;
				}
				return super.visit(node);
			}
		});

		if (targetMethod[0] == null) {
			throw new IllegalStateException("Target method 'helper' not found.");
		}

		// Create Inline Method Refactoring
//		InlineMethodRefactoring refactoring = new InlineMethodRefactoring(cu.getJavaElement(),
//				targetMethod[0].getStartPosition(), true, true);
		// Apply refactoring
//		Document document1 = new Document(sourceCode);
//		TextEdit edits1 = refactoring.createChange(null).getEdit();
//		edits.apply(document1);
//		// Output the modified source codes
//		System.out.println("Modified Code:\n" + document1.get());
	}
    }
