package refactoring.convert.rule;

import java.io.FileWriter;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ConvertMoveClassToRename {
	public static void convertMoveMethodToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException {
		AST ast = cu.getAST();
		ASTRewrite rewrite = ASTRewrite.create(ast);

		cu.recordModifications(); // 启用修改记录
		cu.accept(new ASTVisitor() {
			public boolean visit(TypeDeclaration node) {
				if (node.isPackageMemberTypeDeclaration()) {
					// 更改类名
					node.setName(cu.getAST().newSimpleName("Y"));
				}
				return super.visit(node);
			}
		});

		// 将修改后的 AST 写回文件
		Document document = new Document("sourceCode");
		TextEdit edits = cu.rewrite(document, null);
		try {
			edits.apply(document);
			// 保存到文件
			try (FileWriter writer = new FileWriter("sourcePath")) {
				writer.write(document.get());
			}
			System.out.println("Class name changed successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 获取包声明，并修改为目标包
		PackageDeclaration packageDeclaration = cu.getPackage();
		if (packageDeclaration != null) {
			SimpleName name = ast.newSimpleName("com.newpackage");
			packageDeclaration.setName(name);

		}

	}
}
