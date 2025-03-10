package refactoring.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IModuleDescription;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.SourceRange;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.manipulation.SharedASTProviderCore;
import org.eclipse.jdt.internal.corext.refactoring.Checks;
import org.eclipse.jdt.internal.corext.refactoring.rename.MethodChecks;
import org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;
import org.eclipse.jdt.internal.corext.refactoring.util.JavaElementUtil;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.JdtFlags;

public final class RefactoringAvailabilityTesterCore {
	public static boolean isRenameAvailable(final ICompilationUnit unit) {
		if (unit == null)
			return false;
		if (!unit.exists())
			return false;
		if (!JavaModelUtil.isPrimary(unit))
			return false;
		if (unit.isReadOnly())
			return false;
		return true;
	}

	public static boolean isRenameAvailable(final IJavaProject project) throws JavaModelException {
		if (project == null)
			return false;
		if (!Checks.isAvailable(project))
			return false;
		if (!project.isConsistent())
			return false;
		return true;
	}

	public static boolean isRenameAvailable(final IModuleDescription module) throws JavaModelException {
		return Checks.isAvailable(module);
	}

	public static boolean isRenameAvailable(final ILocalVariable variable) throws JavaModelException {
		return Checks.isAvailable(variable);
	}

	public static boolean isRenameAvailable(final IMethod method) throws CoreException {
		if (method == null)
			return false;
		if (!Checks.isAvailable(method))
			return false;
		if (method.isConstructor())
			return false;
		if (isRenameProhibited(method))
			return false;
		return true;
	}

	public static boolean isRenameAvailable(final IPackageFragment fragment) throws JavaModelException {
		if (fragment == null)
			return false;
		if (!Checks.isAvailable(fragment))
			return false;
		if (fragment.isDefaultPackage())
			return false;
		return true;
	}

	public static boolean isRenameAvailable(final IPackageFragmentRoot root) throws JavaModelException {
		if (root == null)
			return false;
		if (!Checks.isAvailable(root))
			return false;
		if (root.isArchive())
			return false;
		if (root.isExternal())
			return false;
		if (!root.isConsistent())
			return false;
		if (root.getResource() instanceof IProject)
			return false;
		return true;
	}

	public static boolean isRenameAvailable(final IResource resource) {
		if (resource == null)
			return false;
		if (!resource.exists())
			return false;
		if (!resource.isAccessible())
			return false;
		return true;
	}

	public static boolean isRenameAvailable(final IType type) throws JavaModelException {
		if (type == null)
			return false;
		if (type.isAnonymous())
			return false;
		if (type.isLambda())
			return false;
		if (!Checks.isAvailable(type))
			return false;
		if (isRenameProhibited(type))
			return false;
		return true;
	}

	public static boolean isRenameAvailable(final ITypeParameter parameter) throws JavaModelException {
		return Checks.isAvailable(parameter);
	}
	public static boolean isRenameProhibited(final IMethod method) throws CoreException {
		if ("toString".equals(method.getElementName()) //$NON-NLS-1$
				&& (method.getNumberOfParameters() == 0) && ("Ljava.lang.String;".equals(method.getReturnType()) //$NON-NLS-1$
						|| "QString;".equals(method.getReturnType()) //$NON-NLS-1$
						|| "Qjava.lang.String;".equals(method.getReturnType()))) //$NON-NLS-1$
			return true;
		else
			return false;
	}

	public static boolean isRenameProhibited(final IType type) {
		return "java.lang".equals(type.getPackageFragment().getElementName()); //$NON-NLS-1$
	}
}
