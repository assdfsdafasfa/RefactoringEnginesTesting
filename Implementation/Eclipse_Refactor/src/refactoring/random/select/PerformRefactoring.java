package refactoring.random.select;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

import refactoring.file.parse.GetProject;
import refactoring.json.SaveData;
import refactoring.random.type.EncapsulateFieldRefactoring;
import refactoring.random.type.ExtractConstantRefactoringPerform;
import refactoring.random.type.ExtractMethodRefactoringPerform;
import refactoring.random.type.ExtractVariableRefactoring;
import refactoring.random.type.InlineConstantRefactoringPerform;
import refactoring.random.type.InlineVariableRefactoringPerform;
import refactoring.random.type.RenameFieldRefactoring;
import refactoring.random.type.RenameMethodRefactoring;
import refactoring.random.type.RenameTypeRefactoring;
import refactoring.random.type.RenameVariableRefactoring;

public class PerformRefactoring {
	public static int no = 0;
	public static String projectNa;
	public static String filePath;
	public static String className;
//	public static List<RefactoringRecord> records = new ArrayList<>();

	public static void performRefactoring(ExecutionEvent event) throws CoreException, OperationCanceledException,
			InvocationTargetException, InterruptedException, IOException {
//		ImportProjectAction.importProject("D:\\RefactoringdatasetGit");
		String projectName = getName();
		projectNa = projectName;
		IJavaProject javaProject = GetProject.getJavaProject(projectName);
		List<IFile> allIJavaFiles = ParseJavaFile.getAllJavaFiles(javaProject);
		System.out.println("文件数量：" + allIJavaFiles.size());
		selectRefactoring(allIJavaFiles, projectName);
		SaveData.createJsonFile(Constant.SAVE_DATA_PATH_JSON + projectName + "/" + projectName + ".json");

		System.out.println("------program execution completion--------");
	}

	private static void importProject() {
		String workspacePath = "D:\\eclipse\\runtime-EclipseApplication"; // Eclipse 工作空间路径
		String projectPath = "D:\\AllProject\\Eclipse\\opendis7-source-generator"; // 要导入的项目路径

		// 创建项目导入操作
		ImportOperation importOperation = new ImportOperation(Path.fromOSString(projectPath),
				ResourcesPlugin.getWorkspace().getRoot(), FileSystemStructureProvider.INSTANCE, new IOverwriteQuery() {
					@Override
					public String queryOverwrite(String pathString) {
						return ALL;
					}
				}, null);

		// 设置操作参数
//		WizardResourceImportPage importPage = new WizardResourceImportPage("Import Projects", importOperation);
//		importPage.setContainer(null);
//		importPage.setOverwriteResources(true);
//		importPage.setCreateContainerStructure(false);

		// 执行导入操作
		try {
//			importPage.createControl(null);
			importOperation.setContext(null);
			importOperation.run(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void selectRefactoring(List<IFile> allIJavaFiles, String projectName) throws CoreException,
			IOException, OperationCanceledException, InvocationTargetException, InterruptedException {
		for (int i = 0; i < allIJavaFiles.size(); i++) {
			IFile iFile = allIJavaFiles.get(i);
			filePath = iFile.getLocation().toOSString();// 2

			CompilationUnit cUnit = ParseJavaFile.getCompilationUnit(iFile);
			cUnit.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration typeDeclaration) {
					className = typeDeclaration.getName().getIdentifier();// 3
					return false;
				}
			});

			String refactoringBeforeCode = cUnit.toString();
			SaveRefactoringFile.saveCode(
					Constant.CODE_SAVE_PATH_REFACTORING_BEFORE + projectName + "/before/" + i + "_"
							+ cUnit.getJavaElement().getElementName().toString().replace(".java", "") + ".txt",
					refactoringBeforeCode);

			IFile tempFile = allIJavaFiles.get(i);
			CompilationUnit tempUnit = ParseJavaFile.getCompilationUnit(tempFile);
			performDiffRefactoring(tempFile, tempUnit);// perform multiple ref...

			CompilationUnit cUnitAft = ParseJavaFile.getCompilationUnit(iFile);
			String refactoringAfterCode = cUnitAft.toString();
			SaveRefactoringFile.saveCode(
					Constant.CODE_SAVE_PATH_REFACTORING_AFTER + projectName + "/after/" + i + "_"
							+ cUnit.getJavaElement().getElementName().toString().replace(".java", "") + ".txt",
					refactoringAfterCode);
		}
	}

	private static void performDiffRefactoring(IFile iFile, CompilationUnit compilationUnit) throws CoreException,
			IOException, OperationCanceledException, InvocationTargetException, InterruptedException {

//		if (SelectRefactoring.selectRefactoringNumber() == 1) {
//			System.out.println("rename class refactoring");
//			RenameTypeRefactoring.performRenameTypeRefactoring(iFile);
//		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("rename method refactoring");
			RenameMethodRefactoring.performRenameMethodRefactoring(iFile);
		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("rename field refactoring");
			RenameFieldRefactoring.performFieldRenameRefactoring(iFile);
		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("rename variable refactoring");
			RenameVariableRefactoring.performVariableRenameRefactoring(iFile, ParseJavaFile.getCompilationUnit(iFile));
		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("extract method");
			ExtractMethodRefactoringPerform.performExtractMethodRefactoring(iFile,
					ParseJavaFile.getCompilationUnit(iFile));
		}
//		if (SelectRefactoring.selectRefactoringNumber() == 1) {
//			System.out.println("inline method");
//			InlineMethodRefactoringPerform.performInlineMethodRefactoring(iFile,
//					ParseJavaFile.getCompilationUnit(iFile));
//		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("extract variable");
			ExtractVariableRefactoring.performExtractVariableRefactoring(iFile);
		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("inline variable");
			InlineVariableRefactoringPerform.performInlineVariableRefactoring(iFile,
					ParseJavaFile.getCompilationUnit(iFile));
		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("extract constant refactoring");
			ExtractConstantRefactoringPerform.performExtractConstantRefactoring(iFile,
					ParseJavaFile.getCompilationUnit(iFile));
		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("inline constant refactoring");
			InlineConstantRefactoringPerform.performInlineConstantRefactoring(iFile,
					ParseJavaFile.getCompilationUnit(iFile));
		}
		if (SelectRefactoring.selectRefactoringNumber() == 1) {
			System.out.println("选择的Encapsulate field");
			EncapsulateFieldRefactoring.performEncapsulateFieldRefactoring(iFile);
		}
//		if (SelectRefactoring.selectRefactoringNumber() == 1) {
//			System.out.println("inverse Encapsulate field");
//			InverseEncapsulateFieldRefactoringPerform.performInverseEncapsulateFieldRefactoring(iFile,
//					ParseJavaFile.getCompilationUnit(iFile));
//		}
//		if (SelectRefactoring.selectRefactoringNumber() == 1) {
//			System.out.println("Convert variable to field");
//			ConvertVariableToFieldRefactoring.performRefactoring(iFile, ParseJavaFile.getCompilationUnit(iFile));
//		}

//		if (SelectRefactoring.selectRefactoringNumber() < 10) {
//			System.out.println("Convert field to variable");
//			ConvertFieldToVariableRefactoringPerform.performFieldToVariableRefactoring(iFile, compilationUnit);
//		}

//11种
//		if (integer == Constant.RENAME_REFACTORING) {// rename
//			System.out.println("选择的重命名重构");
//			peformRenameRefactoring(iFile, compilationUnit);
//		} else if (integer == Constant.EXTACT_METHOD_REFACTORING) {// extract
//			System.out.println("选择的提取method"); // method
//			ExtractMethodRefactoringPerform.performExtractMethodRefactoring(iFile, compilationUnit);
//		} else if (integer == Constant.INLINE_METHOD_REFACTORING) {// inline method
//			System.out.println("选择的inline method");
//			InlineMethodRefactoringPerform.performInlineMethodRefactoring(iFile, compilationUnit);
//		} else if (integer == Constant.EXTACT_VARIABLE_REFACTORING) {
//			// extract variable
//			System.out.println("选择的提取变量");
//			ExtractVariableRefactoring.performExtractVariableRefactoring(iFile);
//		} else if (integer == Constant.INLINE_VARIABLE_REFACTORING) {
//			System.out.println("选择的inline variable");
//			InlineVariableRefactoringPerform.performInlineVariableRefactoring(iFile, compilationUnit);
//		} else if (integer == Constant.EXTRACT_CONSTANT_REFACTORING) {
//			System.out.println("extract constant refactoring");
//			ExtractConstantRefactoringPerform.performExtractConstantRefactoring(iFile, compilationUnit);
//		} else if (integer == Constant.INLINE_CONSTANT_REFACTORING) {
//			InlineConstantRefactoringPerform.performInlineConstantRefactoring(iFile, compilationUnit);
//		} else if (integer == Constant.ENCAPSULATE_FIELD_REFACTORING) {// encapsulate field
//			System.out.println("选择的Encapsulate field");
//			EncapsulateFieldRefactoring.performEncapsulateFieldRefactoring(iFile);
//		} else if (integer == Constant.INVERSE_ENCAPSULATE_FIELD_REFACTORING) {
//		}
//		else if (integer == Constant.CONVERT_ANONYMOUS_TO_NESTED_REFACTORING) {
//			System.out.println("anonynous to nested");
//			ConvertAnonymousToNestedRefactoringPerform.performConvertAnonymousToNestedRefactoring(iFile,
//					compilationUnit);
//		} 
//		else if (integer == Constant.CHANGE_METHOD_SIGNATURE_REFACTORING) {
//			System.out.println("change method signature refactoring");
//			ChangeMethodSignatureRefactoring.performChangeMethodSignature(iFile, compilationUnit);
//		} 
//		else if (integer == Constant.CONVERT_VARIABLE_TO_FIELD_REFACTORING) {
//			System.out.println("CONVERT_VARIABLE_TO_FIELD_REFACTORING");
//			ConvertVariableToFieldRefactoring.performRefactoring(iFile, compilationUnit);
//		}

//		else if (integer == Constant.INTRODUCE_FACTORY_REFACTORING) {
//			IntroduceFactoryRefactoringPerform.performIntroduceFactoryRefactoring(iFile, compilationUnit);
//		} else if (integer == Constant.INTRODUCE_PARAMETER_REFACTORING) {
//			IntroduceParameterRefactoringPerform.performIntroduceParameter(iFile, compilationUnit);
//		}
	}

	private static void peformRenameRefactoring(IFile iFile, CompilationUnit cUnit) throws CoreException {
		int num = SelectRefactoring.randomRenameType();
		if (num == Constant.RENAME_TYPE && num == Constant.RENAME_METHOD) {// rename class
			System.out.println("选择的类重命名重构");
			RenameTypeRefactoring.performRenameTypeRefactoring(iFile);
		} else if (num == Constant.RENAME_METHOD) {// rename method
			System.out.println("选择的方法重命名重构");
			RenameMethodRefactoring.performRenameMethodRefactoring(iFile);
		} else if (num == Constant.RENAME_FIELD) {// rename field
			System.out.println("选择的字段重命名重构");
			RenameFieldRefactoring.performFieldRenameRefactoring(iFile);
		} else if (num == Constant.RENAME_VARIABLE && num == Constant.RENAME_METHOD) {// rename variable
			System.out.println("选择的局部变量重命名重构");
			RenameVariableRefactoring.performVariableRenameRefactoring(iFile, cUnit);
		}
	}

	public static String getName() {
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();

			if (firstElement instanceof IAdaptable) {
				IProject project = ((IAdaptable) firstElement).getAdapter(IProject.class);
				if (project != null) {
					System.out.println("Clicked project name: " + project.getName());
					return project.getName();
				}
			}
		}
		return null;
	}
}
