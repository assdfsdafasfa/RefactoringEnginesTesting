package refactoring.random.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.jdt.core.search.MethodDeclarationMatch;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.internal.core.manipulation.util.BasicElementLabels;
import org.eclipse.jdt.internal.corext.refactoring.Checks;
import org.eclipse.jdt.internal.corext.refactoring.JavaRefactoringArguments;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.refactoring.SearchResultGroup;
import org.eclipse.jdt.internal.corext.refactoring.delegates.DelegateMethodCreator;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.corext.refactoring.util.JavaStatusContext;
import org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.jdt.internal.corext.util.JdtFlags;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.ltk.core.refactoring.GroupCategorySet;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.RefactoringStatusContext;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.text.edits.ReplaceEdit;

public class EclipseRenameNonVirtualMethodProcessor extends RenameMethodProcessor {

	/**
	 * Creates a new rename method processor.
	 * <p>
	 * This constructor is only invoked by <code>RenameTypeProcessor</code>.
	 * </p>
	 *
	 * @param method      the method
	 * @param manager     the change manager
	 * @param categorySet the group category set
	 */
	EclipseRenameNonVirtualMethodProcessor(IMethod method, TextChangeManager manager, GroupCategorySet categorySet) {
		super(method, manager, categorySet);
	}

	/**
	 * Creates a new rename method processor.
	 * 
	 * @param method the method, or <code>null</code> if invoked by scripting
	 */
	public EclipseRenameNonVirtualMethodProcessor(IMethod method) {
		super(method);
	}

	/**
	 * Creates a new rename method processor from scripting arguments
	 *
	 * @param method    the method, or <code>null</code> if invoked by scripting
	 * @param arguments the arguments
	 * @param status    the resulting status
	 */
	public EclipseRenameNonVirtualMethodProcessor(IMethod method, JavaRefactoringArguments arguments,
			RefactoringStatus status) {
		this(method);
		RefactoringStatus initializeStatus = initialize(arguments);
		status.merge(initializeStatus);
	}

	@Override
	public boolean isApplicable() throws CoreException {
		return true;
	}

	// ----------- preconditions --------------

	@Override
	protected RefactoringStatus doCheckFinalConditions(IProgressMonitor pm, CheckConditionsContext checkContext)
			throws CoreException {
		try {
			pm.beginTask("", 3); //$NON-NLS-1$
			RefactoringStatus result = new RefactoringStatus();
			result.merge(super.doCheckFinalConditions(new SubProgressMonitor(pm, 1), checkContext));
			if (result.hasFatalError())
				return result;

			final IMethod method = getMethod();
			final IType declaring = method.getDeclaringType();
			final String name = getNewElementName();
			IMethod[] hierarchyMethods = hierarchyDeclaresMethodName(new SubProgressMonitor(pm, 1),
					declaring.newTypeHierarchy(new SubProgressMonitor(pm, 1)), method, name);

			for (IMethod hierarchyMethod : hierarchyMethods) {
				RefactoringStatusContext context = JavaStatusContext.create(hierarchyMethod);
				if (Checks.compareParamTypes(method.getParameterTypes(), hierarchyMethod.getParameterTypes())) {
					String message = Messages
							.format(RefactoringCoreMessages.RenamePrivateMethodRefactoring_hierarchy_defines,
									new String[] {
											BasicElementLabels.getJavaElementName(declaring.getFullyQualifiedName('.')),
											BasicElementLabels.getJavaElementName(name) });
					result.addError(message, context);
				} else {
					String message = Messages
							.format(RefactoringCoreMessages.RenamePrivateMethodRefactoring_hierarchy_defines2,
									new String[] {
											BasicElementLabels.getJavaElementName(declaring.getFullyQualifiedName('.')),
											BasicElementLabels.getJavaElementName(name) });
					result.addWarning(message, context);
				}
			}
			return result;
		} finally {
			pm.done();
		}
	}

	private static IMethod[] classesDeclareMethodName(ITypeHierarchy hier, List<IType> classes, IMethod method,
			String newName) throws CoreException {
		Set<IMethod> result = new HashSet<>();
		IType type = method.getDeclaringType();
		List<IType> subtypes = Arrays.asList(hier.getAllSubtypes(type));

		int parameterCount = method.getParameterTypes().length;
		boolean isMethodPrivate = JdtFlags.isPrivate(method);

		for (IType clazz : classes) {
			boolean isSubclass = subtypes.contains(clazz);
			for (IMethod m : clazz.getMethods()) {
				IMethod foundMethod = Checks.findMethod(newName, parameterCount, false, new IMethod[] { m });
				if (foundMethod == null)
					continue;
				if (isSubclass || type.equals(clazz) || (!isMethodPrivate && !JdtFlags.isPrivate(m))) {
					result.add(foundMethod);
				}
			}
		}
		return result.toArray(new IMethod[result.size()]);
	}

	final static IMethod[] hierarchyDeclaresMethodName(IProgressMonitor pm, ITypeHierarchy hierarchy, IMethod method,
			String newName) throws CoreException {
		try {
			Set<IMethod> result = new HashSet<>();
			IType type = method.getDeclaringType();
			IMethod foundMethod = Checks.findMethod(newName, method.getParameterTypes().length, false, type);
			if (foundMethod != null)
				result.add(foundMethod);
			IMethod[] foundInHierarchyClasses = classesDeclareMethodName(hierarchy,
					Arrays.asList(hierarchy.getAllClasses()), method, newName);
			if (foundInHierarchyClasses != null)
				result.addAll(Arrays.asList(foundInHierarchyClasses));
			IType[] implementingClasses = hierarchy.getImplementingClasses(type);
			IMethod[] foundInImplementingClasses = classesDeclareMethodName(hierarchy,
					Arrays.asList(implementingClasses), method, newName);
			if (foundInImplementingClasses != null)
				result.addAll(Arrays.asList(foundInImplementingClasses));
			return result.toArray(new IMethod[result.size()]);
		} finally {
			if (pm != null) {
				pm.done();
			}
		}
	}

	/*
	 * @see RenameMethodProcessor#addOccurrences(org.eclipse.jdt.internal.corext.
	 * refactoring.util.TextChangeManager,
	 * org.eclipse.core.runtime.IProgressMonitor, RefactoringStatus)
	 */
	@Override
	protected void addOccurrences(TextChangeManager manager, IProgressMonitor pm, RefactoringStatus status)
			throws CoreException {
		pm.beginTask("", 1); //$NON-NLS-1$
		// declaration update must be registered first
		addDeclarationUpdate(manager);
		if (getUpdateReferences())
			addReferenceUpdates(manager, pm);
		pm.worked(1);
	}

	private ICompilationUnit getDeclaringCU() {
		return getMethod().getCompilationUnit();
	}

	private void addDeclarationUpdate(TextChangeManager manager) throws CoreException {

		if (getDelegateUpdating()) {
			// create the delegate
			CompilationUnitRewrite rewrite = new CompilationUnitRewrite(getDeclaringCU());
			rewrite.setResolveBindings(true);
			MethodDeclaration methodDeclaration = ASTNodeSearchUtil.getMethodDeclarationNode(getMethod(),
					rewrite.getRoot());
			DelegateMethodCreator creator = new DelegateMethodCreator();
			creator.setDeclaration(methodDeclaration);
			creator.setDeclareDeprecated(getDeprecateDelegates());
			creator.setSourceRewrite(rewrite);
			creator.setCopy(true);
			creator.setNewElementName(getNewElementName());
			creator.prepareDelegate();
			creator.createEdit();
			CompilationUnitChange cuChange = rewrite.createChange(true);
			if (cuChange != null) {
				cuChange.setKeepPreviewEdits(true);
				manager.manage(getDeclaringCU(), cuChange);
			}
		}

		String editName = RefactoringCoreMessages.RenameMethodRefactoring_update_declaration;
		ISourceRange nameRange = getMethod().getNameRange();
		ReplaceEdit replaceEdit = new ReplaceEdit(nameRange.getOffset(), nameRange.getLength(), getNewElementName());
		addTextEdit(manager.get(getDeclaringCU()), editName, replaceEdit);
	}

	private void addReferenceUpdates(TextChangeManager manager, IProgressMonitor pm) {
		for (SearchResultGroup group : getOccurrences()) {
			ICompilationUnit cu = group.getCompilationUnit();
			TextChange change = manager.get(cu);
			for (SearchMatch match : group.getSearchResults()) {
				if (!(match instanceof MethodDeclarationMatch)) {
					ReplaceEdit replaceEdit = createReplaceEdit(match, cu);
					String editName = RefactoringCoreMessages.RenamePrivateMethodRefactoring_update;
					addTextEdit(change, editName, replaceEdit);
				}
			}
		}
		pm.done();
	}

	@Override
	public String getDelegateUpdatingTitle(boolean plural) {
		if (plural)
			return RefactoringCoreMessages.DelegateMethodCreator_keep_original_renamed_plural;
		else
			return RefactoringCoreMessages.DelegateMethodCreator_keep_original_renamed_singular;
	}
}