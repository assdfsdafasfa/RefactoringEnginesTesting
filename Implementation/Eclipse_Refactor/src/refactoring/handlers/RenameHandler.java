package refactoring.handlers;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.JavaRefactoringDescriptor;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.core.refactoring.descriptors.RefactoringSignatureDescriptorFactory;
import org.eclipse.jdt.internal.corext.refactoring.ParameterInfo;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.ChangeSignatureProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.ChangeDescriptor;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringChangeDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.history.RefactoringHistory;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.internal.core.refactoring.history.RefactoringHistoryService;

import refactoring.Info.RenameElemInfo;
import refactoring.Info.RenameMethodInfo;
import refactoring.file.parse.CompilationFile;
import refactoring.file.parse.GetProject;
import refactoring.file.parse.RecordTime;
import refactoring.file.parse.SaveFile;
import refactoring.model.EclipseRenameMethodProcessor;
import refactoring.rename.data.RenameMethodData;
import refactoring.undo.UndoHistory;

public class RenameHandler {
	public static long start;
	public static int end;
	public static final boolean DESCRIPTOR_TEST = false;
	public boolean fIsPreDeltaTest = false;
	static List<String> projectList = new ArrayList<>();
	static List<RenameElemInfo> parameterInfos = new ArrayList<>();
	static List<RenameElemInfo> localVariableInfos = new ArrayList<>();
	static List<RenameElemInfo> fieldInfos = new ArrayList<>();
	static List<RenameMethodInfo> methodInfos = new ArrayList<>();
	static List<RenameElemInfo> classInfos = new ArrayList<>();
	static List<IFile> javaFiles = new ArrayList<>();
	protected static final String TEST_OUTPUT_INFIX = "D:\\eclipse\\workspace\\Dataset\\";

	public static void testTypeRenameRefactoring() {
		for (int a = 0; a < javaFiles.size(); a++) {
			IFile file = javaFiles.get(a);
			IJavaElement element = JavaCore.create(file);
			ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
			try {
				IType[] allTypes = iCompilationUnit.getAllTypes();
				for (IType iType : allTypes) {
					try {
						RenameJavaElementDescriptor descriptor = RefactoringSignatureDescriptorFactory
								.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_TYPE);
						descriptor.setJavaElement(iType);
						descriptor.setNewName("newName");
						descriptor.setUpdateReferences(true);

						RefactoringStatus status = new RefactoringStatus();
						Refactoring ref = descriptor.createRefactoring(status);

						RenameTypeProcessor refactoring = (RenameTypeProcessor) ((RenameRefactoring) ref)
								.getProcessor();
						RefactoringStatus status1 = refactoring.checkInitialConditions(new NullProgressMonitor());
						RefactoringStatus status2 = refactoring.checkFinalConditions(new NullProgressMonitor(), null);
						if (status1.isOK() && status2.isOK()) {
							Change change = refactoring.createChange(new NullProgressMonitor());
							change.perform(new NullProgressMonitor()); // 执行重构
//							System.out.println("Method renamed successfully.");
						}
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void testMethodRenameRefactoring(ExecutionEvent event) {
		String refactoringProjectName = "LayoutManagerGroup";
		List<RenameMethodInfo> renameMethodInfos = RenameMethodData.readJson(refactoringProjectName);
		IJavaProject javaProject;
		try {
			javaProject = GetProject.getJavaProject(refactoringProjectName);
			// 获取 Workspace
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			for (int i = 0; i < renameMethodInfos.size(); i++) {
				LocalDateTime time1 = LocalDateTime.now();
				// 根据文件路径创建 IPath 对象
				RenameMethodInfo methodInfo = renameMethodInfos.get(i);
				IPath path = Path.fromOSString(methodInfo.getPath());
				IFile file = workspace.getRoot().getFileForLocation(path);
				IJavaElement element = JavaCore.create(file);
				ICompilationUnit iCompilationUnit = (ICompilationUnit) element;

				IType[] allTypes = iCompilationUnit.getAllTypes();
				IMethod iMethod = getModifierMethod(allTypes, methodInfo.getMethod(), methodInfo.getOffset(),
						methodInfo.getLength());
				if (iMethod != null) {
//					RenameMethodProcessor processor = new RenameNonVirtualMethodProcessor(iMethod);
					EclipseRenameMethodProcessor processor = new EclipseRenameMethodProcessor(iMethod);
					processor.setNewElementName("mask");
					processor.setUpdateReferences(true);
					RenameRefactoring refactoring = new RenameRefactoring(processor);
//					System.out.println("no:"+methodInfo.getNo()+"--path:"+methodInfo.getPath()+"--method name:"+iMethod.getElementName());
					RefactoringStatus status = refactoring.checkInitialConditions(new NullProgressMonitor());
					RefactoringStatus status2 = refactoring.checkFinalConditions(new NullProgressMonitor());
//					CompositeChange compositeChange = new CompositeChange("Undoable Change");
					try {
						Change change = refactoring.createChange(new NullProgressMonitor());
//					System.out.println("change:"+change);
						if (status.isOK() && status2.isOK()) {
							change.perform(new NullProgressMonitor()); // 执行重构
//						compositeChange.add(change);
//						change.perform(new NullProgressMonitor());
//						System.out.println("change:" + change);
							System.out.println("Method renamed successfully.");
						}
//					else {
//						RefactoringStatusEntry[] refactoringStatusEntries= status.getEntries();
//						 for (RefactoringStatusEntry entry : refactoringStatusEntries) {
//						        System.out.println("Condition check failed: " + entry.getMessage());
//						    }
//						System.out.println("Error: " + status.getMessageMatchingSeverity(RefactoringStatus.ERROR));
//					}

					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// 获取 ICompilationUnit
					ICompilationUnit compilationUnit = iMethod.getCompilationUnit();
					String fileContent = compilationUnit.getSource();
					SaveFile.saveContentToFile("D:\\IDEA_build\\saveFile\\Eclipse\\" + methodInfo.getProjectName()
							+ "\\RenameMethod\\" + methodInfo.getNo() + "\\"
							+ ((IType) iMethod.getDeclaringType()).getElementName() + ".java", fileContent);

					List<java.nio.file.Path> modifierPath = RecordTime.recordFileTime(javaFiles, time1);
					SaveFile.saveAllModifierFile(modifierPath, "D:\\IDEA_build\\saveFile\\Eclipse\\"
							+ methodInfo.getProjectName() + "\\RenameMethod\\" + methodInfo.getNo() + "\\");
					if (file.exists()) {
						try {
							TextFileChange textFileChange = new TextFileChange("Sample Change", file);
//							UndoHistory.undoRefactoringHistory(file);
							UndoHistory.undo();
							file.refreshLocal(IFile.DEPTH_INFINITE, new NullProgressMonitor());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("File does not exist");
					}
				}

			}
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("Rename Method Refacatoring Finish");
	}

	public static IMethod getModifierMethod(IType[] allTypes, String name, int offset, int length) {
		try {
			String modName = name;
			String substringToRemove = "New1";
			int index = name.indexOf(substringToRemove);
			if (index != -1) {
				modName = name.substring(0, index);
			}
			for (IType type : allTypes) {
//				System.out.println("重构方法：" + modName + "-offset:" + offset + "-length:" + length);
				for (IMethod method : type.getMethods()) {

					String methodName = method.getElementName();
					int methodOffset = method.getSourceRange().getOffset();
					int methodLength = method.getSourceRange().getLength();
//					System.out.println("method:"+method.getElementName()+"-offset:"+methodOffset+"-length:"+methodLength);
//					if (methodName.equals(modName) && methodOffset == offset && methodLength == length) {
					if (methodName.equals(modName)) {
//						System.out.println("111");
						return method;
					}
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void getMethodInfo(IType type) {
		try {
			for (IMethod method : type.getMethods()) {
				ICompilationUnit compilationUnit = method.getCompilationUnit();
				if (compilationUnit != null) {
					IJavaProject javaProject = method.getJavaProject();
					String projectName = javaProject.getElementName();
					String filePath = compilationUnit.getPath().toString();
					int offset = method.getSourceRange().getOffset();
					int length = method.getSourceRange().getLength();
					String className = method.getDeclaringType().getFullyQualifiedName();
					String[] parameterTypes = method.getParameterTypes();
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testFieldRenameRefactoring() {
		List<IField> fields = new ArrayList<>();
		try {
			// 获取field
			for (int a = 0; a < javaFiles.size(); a++) {
				IFile file = javaFiles.get(a);
				IJavaElement element = JavaCore.create(file);
				ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
				IType[] allTypes = iCompilationUnit.getAllTypes();
				for (IType type : allTypes) {
					for (IField field : type.getFields()) {
						fields.add(field);
					}
				}
			}

			// test field refactoring
			for (int n = 0; n < fields.size(); n++) {
				IField iField = fields.get(n);
				RenameFieldProcessor processor = new RenameFieldProcessor(iField);
				processor.setNewElementName("newFieldName");
				processor.checkInitialConditions(null); // 检查初始条件
//		        RenameRefactoring refactoring = new RenameRefactoring(processor);
				Refactoring refactoring = new ProcessorBasedRefactoring(processor);
				RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor()); // 检查所有条件
				if (status.isOK()) {
					Change change = refactoring.createChange(new NullProgressMonitor());
					change.perform(new NullProgressMonitor()); // 执行重构
				}

				RefactoringHistoryService service = RefactoringHistoryService.getInstance();
				RefactoringHistory history = service.getWorkspaceHistory(new NullProgressMonitor());

//				System.out.println("执行完成");
			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void testLocalVariableRenameRefactoring() {
		List<IMethod> methods = new ArrayList<>();
		List<ILocalVariable> localVariables = new ArrayList<>();
		List<IParameter> parameters = new ArrayList<>();
		try {
			// 获取method
			for (int a = 0; a < javaFiles.size(); a++) {
				IFile file = javaFiles.get(a);
				IJavaElement element = JavaCore.create(file);
				ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
				IType[] allTypes = iCompilationUnit.getAllTypes();
				for (IType type : allTypes) {
					for (IMethod method : type.getMethods()) {
						methods.add(method);
					}
				}
			}
			// test parameter test
			for (int a = 0; a < methods.size(); a++) {
				IMethod iMethod = methods.get(a);
				ILocalVariable[] variables = iMethod.getParameters();
				for (int s = 0; s < variables.length; s++) {
					if (variables[s].isParameter()) {
						parameters.add((IParameter) variables[s]);
						ChangeSignatureProcessor processor = new ChangeSignatureProcessor(iMethod);
						Refactoring refactoring = new ProcessorBasedRefactoring(processor);
						modifyInfos(processor.getParameterInfos(), new String[0]);
						refactoring.checkAllConditions(new NullProgressMonitor()); // 检查所有条件
						Change change = refactoring.createChange(new NullProgressMonitor());
						change.perform(new NullProgressMonitor()); // 执行重构

					}
				}
			}

		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void testParameterRenameRefactoring() {
		List<IMethod> methods = new ArrayList<>();
		try {
			// 获取method
			for (int a = 0; a < javaFiles.size(); a++) {
				IFile file = javaFiles.get(a);
				IJavaElement element = JavaCore.create(file);
				ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
				IType[] allTypes = iCompilationUnit.getAllTypes();
				for (IType type : allTypes) {
					for (IMethod method : type.getMethods()) {
						methods.add(method);
					}
				}
			}

			// 对每个method做refactoring
			for (int n = 0; n < methods.size(); n++) {
				IMethod iMethod = methods.get(n);
				String[] signature = iMethod.getTypeParameterSignatures();
				ChangeSignatureProcessor processor = new ChangeSignatureProcessor(iMethod);
				Refactoring ref = new ProcessorBasedRefactoring(processor);
				modifyInfos(processor.getParameterInfos(), new String[signature.length]);// 这里需要改
				RefactoringStatus status = ref.checkAllConditions(new NullProgressMonitor());
				System.out.println("RefactoringStatus:" + status);
				if (status.isOK()) {
					Change change = ref.createChange(new NullProgressMonitor());
					change.perform(new NullProgressMonitor()); // 执行重构
//		            refactoring.createChange(new NullProgressMonitor()).perform(new NullProgressMonitor());
					System.out.println("Method renamed successfully.");
				} else {
					System.out.println("Error: " + status.getMessageMatchingSeverity(RefactoringStatus.ERROR));
				}
			}

		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static void modifyInfos(List<ParameterInfo> list, String[] newNames) {
		int i = 0;
		for (Iterator<ParameterInfo> iter = list.iterator(); iter.hasNext(); i++) {
			ParameterInfo info = iter.next();
			info.setNewName(newNames[i]);
		}
	}

	private static void collectJavaFiles(File fileName) {
		File[] files = fileName.listFiles();
		if (files == null || files.length == 0) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				collectJavaFiles(f);
			}
			if (f.isFile()) {
				if (f.getName().endsWith(".java")) {
					projectList.add(f.toString().replace(".java", ""));
				}
			}
		}
	}

	private void findIdentifierName(String javaFilePath, String proName) {
		CompilationUnit cUnit = CompilationFile.getCompilationUnit(javaFilePath + ".java");
		getFieldName(cUnit, javaFilePath, proName);
		getMethodNameAndVariableName(cUnit, javaFilePath, proName);
		getTypeName(cUnit, javaFilePath, proName);
	}

	private static String getProjectName(String path) {
		int posistion = path.lastIndexOf("\\");
		String projectName = path.substring(posistion);
		return projectName;
	}

	private static void getFieldName(ASTNode cuu, String javaFilePath, String proName) {
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(FieldDeclaration node) {
				String identifierName = node.toString();
				int position = node.getStartPosition();
				int length = node.getLength();
				RenameElemInfo rElemInfo = new RenameElemInfo(identifierName, proName, javaFilePath, position, length);
				fieldInfos.add(rElemInfo);
				return true;
			}
		});
	}

	private static void getTypeName(ASTNode cuu, String javaFilePath, String proName) {
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(TypeDeclaration node) {
				String className = node.getName().getIdentifier();
				int position = node.getStartPosition();
				int length = node.getLength();
				RenameElemInfo rElemInfo = new RenameElemInfo(className, proName, javaFilePath, position, length);
				classInfos.add(rElemInfo);
				return true;
			}
		});
	}

	private static void getMethodNameAndVariableName(ASTNode cuu, String javaFilePath, String proName) {
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				// 获取方法名
				String methodName = node.getName().getIdentifier();
				int position = node.getStartPosition();
				int length = node.getLength();
				List paremeters = node.parameters();
				String typeName = ((TypeDeclaration) node.getParent()).getName().toString();
				String[] params = new String[paremeters.size()]; // 获取参数类型
				for (int i = 0; i < paremeters.size(); i++) {
					params[i] = ((SingleVariableDeclaration) paremeters.get(i)).getType().toString();
				}
//				RenameMethodInfo mdElemInfo = new RenameMethodInfo(methodName, proName, javaFilePath, position, length,
//						params, typeName);
//				methodInfos.add(mdElemInfo);

				// 遍历方法的局部变量
				Block methodBody = node.getBody();
				for (Object obj : methodBody.statements()) {
					if (obj instanceof VariableDeclarationStatement) {
						VariableDeclarationStatement varStatement = (VariableDeclarationStatement) obj;
						for (Object varObj : varStatement.fragments()) {
							VariableDeclarationFragment varFragment = (VariableDeclarationFragment) varObj;
							String varName = varFragment.getName().getIdentifier();
							int varStartPos = varFragment.getName().getStartPosition();
							int varLength = varFragment.getName().getLength();
							RenameElemInfo varElemInfo = new RenameElemInfo(varName, proName, javaFilePath, varStartPos,
									varLength);
							localVariableInfos.add(varElemInfo);
						}
					}
				}

				// 遍历方法的参数
				for (Object obj : node.parameters()) {
					SingleVariableDeclaration parameter = (SingleVariableDeclaration) obj;
					String parameterName = parameter.getName().getIdentifier();
					int paramStartPos = parameter.getName().getStartPosition();
					int paramLength = parameter.getName().getLength();
					RenameElemInfo paramElemInfo = new RenameElemInfo(parameterName, proName, javaFilePath,
							paramStartPos, paramLength);
					parameterInfos.add(paramElemInfo);
				}
				return true;
			}
		});
	}

	private RenameJavaElementDescriptor createRefactoringDescriptor(IType type, String newName) {
		RenameJavaElementDescriptor descriptor = RefactoringSignatureDescriptorFactory
				.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_TYPE);
		descriptor.setJavaElement(type);
		descriptor.setNewName(newName);
		descriptor.setUpdateReferences(true);
		return descriptor;
	}

// 这个name传入的有问题
	protected IType getType(ICompilationUnit cu, String name) throws JavaModelException {
		for (IType type : cu.getAllTypes()) {
			if (type.getTypeQualifiedName('.').equals(name) || type.getElementName().equals(name)) {
				return type;
			}
		}
		return null;
	}

	protected final Refactoring createRefactoring(RefactoringDescriptor descriptor) throws CoreException {
		RefactoringStatus status = new RefactoringStatus();
		Refactoring refactoring = descriptor.createRefactoring(status);
		return refactoring;
	}

	private void checkMappers(Refactoring refactoring, IType type, String newCUName, IJavaElement[] someClassMembers) {
		RenameTypeProcessor rtp = (RenameTypeProcessor) ((RenameRefactoring) refactoring).getProcessor();
		ICompilationUnit newUnit = (ICompilationUnit) rtp.getRefactoredJavaElement(type.getCompilationUnit());
		IFile newFile = (IFile) rtp.getRefactoredResource(type.getResource());

		if ((type.getParent().getElementType() == IJavaElement.COMPILATION_UNIT)
				&& (type.getElementName() + ".java").equals(type.getCompilationUnit().getElementName())) {
		}

		IPackageFragment oldPackage = (IPackageFragment) type.getCompilationUnit().getParent();
		IPackageFragment newPackage = (IPackageFragment) rtp.getRefactoredJavaElement(oldPackage);

		for (IJavaElement someClassMember : someClassMembers) {
			IMember member = (IMember) someClassMember;
			IJavaElement refactoredMember = rtp.getRefactoredJavaElement(member);
			if (member instanceof IMethod && member.getElementName().equals(type.getElementName()))
				continue; // constructor
		}
	}

	protected final RefactoringStatus performRefactoring(Refactoring ref) throws Exception {
		IUndoManager undoManager = getUndoManager();
		if (DESCRIPTOR_TEST) {
			final CreateChangeOperation create = new CreateChangeOperation(
					new CheckConditionsOperation(ref, CheckConditionsOperation.ALL_CONDITIONS),
					RefactoringStatus.FATAL);
			create.run(new NullProgressMonitor());
			RefactoringStatus checkingStatus = create.getConditionCheckingStatus();
			if (!checkingStatus.isOK())
				return checkingStatus;
			Change change = create.getChange();
			ChangeDescriptor descriptor = change.getDescriptor();
			if (descriptor instanceof RefactoringChangeDescriptor) {
				RefactoringChangeDescriptor rcd = (RefactoringChangeDescriptor) descriptor;
				RefactoringDescriptor refactoringDescriptor = rcd.getRefactoringDescriptor();
				if (refactoringDescriptor instanceof JavaRefactoringDescriptor) {
					JavaRefactoringDescriptor jrd = (JavaRefactoringDescriptor) refactoringDescriptor;
					RefactoringStatus validation = jrd.validateDescriptor();
					if (!validation.isOK())
						return validation;
					RefactoringStatus refactoringStatus = new RefactoringStatus();
					Class<? extends JavaRefactoringDescriptor> expected = jrd.getClass();
					RefactoringContribution contribution = RefactoringCore.getRefactoringContribution(jrd.getID());
					jrd = (JavaRefactoringDescriptor) contribution.createDescriptor(jrd.getID(), jrd.getProject(),
							jrd.getDescription(), jrd.getComment(), contribution.retrieveArgumentMap(jrd),
							jrd.getFlags());
					ref = jrd.createRefactoring(refactoringStatus);
					if (!refactoringStatus.isOK())
						return refactoringStatus;
				}
			}
		}
		final CreateChangeOperation create = new CreateChangeOperation(
				new CheckConditionsOperation(ref, CheckConditionsOperation.ALL_CONDITIONS), RefactoringStatus.FATAL);
		final PerformChangeOperation perform = new PerformChangeOperation(create);
		perform.setUndoManager(undoManager, ref.getName());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		RefactoringStatus status = create.getConditionCheckingStatus();
//		if (!status.isOK())
//			return status;
		if (perform.changeExecuted()) {
			System.out.println("已经执行");
		} else {
			System.out.println("没有执行");
		}
		Change undo = perform.getUndoChange();

		return null;
	}

	private static void getICompilationUnit() {
		IJavaProject javaProject;
		try {
			javaProject = GetProject.getJavaProject("MC322");
			List<IField> fields = new ArrayList<>();
			IJavaElement[] elements = javaProject.getChildren();
			IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
			for (IPackageFragmentRoot packageFragmentRoot : packageFragmentRoots) {
				if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
					IJavaElement[] children = packageFragmentRoot.getChildren();
					for (IJavaElement child : children) {
						if (child.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
							IPackageFragment packageFragment = (IPackageFragment) child;
							ICompilationUnit[] compilationUnits = packageFragment.getCompilationUnits();
							for (ICompilationUnit unit : compilationUnits) {
								if (unit.getResource() instanceof IFile) {
									IFile file = (IFile) unit.getResource();
									if (file.getName().endsWith(".java")) {
										javaFiles.add(file);
									}
								}
							}
						}
					}
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void executePerformOperation(final PerformChangeOperation perform, IWorkspace workspace)
			throws CoreException {
		workspace.run(perform, new NullProgressMonitor());
	}

	protected IUndoManager getUndoManager() {
		IUndoManager undoManager = RefactoringCore.getUndoManager();
		undoManager.flush();
		return undoManager;
	}

	private static IMethod findMethodByNameAndSignature(IType type, String methodName, String[] parameterTypes)
			throws JavaModelException {
		IMethod[] methods = type.getMethods();
		for (IMethod method : methods) {
			if (method.getElementName().equals(methodName) && compareMethodParameters(method, parameterTypes)) {
				return method;
			}
		}
		return null;
	}

	private static boolean compareMethodParameters(IMethod method, String[] parameterTypes) throws JavaModelException {
		String[] methodParameterTypes = method.getParameterTypes();
		if (methodParameterTypes.length == parameterTypes.length) {
			for (int i = 0; i < methodParameterTypes.length; i++) {
				if (!methodParameterTypes[i].equals(parameterTypes[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	protected String getOutputTestFileName(String cuName) {
		return TEST_OUTPUT_INFIX + cuName + ".java";
	}

}
