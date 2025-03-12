package refactoring;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodCallVisitor extends ASTVisitor {
	private String methodName;
	private boolean methodCallFound;

	public MethodCallVisitor(String methodName) {
		this.methodName = methodName;
		this.methodCallFound = false;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// 检查方法调用语句是否匹配指定的方法名
		if (node.getName().getIdentifier().equals(methodName)) {
			methodCallFound = true;
		}
		return super.visit(node);
	}

	public boolean isMethodCallFound() {
		return methodCallFound;
	}
}
