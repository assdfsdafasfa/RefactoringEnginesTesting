package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import refactoring.file.parse.FindIdentifier;
import refactoring.file.parse.SaveFile;
import utils.Constant;

public class ConvertRenameMethodToPullDown {
	// 过度转换，先将rename 转化为 move , 再将move 转化pull down
	public void convertRenameMethodToPullDown(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String fileName, String oldMethodName, String newMethodName)
			throws JavaModelException, IllegalArgumentException {
		if (!isSubclass(cu, fileName)) {
			MethodDeclaration methodDec = FindIdentifier.findMethodDeclaration(cu, oldMethodName);
//			System.out.println("methodDec:" + methodDec);

			AST ast = cu.getAST();
			ASTRewrite rewrite = ASTRewrite.create(ast);

			TypeDeclaration newClass = ast.newTypeDeclaration();
			newClass.setName(ast.newSimpleName("NewClassTest1"));

			MethodDeclaration newMethod = ast.newMethodDeclaration();
			System.out.print("newMethodName:" + newMethodName);

			SimpleName name = ast.newSimpleName(newMethodName);
			newMethod.setName(name);
			newMethod.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
			newMethod.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

			Block methodBody = ast.newBlock();
			newMethod.setBody(methodBody);
//							methodDec.setName(ast.newSimpleName(newMethodName));
			newClass.bodyDeclarations().add(newMethod);

			cu.types().add(newClass);

			// Rename method ->move 新创建的特定类 NewClassTest1
			for (Object type : cu.types()) {
				if (type instanceof TypeDeclaration) {
					TypeDeclaration typeDeclaration = (TypeDeclaration) type;
					if (typeDeclaration.getName().toString().equals(fileName)) {
						SimpleName superclassTypeName = cu.getAST().newSimpleName("NewClassTest1");
						SimpleType superclassType = cu.getAST().newSimpleType(superclassTypeName);
						typeDeclaration.setSuperclassType(superclassType);
						// 生成更改后的代码
						String modifiedCode = cu.toString();

						// 输出更改后的代码
						System.out.println(modifiedCode);
						TextEdit edits = rewrite.rewriteAST();

						String sourceCode = cu.toString();
						// 应用修改`
						Document document = new Document(sourceCode);
						try {
							edits.apply(document);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}

						String modifiedSourceCode = document.get();
//										System.out.println(modifiedSourceCode);
						SaveFile.saveContentToFile(Constant.CONVERT_PROGRAM_SAVE_PATH + "ConvertRenameMethodToPullDown/"
								+ "Bug" + fileName + ".java", modifiedSourceCode);
					}
				}
			}
		} else {
			System.out.print("the class is subclass, skip execution");
		}
	}

	public static boolean isSubclass(CompilationUnit cu, String parentClassName) {
		// 获取 CompilationUnit 中的所有类型声明
		for (Object type : cu.types()) {
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				// 获取当前类的父类类型
				Type superclassType = typeDeclaration.getSuperclassType();
				// 判断父类类型是否与目标类相匹配
				if (superclassType != null && superclassType.toString().equals(parentClassName)) {
					return true;
				}
			}
		}

		return false;
	}
}
