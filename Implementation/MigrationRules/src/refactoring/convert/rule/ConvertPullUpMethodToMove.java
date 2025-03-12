package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertPullUpMethodToMove {
	String parentClassName = "";

	public void convertPullUpMethodToMove(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName)
			throws JavaModelException, IllegalArgumentException {

		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(TypeDeclaration node) {
				if (node.getName().getIdentifier().equals(sourceClass)) {
					if (node.getSuperclassType() != null) {
						parentClassName = node.getSuperclassType().toString();
					}
					return false; // 停止遍历
				}
				return super.visit(node);
			}
		});

		for (Object type : cu.types()) {
			AST ast = cu.getAST();
			ASTRewrite rewrite = ASTRewrite.create(ast);
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				if (typeDeclaration.getName().toString().equals(sourceClass)) {
					VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
					variableDeclarationFragment.setName(ast.newSimpleName(parentClassName.toLowerCase()));

					// 创建字段类型节点
					SimpleType fieldType = ast.newSimpleType(ast.newSimpleName(parentClassName));

					// 创建字段声明节点
					FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
					fieldDeclaration.setType(fieldType);
					fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD));

					typeDeclaration.bodyDeclarations().add(fieldDeclaration);

					try {
						TextEdit edits = rewrite.rewriteAST();

						String sourceCode = cu.toString();
						// 应用修改
						Document document = new Document(sourceCode);
						edits.apply(document);
						String modifiedSourceCode = document.get();
//						System.out.println(modifiedSourceCode);
						SaveFile.saveContentToFile(
								Constant.CONVERT_PROGRAM_SAVE_PATH + "pullUpToMove/" + "Bug" + sourceClass + ".java",
								modifiedSourceCode);

					} catch (BadLocationException e) {
						e.printStackTrace();
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
