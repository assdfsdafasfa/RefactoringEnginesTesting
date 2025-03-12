package refactoring.file.parse;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ChangeAST {
	public static void addMethodToTargetClass(CompilationUnit targetAst, MethodDeclaration method) {
		AST ast = targetAst.getAST();
		MethodDeclaration newMethod = (MethodDeclaration) ASTNode.copySubtree(ast, method);
		// 获取目标类，假设目标类只有一个类型声明
		TypeDeclaration targetClass = (TypeDeclaration) targetAst.types().get(0);
		targetClass.bodyDeclarations().add(newMethod);
	}

	// 从源类中删除方法
	public static void deleteMethodFromSourceClass(CompilationUnit sourceAst, MethodDeclaration method) {
		TypeDeclaration sourceClass = (TypeDeclaration) sourceAst.types().get(0); // 假设源类只有一个类型声明
		List bodyDeclarations = sourceClass.bodyDeclarations();
		// 删除方法
		bodyDeclarations.remove(method);
	}

	// 为方法添加参数
	public static void addParameterToMethod(CompilationUnit ast, String methodName, String paramName,
			String paramType) {
		ast.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration method) {
				// 检查方法名是否匹配
				if (method.getName().getIdentifier().equals(methodName)) {
					// 创建新的参数
					AST methodAst = method.getAST();
					SingleVariableDeclaration newParam = methodAst.newSingleVariableDeclaration();
					newParam.setName(methodAst.newSimpleName(paramName));
					newParam.setType(methodAst.newSimpleType(methodAst.newSimpleName(paramType)));

					// 获取原有参数列表并添加新参数
					List<SingleVariableDeclaration> parameters = method.parameters();
					parameters.add(newParam);

					// 将新的参数列表设置回方法中
					method.parameters().clear();
					method.parameters().addAll(parameters);
				}
				return super.visit(method);
			}
		});
	}
}
