package refactoring.random.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import refactoring.json.RefactoringRecord;
import refactoring.json.SaveData;
import refactoring.random.gpt.GptApi;
import refactoring.random.model.EclipseRenameLocalVariableProcessor;
import refactoring.random.select.Constant;
import refactoring.random.select.PerformRefactoring;

public class RenameVariableRefactoring {
	public static void performVariableRenameRefactoring(IFile file, CompilationUnit astRoot) throws CoreException {
		LocalVariableVisitor visitor = new LocalVariableVisitor();
		astRoot.accept(visitor);

		// 获取局部变量信息
		ILocalVariable[] localVariables = visitor.getLocalVariables();
		for (ILocalVariable localVar : localVariables) {
			System.out.println("Local Variable: " + localVar.getElementName());
		}

		IJavaElement element = JavaCore.create(file);
		ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
		IType[] allTypes = iCompilationUnit.getAllTypes();
		List<ILocalVariable> iLocalVariablesList = getILocalVariable(allTypes, astRoot, iCompilationUnit);

		if (iLocalVariablesList.size() > 0) {
			Collections.shuffle(iLocalVariablesList);
//			int num = SelectRefactoring.selectRefactoringNumber(iLocalVariablesList.size());
//			for (int a = 0; a < iLocalVariablesList.; a++) {
			selectVariable(iLocalVariablesList);

		} else {
			System.out.println("没有可以rename variable");
		}
	}

	public static void selectVariable(List<ILocalVariable> variableList) throws CoreException {
//		while (variableList.size() > 0) {
//			System.out.println("rename variable");
//			for (ILocalVariable i : variableList) {
//				System.out.println("local:" + i.getElementName());
//			}
//			if (variableList.size() == 1) {
//				ILocalVariable iLocalVariable = variableList.get(0);
//				if (iLocalVariable != null) {
		for (int a = 0; a < variableList.size(); a++) {
			ILocalVariable variable = variableList.get(a);
			ISourceRange sourceRange = variable.getSourceRange();
			int startPosition = sourceRange.getOffset();
			int length = sourceRange.getLength();
			String newName = GptApi
					.printMessage(Constant.RENAME_VARIABLE_PROMPT + variable.getElementName().toString());
			newName = newName.replaceAll(Constant.regEx, "");
			System.out.println("newName:" + newName);
			EclipseRenameLocalVariableProcessor processor = new EclipseRenameLocalVariableProcessor(variable);
			processor.setNewElementName(newName);
			processor.setUpdateReferences(true);
			final RefactoringStatus status = new RefactoringStatus();
			RenameRefactoring refactoring = new RenameRefactoring(processor);
			// 检查所有条件
			if (refactoring.checkAllConditions(new NullProgressMonitor()).isOK()) {
				Change change = refactoring.createChange(new NullProgressMonitor());
				change.perform(new NullProgressMonitor());
				System.out.println("perform rename variable refactoring");

				PerformRefactoring.no += 1;
				RefactoringRecord record = new RefactoringRecord(PerformRefactoring.projectNa, PerformRefactoring.no,
						variable.getElementName(), newName, PerformRefactoring.filePath, startPosition, length,
						PerformRefactoring.className, "Rename class");
				SaveData saveData = new SaveData(record);
				break;
			}
		}
	}

//	break;}else{
//
//	int pos = SelectRefactoring.randomRefactoringIdentifier(variableList.size());
//	ILocalVariable iLocalVariable = variableList.get(pos);if(iLocalVariable!=null)
//	{
//		String newName = GptApi
//				.printMessage(Constant.RENAME_VARIABLE_PROMPT + iLocalVariable.getElementName().toString());
//		System.out.println("newName:" + newName);
//		final RenameJavaElementDescriptor descriptor = RefactoringSignatureDescriptorFactory
//				.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_LOCAL_VARIABLE);
//		descriptor.setJavaElement(iLocalVariable);
//		descriptor.setNewName(newName);
//		descriptor.setUpdateReferences(true);
//		final RefactoringStatus status = new RefactoringStatus();
//		final Refactoring refactoring = descriptor.createRefactoring(status);
//		// 检查所有条件
//		if (refactoring.checkInitialConditions(new NullProgressMonitor()).isOK()) {
//			System.out.println("222");
//			Change change = refactoring.createChange(new NullProgressMonitor());
//			change.perform(new NullProgressMonitor());
//			System.out.println("perform rename variable refactoring");
//			break;
//		} else {
//			variableList.remove(pos);
//			selectVariable(variableList);
//		}
//	}
//	}}
//	}

	public static List<ILocalVariable> getILocalVariable(IType[] allTypes, CompilationUnit astRoot,
			ICompilationUnit compilationUnit) throws JavaModelException {
		List<IMethod> methods = new ArrayList<>();
		for (IType type : allTypes) {

			for (IMethod method : type.getMethods()) {
				methods.add(method);
			}
		}
//		for (int a = 0; a < methods.size(); a++) {
//			IMethod iMethod = methods.get(a);
//
//			ILocalVariable[] variables = iMethod.getParameters();
//			for (int s = 0; s < variables.length; s++) {
//				localVariables.add(variables[s]);
//			}
//		}

		List<ILocalVariable> localVariables = new ArrayList<>();
		List<Integer> fIntegers = new ArrayList<>();
		astRoot.accept(new ASTVisitor() {
			@Override
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				fIntegers.add(name.getStartPosition());
				fIntegers.add(name.getLength());
//				for (Object obj : node.parameters()) {
//					if (obj instanceof SingleVariableDeclaration) {
//						SingleVariableDeclaration variableDeclaration = (SingleVariableDeclaration) obj;
//						IVariableBinding variableBinding = variableDeclaration.resolveBinding();
//						if (variableBinding != null && variableBinding.getKind() == IBinding.VARIABLE) {
//							IJavaElement element = variableBinding.getJavaElement();
//							System.out.println("ele:" + element);
//							if (element instanceof ILocalVariable) {
//								localVariables.add((ILocalVariable) element);
//							}
//						}
//					}
//				}
				return super.visit(node);
			}
		});

//		if (fIntegers.size() > 0) {
//			System.out.println("integer不为空");
//			for (int i = 0; i < fIntegers.size(); i++) {
//				IJavaElement[] elements = compilationUnit.codeSelect(i, i + 1, null);
//				if (elements.length > 0) {
//					if (elements[0] instanceof ILocalVariable) {
//						System.out.println("存在 ilv");
//					}
//				} else {
//					System.out.println("ele为null");
//				}
//
//			}
//		}
		return localVariables;

	}

	private static ILocalVariable findLocalVariable(IBinding binding, IMethod method) throws JavaModelException {
		if (binding instanceof IVariableBinding) {
			IVariableBinding variableBinding = (IVariableBinding) binding;
			IJavaElement element = JavaCore.create((IFile) variableBinding.getJavaElement());
			if (element instanceof ILocalVariable && element.getParent().equals(method)) {
				return (ILocalVariable) element;
			}
		}
		return null;
	}
}

class LocalVariableVisitor extends ASTVisitor {

	private ILocalVariable[] localVariables = new ILocalVariable[0];

	@Override
	public boolean visit(MethodDeclaration node) {
		// 遍历方法内的局部变量
		if (node.getBody() != null) {
			for (Object obj : node.getBody().statements()) {
				if (obj instanceof VariableDeclarationStatement) {
					System.out.println("obj:" + obj);
					VariableDeclarationStatement declarationStatement = (VariableDeclarationStatement) obj;
					for (Object fragmentObj : declarationStatement.fragments()) {
						if (fragmentObj instanceof VariableDeclarationFragment) {
							VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragmentObj;
							SimpleName name = fragment.getName();
							IVariableBinding binding = (IVariableBinding) name.resolveBinding();
							if (binding != null) {
								System.out.println("name:" + binding.getVariableDeclaration());
							}
							if (binding instanceof ILocalVariable) {
								ILocalVariable localVar = (ILocalVariable) binding;
								System.out.println("localVar:" + localVar);
								addLocalVariable(localVar);
							}
						}
					}
				}
			}
		}
		return true;
	}

	private void addLocalVariable(ILocalVariable localVar) {
		ILocalVariable[] newArray = new ILocalVariable[localVariables.length + 1];
		System.arraycopy(localVariables, 0, newArray, 0, localVariables.length);
		newArray[localVariables.length] = localVar;
		localVariables = newArray;
	}

	public ILocalVariable[] getLocalVariables() {
		return localVariables;
	}
}