package refactoring.file.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import refactoring.MethodCallVisitor;
import refactoring.MethodInvocationVisitor;

public class FindIdentifier {
//	public static List<String> getMethodArguments(String javaFile, String methodName) {
//		CompilationUnit ast = CompilationFile.getCompilationUnit(javaFile);
//		MethodInvocationVisitor visitor = new MethodInvocationVisitor();
//		ast.accept(visitor);
//
//		for (MethodInvocation methodInvocation : visitor.getMethodInvocations()) {
//			if (methodName.equals(methodInvocation.getName().getIdentifier())) {
//				List<String> arguments = new ArrayList<>();
//				List<?> args = methodInvocation.arguments();
//				for (int i = 0; i < args.size(); i++) {
//					if (args.get(i) instanceof StringLiteral) {
//						StringLiteral literal = (StringLiteral) args.get(i);
//						arguments.add(literal.getLiteralValue());
//					}
//				}
//				return arguments;
//			}
//		}
//		return new ArrayList<>();
//	}

	public static List<String> getMethodArguments(CompilationUnit cu, String methodName) {
		List<String> arguments = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				if (node.getName().getIdentifier().equals(methodName)) {
					node.accept(new ASTVisitor() {
						@Override
						public boolean visit(MethodInvocation methodInvocation) {
							if (methodInvocation.getName().getIdentifier().equals("helper2_0")) {
								for (Expression argument : (List<Expression>) methodInvocation.arguments()) {
									arguments.add(argument.toString());
								}
							}
							return super.visit(methodInvocation);
						}
					});
				}
				return super.visit(node);
			}
		});
		return arguments;
	}

	public static String getClassFromFilePath(IFile javaFile) {// 方法名和类名一样
		File file = new File(javaFile.toString());
		String fileName = file.getName();
		String className = fileName.substring(0, fileName.lastIndexOf('.'));
		return className;
	}

	public static MethodDeclaration findMethodDeclaration(CompilationUnit cu, String methodName) {
		// 遍历类型声明
		for (Object typeObject : cu.types()) {
			if (typeObject instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) typeObject;
				// 遍历方法声明
				for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
					if (methodDeclaration.getName().getIdentifier().equals(methodName)) {
						return methodDeclaration;
					}
				}
			}
		}
		return null;
	}

	public static FieldDeclaration findFieldDeclaration(CompilationUnit cu, String oldfieldName) {
		// 遍历类型声明
		for (Object typeObject : cu.types()) {
			if (typeObject instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) typeObject;
				// 遍历方法声明
				for (FieldDeclaration fieldDeclaration : typeDeclaration.getFields()) {
					String fieldName = ((SimpleName) ((VariableDeclarationFragment) fieldDeclaration.fragments().get(0))
							.getName()).getIdentifier();
					if (fieldName.equals(oldfieldName)) {
						return fieldDeclaration;
					}
				}
			}
		}
		return null;
	}

	public static TypeDeclaration findClassDeclaration(CompilationUnit cu, String className) {
		// 遍历类型声明
		for (Object typeObject : cu.types()) {
			if (typeObject instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) typeObject;
				if (typeDeclaration.getName().getIdentifier().equals(className)) {
					return typeDeclaration;
				}
			}
		}
		return null;
	}

	public static boolean containsMethodCall(CompilationUnit cu, String methodName) {
		// 创建一个 Visitor
		MethodCallVisitor visitor = new MethodCallVisitor(methodName);

		// 遍历 CompilationUnit 并检查每个语句
		cu.accept(visitor);

		// 判断是否包含方法调用语句
		return visitor.isMethodCallFound();
	}

	public static boolean isMethodCallInSameClass(CompilationUnit cu, String methodName) {
		// 创建一个 Visitor
		MethodInvocationVisitor visitor = new MethodInvocationVisitor(methodName);
		// 遍历 CompilationUnit 并检查每个语句
		cu.accept(visitor);
		// 判断方法调用是否在同一个类中
		return visitor.isMethodCallInSameClass();
	}
}
