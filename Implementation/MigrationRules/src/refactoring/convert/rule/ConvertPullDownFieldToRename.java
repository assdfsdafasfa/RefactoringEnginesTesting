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
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.convert.test.ConvertRuleTest;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertPullDownFieldToRename {
	Type fieldType = null;
	String parentClassName = "";

	public void convertPullDownFieldToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String fieldName, String targetClass)
			throws JavaModelException, IllegalArgumentException {

		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(FieldDeclaration node) {
				String name = ((VariableDeclarationFragment) node.fragments().get(0)).getName().getIdentifier();
				if (name.equals(fieldName)) {
					fieldType = node.getType();
				}
				return super.visit(node);
			}
		});

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
				if (typeDeclaration.getName().toString().equals(parentClassName) && fieldType != null) {

//					Random r = new Random();
//					String createFieldName = Constant.RANDOM_SELECT_NEW_FIELD_NAME[r
//							.nextInt(Constant.RANDOM_SELECT_NEW_FIELD_NAME.length)];
					List<String> fieldNameList = ConvertRuleTest.getAllMethodName(cu);
					String createFieldName = ConvertRuleTest.getNewMethodName(fieldNameList);
					FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(ast.newVariableDeclarationFragment());
//					fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName(fieldType.toString())));
					fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));// ------------------------需要修改------------------------------
					fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD));
					((VariableDeclarationFragment) fieldDeclaration.fragments().get(0))
							.setName(ast.newSimpleName(createFieldName));

//									methodDec.setName(ast.newSimpleName(newMethodName));
					typeDeclaration.bodyDeclarations().add(fieldDeclaration);

					TextEdit edits = rewrite.rewriteAST();

					String sourceCode = cu.toString();
					// 应用修改
					Document document = new Document(sourceCode);
					try {
						edits.apply(document);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}

					String modifiedSourceCode = document.get();
//									System.out.println(modifiedSourceCode);
					SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH + "convertPullDownFieldToRename/"
							+ "Bug" + sourceClass + ".java", modifiedSourceCode);

				}
			}
		}
	}
}
