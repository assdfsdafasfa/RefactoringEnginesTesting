package refactoring;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor {
	private String methodName;
	private boolean methodCallFound;
	private String declaringClassName;

	public MethodInvocationVisitor(String methodName) {
		this.methodName = methodName;
		this.methodCallFound = false;
		this.declaringClassName = null;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// 检查方法声明语句是否匹配指定的方法名
		if (node.getName().getIdentifier().equals(methodName)) {
			declaringClassName = node.resolveBinding().getDeclaringClass().getQualifiedName();
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// 检查方法调用语句是否匹配指定的方法名，并判断是否在同一个类中
		if (node.getName().getIdentifier().equals(methodName)) {
			String callingClassName = node.resolveMethodBinding().getDeclaringClass().getQualifiedName();
			methodCallFound = callingClassName.equals(declaringClassName);
		}
		return super.visit(node);
	}

	public boolean isMethodCallInSameClass() {
		return methodCallFound;
	}
}
