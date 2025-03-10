package refactoring.random.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.jdt.core.refactoring.descriptors.InlineMethodDescriptor;
import org.eclipse.jdt.core.refactoring.descriptors.JavaRefactoringDescriptor;
import org.eclipse.jdt.internal.core.manipulation.BindingLabelProviderCore;
import org.eclipse.jdt.internal.core.manipulation.JavaElementLabelsCore;
import org.eclipse.jdt.internal.core.manipulation.util.BasicElementLabels;
import org.eclipse.jdt.internal.core.refactoring.descriptors.RefactoringSignatureDescriptorFactory;
import org.eclipse.jdt.internal.corext.dom.IASTSharedValues;
import org.eclipse.jdt.internal.corext.refactoring.JDTRefactoringDescriptorComment;
import org.eclipse.jdt.internal.corext.refactoring.JavaRefactoringDescriptorUtil;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringAvailabilityTesterCore;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.refactoring.base.ReferencesInBinaryContext;
import org.eclipse.jdt.internal.corext.refactoring.changes.DynamicValidationRefactoringChange;
import org.eclipse.jdt.internal.corext.refactoring.changes.TextChangeCompatibility;
import org.eclipse.jdt.internal.corext.refactoring.code.Invocations;
import org.eclipse.jdt.internal.corext.refactoring.code.TargetProvider;
import org.eclipse.jdt.internal.corext.refactoring.util.JavaElementUtil;
import org.eclipse.jdt.internal.corext.refactoring.util.JavaStatusContext;
import org.eclipse.jdt.internal.corext.refactoring.util.RefactoringASTParser;
import org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class EclipseInlineMethodRefactoring extends Refactoring {

	private static final String ATTRIBUTE_MODE = "mode"; //$NON-NLS-1$
	private static final String ATTRIBUTE_DELETE = "delete"; //$NON-NLS-1$

	public static class Mode {
		private Mode() {
		}

		public static final Mode INLINE_ALL = new Mode();
		public static final Mode INLINE_SINGLE = new Mode();
	}

	private ITypeRoot fInitialTypeRoot;
	private ASTNode fInitialNode;
	private TextChangeManager fChangeManager;
	private SourceProvider fSourceProvider;
	private TargetProvider fTargetProvider;
	/**
	 * must never be true if fInitialUnit instanceof IClassFile
	 */
	private boolean fDeleteSource;
	private Mode fCurrentMode;
	private Mode fInitialMode;
	private int fSelectionStart;
	private int fSelectionLength;

	public ICompilationUnit iCompilationUnit;
	public static CompilationUnit compilationUnit;
	public static String simpleName;
	public static MethodDeclaration mDeclaration;
	public static ITypeRoot iTypeRoot;
	public static IDocument fDocument;
	ASTNode[] nodes;

	private EclipseInlineMethodRefactoring(ITypeRoot typeRoot, ASTNode node, int offset, int length) {
		Assert.isNotNull(typeRoot);
		Assert.isTrue(JavaElementUtil.isSourceAvailable(typeRoot));
		Assert.isNotNull(node);
		fInitialTypeRoot = typeRoot;
		fInitialNode = node;
		fSelectionStart = offset;
		fSelectionLength = length;
		iCompilationUnit = (ICompilationUnit) typeRoot;
	}

	private EclipseInlineMethodRefactoring(ICompilationUnit unit, MethodInvocation node, int offset, int length) {
		this(unit, (ASTNode) node, offset, length);
		fTargetProvider = TargetProvider.create(unit, node);
		fInitialMode = fCurrentMode = Mode.INLINE_SINGLE;
		fDeleteSource = false;
		iCompilationUnit = unit;
	}

	private EclipseInlineMethodRefactoring(ICompilationUnit unit, SuperMethodInvocation node, int offset, int length) {
		this(unit, (ASTNode) node, offset, length);
		fTargetProvider = TargetProvider.create(unit, node);
		fInitialMode = fCurrentMode = Mode.INLINE_SINGLE;
		fDeleteSource = false;
		iCompilationUnit = unit;
	}

	private EclipseInlineMethodRefactoring(ICompilationUnit unit, ConstructorInvocation node, int offset, int length) {
		this(unit, (ASTNode) node, offset, length);
		fTargetProvider = TargetProvider.create(unit, node);
		fInitialMode = fCurrentMode = Mode.INLINE_SINGLE;
		fDeleteSource = false;
		iCompilationUnit = unit;
	}

	private EclipseInlineMethodRefactoring(ITypeRoot typeRoot, MethodDeclaration node, int offset, int length) {
		this(typeRoot, (ASTNode) node, offset, length);
		fSourceProvider = new SourceProvider(typeRoot, node);
		fTargetProvider = TargetProvider.create(node);
		fInitialMode = fCurrentMode = Mode.INLINE_ALL;
		fDeleteSource = canEnableDeleteSource();
		iCompilationUnit = (ICompilationUnit) typeRoot;
	}

	/**
	 * Creates a new inline method refactoring
	 * 
	 * @param unit            the compilation unit or class file
	 * @param node            the compilation unit node
	 * @param selectionStart  start
	 * @param selectionLength length
	 * @return returns the refactoring
	 */
	public static EclipseInlineMethodRefactoring create(ITypeRoot unit, CompilationUnit node,
			MethodDeclaration methodDeclaration, int selectionStart, int selectionLength) {
		ASTNode target = RefactoringAvailabilityTesterCore.getInlineableMethodNode(unit, node, selectionStart,
				selectionLength);
		compilationUnit = node;
		simpleName = methodDeclaration.getName().toString();
		iTypeRoot = unit;
		mDeclaration = methodDeclaration;
		if (target == null)
			return null;
		if (target.getNodeType() == ASTNode.METHOD_DECLARATION) {
			System.out.println("------");
			return new EclipseInlineMethodRefactoring(unit, (MethodDeclaration) target, selectionStart,
					selectionLength);
		} else {
			ICompilationUnit cu = (ICompilationUnit) unit;
			if (target.getNodeType() == ASTNode.METHOD_INVOCATION) {
				return new EclipseInlineMethodRefactoring(cu, (MethodInvocation) target, selectionStart,
						selectionLength);
			} else if (target.getNodeType() == ASTNode.SUPER_METHOD_INVOCATION) {
				return new EclipseInlineMethodRefactoring(cu, (SuperMethodInvocation) target, selectionStart,
						selectionLength);
			} else if (target.getNodeType() == ASTNode.CONSTRUCTOR_INVOCATION) {
				return new EclipseInlineMethodRefactoring(cu, (ConstructorInvocation) target, selectionStart,
						selectionLength);
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return RefactoringCoreMessages.InlineMethodRefactoring_name;
	}

	/**
	 * Returns the method to inline, or null if the method could not be found or
	 * {@link #checkInitialConditions(IProgressMonitor)} has not been called yet.
	 *
	 * @return the method, or <code>null</code>
	 */
	public IMethod getMethod() {
		if (fSourceProvider == null)
			return null;
		IMethodBinding binding = fSourceProvider.getDeclaration().resolveBinding();
		if (binding == null)
			return null;
		return (IMethod) binding.getJavaElement();
	}

	public boolean canEnableDeleteSource() {
		return !(fSourceProvider.getTypeRoot() instanceof IClassFile);
	}

	public boolean getDeleteSource() {
		return fDeleteSource;
	}

	public void setDeleteSource(boolean remove) {
		if (remove)
			Assert.isTrue(canEnableDeleteSource());
		fDeleteSource = remove;
	}

	public Mode getInitialMode() {
		return fInitialMode;
	}

	public RefactoringStatus setCurrentMode(Mode mode) throws JavaModelException {
		if (fCurrentMode == mode)
			return new RefactoringStatus();
		Assert.isTrue(getInitialMode() == Mode.INLINE_SINGLE);
		fCurrentMode = mode;
		if (mode == Mode.INLINE_SINGLE) {
			if (fInitialNode instanceof MethodInvocation)
				fTargetProvider = TargetProvider.create((ICompilationUnit) fInitialTypeRoot,
						(MethodInvocation) fInitialNode);
			else if (fInitialNode instanceof SuperMethodInvocation)
				fTargetProvider = TargetProvider.create((ICompilationUnit) fInitialTypeRoot,
						(SuperMethodInvocation) fInitialNode);
			else if (fInitialNode instanceof ConstructorInvocation)
				fTargetProvider = TargetProvider.create((ICompilationUnit) fInitialTypeRoot,
						(ConstructorInvocation) fInitialNode);
			else
				throw new IllegalStateException(String.valueOf(fInitialNode));
		} else {
			fTargetProvider = TargetProvider.create(fSourceProvider.getDeclaration());
		}
		return fTargetProvider.checkActivation();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException {
		RefactoringStatus result = new RefactoringStatus();
//		if (fSourceProvider == null && Invocations.isInvocation(fInitialNode)) {
//			fSourceProvider = resolveSourceProvider(result, fInitialTypeRoot, fInitialNode);
//			if (result.hasFatalError())
//				return result;
//		}
//		result.merge(fSourceProvider.checkActivation());
//		result.merge(fTargetProvider.checkActivation());
		return result;
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException {
		pm.beginTask("", 20); //$NON-NLS-1$
		fChangeManager = new TextChangeManager();
		RefactoringStatus result = new RefactoringStatus();
		System.out.println("fSourceProvider:" + fSourceProvider);
		System.out.println("fTargetProvider:" + fTargetProvider);
//		fSourceProvider.initialize();
//		fTargetProvider.initialize();

		pm.setTaskName(RefactoringCoreMessages.InlineMethodRefactoring_searching);
		RefactoringStatus searchStatus = new RefactoringStatus();
		String binaryRefsDescription = Messages.format(
				RefactoringCoreMessages.ReferencesInBinaryContext_ref_in_binaries_description,
				BasicElementLabels.getJavaElementName(fSourceProvider.getMethodName()));
		ReferencesInBinaryContext binaryRefs = new ReferencesInBinaryContext(binaryRefsDescription);
		ICompilationUnit[] units = fTargetProvider.getAffectedCompilationUnits(searchStatus, binaryRefs,
				new SubProgressMonitor(pm, 1));
		binaryRefs.addErrorIfNecessary(searchStatus);
		if (searchStatus.hasFatalError()) {
			result.merge(searchStatus);
			return result;
		}

//		IFile[] filesToBeModified = getFilesToBeModified(units);
//		result.merge(Checks.validateModifiesFiles(filesToBeModified, getValidationContext(), pm));
//		if (result.hasFatalError())
//			return result;
//		result.merge(ResourceChangeChecker.checkFilesToBeChanged(filesToBeModified, new SubProgressMonitor(pm, 1)));
//		checkOverridden(result, new SubProgressMonitor(pm, 4));
		IProgressMonitor sub = new SubProgressMonitor(pm, 15);
//		sub.beginTask("", units.length * 3); //$NON-NLS-1$
//		for (ICompilationUnit unit : units) {
		sub.subTask(Messages.format(RefactoringCoreMessages.InlineMethodRefactoring_processing,
				BasicElementLabels.getFileName(iCompilationUnit)));
		CallInliner inliner = null;
		try {
			boolean added = false;
			MultiTextEdit root = new MultiTextEdit();
			CompilationUnitChange change = (CompilationUnitChange) fChangeManager.get(iCompilationUnit);
			change.setEdit(root);
			BodyDeclaration[] bodies = getBodyDeclarations(compilationUnit);
//					fTargetProvider.getAffectedBodyDeclarations(iCompilationUnit,
//					new SubProgressMonitor(pm, 1));
			if (bodies.length == 0)
				System.out.println("bodies is null");
			if (bodies.length > 0) {
				inliner = new CallInliner(iCompilationUnit, (CompilationUnit) bodies[0].getRoot(), fSourceProvider);
				for (BodyDeclaration body : bodies) {
					inliner.initialize(body);
//					inliner = new CallInliner(iCompilationUnit, (CompilationUnit) bodies[0].getRoot(), fSourceProvider);
					RefactoringStatus nestedInvocations = new RefactoringStatus();
//					System.out.println("removeNestedCalls");
					ASTNode[] invocations = removeNestedCalls(nestedInvocations, iCompilationUnit, nodes);
					// fTargetProvider.getInvocations(body, new SubProgressMonitor(sub, 2))
					for (ASTNode invocation : invocations) {
						result.merge(inliner.initialize(invocation, fTargetProvider.getStatusSeverity()));
//					if (result.hasFatalError())
//						break;
//						System.out.println("result.getSeverity():" + result.getSeverity());
//						System.out
//								.println("fTargetProvider.getStatusSeverity():" + fTargetProvider.getStatusSeverity());
						if (result.getSeverity() <= fTargetProvider.getStatusSeverity()) {
							added = true;
							TextEditGroup group = new TextEditGroup(
									RefactoringCoreMessages.InlineMethodRefactoring_edit_inline);
							change.addTextEditGroup(group);
							if (result != null && inliner != null && group != null && inliner.perform(group) != null) {
								result.merge(inliner.perform(group));
							}
//							System.out.println("-----------------------");W
						} else {
							fDeleteSource = false;
						}
					}
					// do this after we have inlined the method calls. We still want
					// to generate the modifications.
					if (!nestedInvocations.isOK()) {
						result.merge(nestedInvocations);
						fDeleteSource = false;
					}
				}
			}
			if (!added) {
				fChangeManager.remove(iCompilationUnit);
			} else {
				root.addChild(inliner.getModifications());
				ImportRewrite rewrite = inliner.getImportEdit();
				if (rewrite.hasRecordedChanges()) {
					for (String s : rewrite.getAddedImports()) {
						for (IType t : iCompilationUnit.getTypes()) {
							if (s.startsWith(t.getFullyQualifiedName())) {
								rewrite.removeImport(s);
							}
						}
					}
					if (rewrite.hasRecordedChanges()) {
						TextEdit edit = rewrite.rewriteImports(null);
						if (edit instanceof MultiTextEdit ? ((MultiTextEdit) edit).getChildrenSize() > 0 : true) {
							root.addChild(edit);
							change.addTextEditGroup(
									new TextEditGroup(RefactoringCoreMessages.InlineMethodRefactoring_edit_import,
											new TextEdit[] { edit }));
						}
					}
				}
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inliner != null)
				inliner.dispose();
		}
		sub.worked(1);
		if (sub.isCanceled())
			throw new OperationCanceledException();
//		}
		result.merge(searchStatus);
		sub.done();
		pm.done();
		return result;
	}

	public BodyDeclaration[] getBodyDeclarations(CompilationUnit cUnit) {
		List<BodyDeclaration> bodyDeclarations = new ArrayList<>();
		List<ASTNode> astNodes = new ArrayList<>();
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				MethodDeclaration declaration = (MethodDeclaration) node;
				if (!declaration.getName().toString().equals(simpleName)) {
					System.out.println("not_equals_AST_declaration:" + declaration);
					declaration.accept(new ASTVisitor() {
						public boolean visit(MethodInvocation methodInvocation) {
							System.out.println("methodInvocation:" + methodInvocation.getName().toString());
							System.out.println("simpleName:" + simpleName.toString());
							if (methodInvocation.getName().toString().equals(simpleName.toString())) {
								astNodes.add((ASTNode) methodInvocation);
								if (!bodyDeclarations.contains(declaration)) {
									bodyDeclarations.add(declaration);
								}
							}
							return true;
						}
					});
				}
				return true;
			}
		});
		BodyDeclaration[] bodyDeclarations2 = new BodyDeclaration[bodyDeclarations.size()];
		for (int i = 0; i < bodyDeclarations.size(); i++) {
			System.out.println("bodyDeclarations:" + bodyDeclarations.get(i));
			bodyDeclarations2[i] = bodyDeclarations.get(i);
		}

		nodes = new ASTNode[astNodes.size()];
		for (int j = 0; j < astNodes.size(); j++) {
			nodes[j] = astNodes.get(j);
		}
		return bodyDeclarations2;
	}

//	public ASTNode[] getASTNode(BodyDeclaration body) {
//		MethodDeclaration declaration = (MethodDeclaration) body;
//		declaration.accept(new ASTVisitor() {
//			public boolean visit(Method) {
//			}
//		});
//		return null;
//	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException {
		if (fDeleteSource && fCurrentMode == Mode.INLINE_ALL) {
			TextChange change = fChangeManager.get((ICompilationUnit) fSourceProvider.getTypeRoot());
			TextEdit delete = getDeleteEdit();
			TextEditGroup description = new TextEditGroup(RefactoringCoreMessages.InlineMethodRefactoring_edit_delete,
					new TextEdit[] { delete });
			TextEdit root = change.getEdit();
			if (delete != null) {
				if (root != null) {
					// TODO instead of finding the right insert position the call inliner should
					// reuse the AST & rewriter of the source provide and we should rewrite the
					// whole AST at the end. However, since recursive calls aren't allowed there
					// shouldn't be a text edit overlap.
					// root.addChild(delete);
					TextChangeCompatibility.insert(root, delete);
				} else {
					change.setEdit(delete);
				}
				change.addTextEditGroup(description);
			}
		}
		final Map<String, String> arguments = new HashMap<>();
		String project = null;
		IJavaProject javaProject = fInitialTypeRoot.getJavaProject();
		if (javaProject != null)
			project = javaProject.getElementName();
		int flags = RefactoringDescriptor.STRUCTURAL_CHANGE | JavaRefactoringDescriptor.JAR_REFACTORING
				| JavaRefactoringDescriptor.JAR_SOURCE_ATTACHMENT;
//		final IMethodBinding binding = fSourceProvider.getDeclaration().resolveBinding();
//		final ITypeBinding declaring = binding.getDeclaringClass();
		if (!Modifier.isPrivate(mDeclaration.getModifiers()))
			flags |= RefactoringDescriptor.MULTI_CHANGE;
		final String description = Messages.format(
				RefactoringCoreMessages.InlineMethodRefactoring_descriptor_description_short,
				BasicElementLabels.getJavaElementName(simpleName));
		final String header = Messages.format(RefactoringCoreMessages.InlineMethodRefactoring_descriptor_description,
				new String[] {
						BindingLabelProviderCore.getBindingLabel(null, JavaElementLabelsCore.ALL_FULLY_QUALIFIED),
						BindingLabelProviderCore.getBindingLabel(null, JavaElementLabelsCore.ALL_FULLY_QUALIFIED) });
		final JDTRefactoringDescriptorComment comment = new JDTRefactoringDescriptorComment(project, this, header);
		comment.addSetting(Messages.format(RefactoringCoreMessages.InlineMethodRefactoring_original_pattern,
				BindingLabelProviderCore.getBindingLabel(null, JavaElementLabelsCore.ALL_FULLY_QUALIFIED)));
		if (fDeleteSource)
			comment.addSetting(RefactoringCoreMessages.InlineMethodRefactoring_remove_method);
		if (fCurrentMode == Mode.INLINE_ALL)
			comment.addSetting(RefactoringCoreMessages.InlineMethodRefactoring_replace_references);
		final InlineMethodDescriptor descriptor = RefactoringSignatureDescriptorFactory
				.createInlineMethodDescriptor(project, description, comment.asString(), arguments, flags);
		arguments.put(JavaRefactoringDescriptorUtil.ATTRIBUTE_INPUT,
				JavaRefactoringDescriptorUtil.elementToHandle(project, fInitialTypeRoot));
		arguments.put(JavaRefactoringDescriptorUtil.ATTRIBUTE_SELECTION,
				Integer.toString(fSelectionStart) + " " + Integer.toString(fSelectionLength)); //$NON-NLS-1$
		arguments.put(ATTRIBUTE_DELETE, Boolean.toString(fDeleteSource));
		arguments.put(ATTRIBUTE_MODE, Integer.toString(fCurrentMode == Mode.INLINE_ALL ? 1 : 0));
		return new DynamicValidationRefactoringChange(descriptor,
				RefactoringCoreMessages.InlineMethodRefactoring_edit_inlineCall, fChangeManager.getAllChanges());
	}

	public TextEdit getDeleteEdit() {
		final ASTRewrite rewriter = ASTRewrite.create(mDeclaration.getAST());
		rewriter.remove(mDeclaration, null);
		Map<String, String> options = iTypeRoot instanceof ICompilationUnit
				? ((ICompilationUnit) iTypeRoot).getOptions(true)
				: iTypeRoot.getJavaProject().getOptions(true);
		fDocument = extractDocument(iCompilationUnit);
		System.out.println("fDocument:" + fDocument);
//		System.out.println("options:" + options);
		if (fDocument != null) {
			return rewriter.rewriteAST(fDocument, options);
		}
		return null;
	}

	public static IDocument extractDocument(ICompilationUnit compilationUnit) {
		IFile file = (IFile) compilationUnit.getResource();
		IDocument fDocument = getDocumentFromIFile(file);
		System.out.println("file:" + file);
		System.out.println("fDocument:" + fDocument);
//		if (file != null) {
//			try {
//				FileDocumentProvider documentProvider = new FileDocumentProvider();
//				documentProvider.connect(file);
//				IDocument fDocument = documentProvider.getDocument(file);
//				System.out.println("IDocument:" + fDocument);
//				if (fDocument != null) {
//					return documentProvider.getDocument(file);
//				}
//			} catch (CoreException e) {
//				e.printStackTrace();
//			}
//		}

		return fDocument;
	}

	public static IDocument getDocumentFromIFile(IFile file) {
		IEditorInput editorInput = new FileEditorInput(file);
		ITextEditor editor = getEditor(editorInput);

		if (editor != null) {
			IDocumentProvider documentProvider = editor.getDocumentProvider();
			return documentProvider.getDocument(editorInput);
		}

		return null;
	}

	public static ITextEditor getEditor(IEditorInput editorInput) {
		try {
			return (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.openEditor(editorInput, "org.eclipse.ui.DefaultTextEditor");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SourceProvider resolveSourceProvider(RefactoringStatus status, ITypeRoot typeRoot,
			ASTNode invocation) {
		CompilationUnit root = (CompilationUnit) invocation.getRoot();
		IMethodBinding methodBinding = Invocations.resolveBinding(invocation);
		if (methodBinding == null) {
			status.addFatalError(RefactoringCoreMessages.InlineMethodRefactoring_error_noMethodDeclaration);
			return null;
		}
		MethodDeclaration declaration = (MethodDeclaration) root.findDeclaringNode(methodBinding);
		if (declaration != null) {
			return new SourceProvider(typeRoot, declaration);
		}
		IMethod method = (IMethod) methodBinding.getJavaElement();
		if (method != null) {
			CompilationUnit methodDeclarationAstRoot;
			ICompilationUnit methodCu = method.getCompilationUnit();
			if (methodCu != null) {
				methodDeclarationAstRoot = new RefactoringASTParser(IASTSharedValues.SHARED_AST_LEVEL).parse(methodCu,
						true);
			} else {
				IClassFile classFile = method.getClassFile();
				if (!JavaElementUtil.isSourceAvailable(classFile)) {
					String methodLabel = JavaElementLabelsCore.getTextLabel(method,
							JavaElementLabelsCore.M_FULLY_QUALIFIED | JavaElementLabelsCore.M_PARAMETER_TYPES);
					status.addFatalError(Messages
							.format(RefactoringCoreMessages.InlineMethodRefactoring_error_classFile, methodLabel));
					return null;
				}
				methodDeclarationAstRoot = new RefactoringASTParser(IASTSharedValues.SHARED_AST_LEVEL).parse(classFile,
						true);
			}
			ASTNode node = methodDeclarationAstRoot.findDeclaringNode(methodBinding.getMethodDeclaration().getKey());
			if (node instanceof MethodDeclaration) {
				return new SourceProvider(methodDeclarationAstRoot.getTypeRoot(), (MethodDeclaration) node);
			}
		}
		status.addFatalError(RefactoringCoreMessages.InlineMethodRefactoring_error_noMethodDeclaration);
		return null;
	}

	private IFile[] getFilesToBeModified(ICompilationUnit[] units) {
		List<IFile> result = new ArrayList<>(units.length + 1);
		IFile file;
		for (ICompilationUnit unit : units) {
			file = getFile(unit);
			if (file != null)
				result.add(file);
		}
		if (fDeleteSource) {
			file = getFile((ICompilationUnit) fSourceProvider.getTypeRoot());
			if (file != null && !result.contains(file))
				result.add(file);
		}
		return result.toArray(new IFile[result.size()]);
	}

	private IFile getFile(ICompilationUnit unit) {
		unit = unit.getPrimary();
		IResource resource = unit.getResource();
		if (resource != null && resource.getType() == IResource.FILE)
			return (IFile) resource;
		return null;
	}

	private void checkOverridden(RefactoringStatus status, IProgressMonitor pm) throws JavaModelException {
		pm.beginTask("", 9); //$NON-NLS-1$
		pm.setTaskName(RefactoringCoreMessages.InlineMethodRefactoring_checking_overridden);
		MethodDeclaration decl = fSourceProvider.getDeclaration();
		IMethod method = (IMethod) decl.resolveBinding().getJavaElement();
		if (method == null || Flags.isPrivate(method.getFlags())) {
			pm.worked(8);
			return;
		}
		IType type = method.getDeclaringType();
		ITypeHierarchy hierarchy = type.newTypeHierarchy(new SubProgressMonitor(pm, 6));
		checkSubTypes(status, method, hierarchy.getAllSubtypes(type), new SubProgressMonitor(pm, 1));
		checkSuperClasses(status, method, hierarchy.getAllSuperclasses(type), new SubProgressMonitor(pm, 1));
		checkSuperInterfaces(status, method, hierarchy.getAllSuperInterfaces(type), new SubProgressMonitor(pm, 1));
		pm.setTaskName(""); //$NON-NLS-1$
	}

	private void checkSubTypes(RefactoringStatus result, IMethod method, IType[] types, IProgressMonitor pm) {
		checkTypes(result, method, types, RefactoringCoreMessages.InlineMethodRefactoring_checking_overridden_error,
				pm);
	}

	private void checkSuperClasses(RefactoringStatus result, IMethod method, IType[] types, IProgressMonitor pm) {
		checkTypes(result, method, types, RefactoringCoreMessages.InlineMethodRefactoring_checking_overrides_error, pm);
	}

	private void checkSuperInterfaces(RefactoringStatus result, IMethod method, IType[] types, IProgressMonitor pm) {
		checkTypes(result, method, types, RefactoringCoreMessages.InlineMethodRefactoring_checking_implements_error,
				pm);
	}

	private void checkTypes(RefactoringStatus result, IMethod method, IType[] types, String key, IProgressMonitor pm) {
		pm.beginTask("", types.length); //$NON-NLS-1$
		for (IType type : types) {
			pm.worked(1);
			IMethod[] overridden = type.findMethods(method);
			if (overridden != null && overridden.length > 0) {
				result.addError(
						Messages.format(key,
								JavaElementLabelsCore.getElementLabel(type, JavaElementLabelsCore.ALL_DEFAULT)),
						JavaStatusContext.create(overridden[0]));
			}
		}
	}

	private ASTNode[] removeNestedCalls(RefactoringStatus status, ICompilationUnit unit, ASTNode[] invocations) {
//		System.out.println("invocations:" + invocations);
		if (invocations.length <= 1) {
//			System.out.println("invocations.length:" + invocations.length);
			return invocations;
		}
		ASTNode[] parents = new ASTNode[invocations.length];
		for (int i = 0; i < invocations.length; i++) {
			parents[i] = invocations[i].getParent();
		}
		for (int i = 0; i < invocations.length; i++) {
			removeNestedCalls(status, unit, parents, invocations, i);
		}
		List<ASTNode> result = new ArrayList<>();
		for (ASTNode invocation : invocations) {
			if (invocation != null)
				result.add(invocation);
		}
		return result.toArray(new ASTNode[result.size()]);
	}

	private void removeNestedCalls(RefactoringStatus status, ICompilationUnit unit, ASTNode[] parents,
			ASTNode[] invocations, int index) {
		ASTNode invocation = invocations[index];
		for (ASTNode parent2 : parents) {
			ASTNode parent = parent2;
			while (parent != null) {
				if (parent == invocation) {
					status.addError(RefactoringCoreMessages.InlineMethodRefactoring_nestedInvocation,
							JavaStatusContext.create(unit, parent));
					invocations[index] = null;
				}
				parent = parent.getParent();
			}
		}
	}

}
