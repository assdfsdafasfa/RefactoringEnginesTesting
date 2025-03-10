package refactoring.random.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.select.PerformRefactoring;

public class InverseEncapsulateFieldRefactoringPerform {
	public static void performInverseEncapsulateFieldRefactoring(IFile file, CompilationUnit cUnit)
			throws JavaModelException {
		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		List<FieldDeclaration> fieldDeclarations = getDeclarations(cUnit);
		List<String> methodName = getMethodDeclaration(cUnit);
		if (fieldDeclarations.size() > 0 && methodName.size() > 0) {
			for (int a = fieldDeclarations.size() - 1; a >= 0; a--) {
				System.out.println("fd。size:" + fieldDeclarations.size());
				for (Object fragment : fieldDeclarations.get(a).fragments()) {
					if (fragment instanceof VariableDeclarationFragment) {
						VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragment;
						String temp = variableDeclarationFragment.getName().getIdentifier();
						String setName = "set" + capitalize(temp);
						String getName = "get" + capitalize(temp);
						if (!methodName.contains(setName) && !methodName.contains(getName)) {
							if (fieldDeclarations.size() > 0 && a > 0 && fieldDeclarations.size() >= a) {
								fieldDeclarations.remove(a - 1);
							}
						}

					}
				}
			}
			Collections.shuffle(fieldDeclarations);
			if (fieldDeclarations.size() > 0) {
				selectRefactoring(fieldDeclarations.get(0), iCompilationUnit, cUnit);
			}

		} else {
			System.out.println("not inverse field");
		}
	}

	public static void selectRefactoring(FieldDeclaration fDeclaration, ICompilationUnit iCompilationUnit,
			CompilationUnit cUnit) throws JavaModelException {
		String fieldName = ((VariableDeclarationFragment) fDeclaration.fragments().get(0)).getName().getIdentifier();
		DeEncapsulationVisitor visitor = new DeEncapsulationVisitor(cUnit, fDeclaration);
		cUnit.accept(visitor);
		visitor.applyChanges(iCompilationUnit);

		int startPosition = fDeclaration.getStartPosition();
		int length = fDeclaration.getLength();
		PerformRefactoring.no += 1;
		RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa, PerformRefactoring.no, fieldName,
				fieldName, PerformRefactoring.filePath, startPosition, length, PerformRefactoring.className,
				"inverse encapsulate field");
		SaveData saveData = new SaveData(record);
//		Modifier modifier = AST.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
//		fieldDeclaration.modifiers().add(modifier);
//		fDeclaration.modifiers()
//				.removeIf(modifier -> modifier instanceof Modifier && ((Modifier) modifier).isPrivate());
//
//		String fieldName = ((VariableDeclarationFragment) fDeclaration.fragments().get(0)).getName().getIdentifier();
//		IMethod[] methods = iCompilationUnit.findPrimaryType().getMethods();
//		for (IMethod method : methods) {
//			System.out.println("methodName:--" + method.getElementName());
//			if (method.getElementName().equals("get" + capitalize(fieldName))
//					|| method.getElementName().equals("set" + capitalize(fieldName))) {
//				method.delete(true, null);
//				List<MethodInvocation> invocations = getMethodInvocation(iCompilationUnit, cUnit,
//						"get" + capitalize(fieldName), "set" + capitalize(fieldName), fieldName);
//			}
//		}
//		iCompilationUnit.commitWorkingCopy(true, null);

	}

	private static String capitalize(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	public static List<FieldDeclaration> getDeclarations(CompilationUnit cUnit) {
		List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
		cUnit.accept(new ASTVisitor() {
			public boolean visit(FieldDeclaration node) {
				int modifiers = node.getModifiers();
				Type fieldType = node.getType();
				if (Modifier.isPrivate(modifiers) && !node.toString().contains("boolean")) {
					fieldDeclarations.add(node);
				}
				return true;
			}
		});
		return fieldDeclarations;
	}

	public static List<String> getMethodDeclaration(CompilationUnit cUnit) {
		List<String> methodDeclarations = new ArrayList<>();
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				methodDeclarations.add(node.getName().toString());
				return true;
			}
		});
		return methodDeclarations;
	}

	public static List<MethodInvocation> getMethodInvocation(ICompilationUnit cu, CompilationUnit cUnit, String getName,
			String setName, String fieldName) {
		List<MethodInvocation> methodInvocation = new ArrayList<>();
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodInvocation node) {
				String methodName = node.getName().toString();
				System.out.println("methodInvocation name:" + methodName);
				System.out.println("getName");
				if (methodName.equals(getName)) {
					SimpleName fieldReference = node.getAST().newSimpleName(fieldName);
					System.out.println("fieldReference:" + fieldReference);
//					FieldAccess fieldAccess = node.getAST().newFieldAccess();
//					fieldAccess.setExpression(fieldAccess);
//					fieldAccess.setName(fieldReference);

					AST ast = cUnit.getAST();
					ASTRewrite rewrite = ASTRewrite.create(ast);
					rewrite.replace(node, fieldReference, null);
//					cUnit.accept(null);
					try {
//						System.out.println("----");
						String source = cu.getSource();
						Document document = new Document(source);
//						TextEdit edits = rewrite.rewriteAST();
						TextEdit edits = rewrite.rewriteAST();
//								cUnit.rewrite(document, cu.getJavaProject().getOptions(true));
						try {
							edits.apply(document);
							cu.getBuffer().setContents(document.get());
						} catch (MalformedTreeException | BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						TextFileChange change = new TextFileChange("", (IFile) cu.getResource());
						change.setEdit(edits);
						change.perform(null);
//						System.out.println("1111");
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (methodName.equals(setName)) {

				}
				methodInvocation.add(node);
				return true;
			}
		});
		return methodInvocation;
	}
}

class DeEncapsulationVisitor extends ASTVisitor {
	private ASTRewrite rewrite;
	private CompilationUnit cu;
	private FieldDeclaration fieldDeclaration;
	String fieldName;
	String getName;
	String setName;

	public DeEncapsulationVisitor(CompilationUnit cUnit, FieldDeclaration fieldDeclarations) {
		fieldDeclaration = fieldDeclarations;
		this.cu = cUnit;
		AST ast = cu.getAST();
		this.rewrite = ASTRewrite.create(ast);

		fieldName = ((VariableDeclarationFragment) fieldDeclaration.fragments().get(0)).getName().getIdentifier();
		getName = "get" + capitalize(fieldName);
		setName = "set" + capitalize(fieldName);
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		// Change private to public
		ListRewrite listRewrite = rewrite.getListRewrite(fieldDeclaration, FieldDeclaration.MODIFIERS2_PROPERTY);
		for (Object modifier : fieldDeclaration.modifiers()) {
			if (modifier instanceof Modifier && ((Modifier) modifier).getKeyword() == ModifierKeyword.PRIVATE_KEYWORD) {
				rewrite.replace((ASTNode) modifier, node.getAST().newModifier(ModifierKeyword.PUBLIC_KEYWORD), null);
			}
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// Remove getter and setter methods
		if (node.getName().toString().equals(getName) || node.getName().toString().equals(setName)) {
			rewrite.remove(node, null);
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// Replace method invocation with field access or assignment
		System.out.println("MethodInvocation name:" + node.getName().toString());
		if (isGetterMethodInvocation(node) && node.getName().getIdentifier().toString().length() > 3
				&& !node.getName().getIdentifier().contains("false") && !node.getName().getIdentifier().contains("true")
				&& !node.getName().getIdentifier().contains("this") && !node.getName().getIdentifier().equals("class")
				&& !node.getName().getIdentifier().contains("null")
				&& !node.getName().getIdentifier().toLowerCase().contains("int")
				&& !node.getName().getIdentifier().toLowerCase().contains("float")
				&& !node.getName().getIdentifier().toLowerCase().contains("byte")
				&& !node.getName().getIdentifier().toLowerCase().contains("double")
				&& !node.getName().getIdentifier().toLowerCase().contains("char")
				&& !node.getName().getIdentifier().contains("super")
				&& !node.getName().getIdentifier().toLowerCase().contains("long")
				&& !node.getName().getIdentifier().toLowerCase().contains("boolean")
				&& !node.getName().getIdentifier().toLowerCase().contains("double")
				&& !node.getName().getIdentifier().toLowerCase().contains("short")
				&& !node.getName().getIdentifier().toLowerCase().equals("")
				&& !node.getName().getIdentifier().toLowerCase().equals("default")) {
			SimpleName fieldName = node.getAST()
					.newSimpleName(node.getName().getIdentifier().substring(3).toLowerCase());
			rewrite.replace(node, fieldName, null);
		} else if (isSetterMethodInvocation(node)) {
			AST ast = cu.getAST();
			// Assuming the argument is a simple expression (e.g., a SimpleName or a
			// NumberLiteral)
			System.out.println("node:" + node.toString());
			int firstOpenParenIndex = node.toString().indexOf('(');
			int lastCloseParenIndex = node.toString().lastIndexOf(')');
			String argString = node.toString().substring(firstOpenParenIndex + 1, lastCloseParenIndex);
//			SimpleName expression = (Expression) node.arguments().get(0);
			String regex = "[^a-zA-Z]";
			Pattern pattern = Pattern.compile(regex);
			if (!argString.contains("false") && !argString.contains("true") && !argString.contains("this")
					&& !argString.contains("class") && !argString.contains("null")
					&& !argString.toLowerCase().contains("int") && !argString.toLowerCase().contains("float")
					&& !argString.toLowerCase().contains("byte") && !argString.toLowerCase().contains("double")
					&& !argString.toLowerCase().contains("char") && !argString.contains("super")
					&& !argString.toLowerCase().contains("long") && !argString.toLowerCase().contains("boolean")
					&& !argString.toLowerCase().contains("double") && !argString.toLowerCase().contains("short")
					&& !pattern.matcher(argString).find() && !argString.toLowerCase().equals("")
					&& !argString.toLowerCase().equals("default")) {
				Assignment assignment = node.getAST().newAssignment();
				if (node.getName().getIdentifier().substring(3).toLowerCase() != ""
						&& !node.getName().getIdentifier().substring(3).toLowerCase().contains("><")) {
					SimpleName fieldName = node.getAST()
							.newSimpleName(node.getName().getIdentifier().substring(3).toLowerCase());
					assignment.setLeftHandSide(fieldName);
					assignment.setRightHandSide(ast.newSimpleName(argString));// argument.toString()
					rewrite.replace(node, assignment, null);
				}
			}
		}
		return super.visit(node);
	}

	public void applyChanges(ICompilationUnit cu) throws JavaModelException, IllegalArgumentException {
		TextEdit edits = rewrite.rewriteAST();
		try {
			Document document = new Document(cu.getSource());
			edits.apply(document);
			cu.getBuffer().setContents(document.get());
		} catch (MalformedTreeException | BadLocationException e) {
			e.printStackTrace();
		}
	}

	private boolean isGetterMethod(MethodDeclaration method) {
		// Implement your logic to identify getter methods
		// For example, check method name, return type, and parameters
		return method.getName().getIdentifier().startsWith("get") && method.parameters().isEmpty()
				&& method.getReturnType2() != null;
	}

	private boolean isSetterMethod(MethodDeclaration method) {
		// Implement your logic to identify setter methods
		// For example, check method name, return type, and parameters
		return method.getName().getIdentifier().startsWith("set") && !method.parameters().isEmpty()
				&& method.getReturnType2() == null;
	}

	private boolean isGetterMethodInvocation(MethodInvocation node) {
		return node.getName().getIdentifier().startsWith("get");
	}

	private boolean isSetterMethodInvocation(MethodInvocation node) {
		return node.getName().getIdentifier().startsWith("set");
	}

	private static String capitalize(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
}