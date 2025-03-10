package refactoring.random.select;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class ParseJavaFile {

	public static List<IFile> getAllJavaFiles(IJavaProject javaProject) throws JavaModelException {
		final List<IFile> javaFiles = new ArrayList<>();

		IPackageFragment[] packages = javaProject.getPackageFragments();

		for (IPackageFragment aPackage : packages) {
			if (aPackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				for (ICompilationUnit compilationUnit : aPackage.getCompilationUnits()) {
					IResource resource = compilationUnit.getResource();
					if (resource instanceof IFile) {
						javaFiles.add((IFile) resource);
					}
				}
			}
		}
		return javaFiles;
	}

	public static CompilationUnit getCompilationUnit(IFile iFile) {
		IJavaElement element = JavaCore.create(iFile);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		ASTParser parser = ASTParser.newParser(AST.JLS18);
		parser.setSource(iCompilationUnit);
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		return astRoot;
	}

//MethodInvocation
	public static List<MethodDeclaration> getMethodInvocationsName(ASTNode cuu) {
		List<MethodDeclaration> methodInvocations = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				methodInvocations.add(node);
				return true;
			}
		});
		return methodInvocations;
	}

	public static List<MethodDeclaration> getMethodDeclaration(ASTNode cuu) {
		List<MethodDeclaration> methodInvocations = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				methodInvocations.add(node);
				return true;
			}
		});
		return methodInvocations;
	}

	public static List<Statement> getStatementList(ASTNode cuu) {
		List<Statement> statements = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(Block node) {
				statements.add(node);
				return true;
			}
//			public boolean visit(BreakStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(AssertStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(ContinueStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(DoStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(EmptyStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(EnhancedForStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(ExpressionStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(ForStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(IfStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(LabeledStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(ReturnStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(ExpressionStatement node) {
//				statements.add(node);
//				return true;
//			}
//			public boolean visit(ExpressionStatement node) {
//				statements.add(node);
//				return true;
//			}

		});
		return statements;
	}

	public static List<Expression> getExpressions(ASTNode cuu) {
		List<Expression> expressions = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(ExpressionStatement node) {
				expressions.add(node.getExpression());
				System.out.println("node:" + node.getExpression());
				return true;
			}
		});

		if (expressions.size() == 1 && expressions.get(0).toString().equals(cuu.toString())) {
			return null;
		}
		return expressions;
	}

	public static List<Assignment> getAssignment(ASTNode cuu) {
		List<Assignment> assignments = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(Assignment node) {
				assignments.add(node);
				return true;
			}
		});
		return assignments;
	}

	public static List<VariableDeclarationFragment> getVariableDeclarationFragment(ASTNode cu) {
		List<VariableDeclarationFragment> assignments = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(VariableDeclarationFragment node) {
				assignments.add(node);
				return true;
			}
		});
		return assignments;
	}

	public static List<SimpleName> getFieldDeclaration(ASTNode cu) {
		List<SimpleName> assignments = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(FieldDeclaration node) {

				if (Modifier.isFinal(node.getModifiers()) && Modifier.isStatic(node.getModifiers())) {
					VariableDeclarationFragment fragment = (VariableDeclarationFragment) node.fragments().get(0);
					Expression initializer = fragment.getInitializer();
					if (initializer != null) {
						SimpleName name = fragment.getName();
						assignments.add(name);
					}
				}
				return true;
			}
		});
		return assignments;
	}

	public static List<SimpleName> getVariableDeclaration(ASTNode cu) {
		List<SimpleName> sList = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(VariableDeclarationStatement node) {
				for (Object fragmentObj : node.fragments()) {
					if (fragmentObj instanceof VariableDeclarationFragment) {
						VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragmentObj;
						Expression initializer = fragment.getInitializer();
						SimpleName variableName = fragment.getName();
						if (initializer != null) {
//							System.out.println("variableName:" + variableName);
							sList.add(variableName);
						}
//						if (initializer instanceof Assignment) {
//							Assignment assignment = (Assignment) initializer;
//							Expression leftHandSide = assignment.getLeftHandSide();
//							if (leftHandSide instanceof SimpleName) {
//								SimpleName lhsName = (SimpleName) leftHandSide;
//								sList.add(lhsName);
//							}
//						}
//						else if (initializer instanceof SimpleName) {
//							// 如果初始化表达式本身就是一个变量名，处理它
//							SimpleName initializerName = (SimpleName) initializer;
//							sList.add(initializerName);
//						}
					}
				}

				return true;
			}
		});
		return sList;
	}

	public static List<SimpleName> getVariableDeclarationList(ASTNode cu) {
		List<SimpleName> sList = new ArrayList<>();
		cu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName variableName = node.getName();
				System.out.println("simpleName:" + node);
				sList.add(variableName);
				return true;
			}
		});
		return sList;
	}

}
