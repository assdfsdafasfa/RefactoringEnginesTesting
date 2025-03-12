package refactoring.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.Workbench;

import refactoring.convert.test.ConvertRuleTest;

public class SampleHandler extends AbstractHandler {
	private JavaEditor editor;
	private ITextSelection selection;
	private CompilationUnit astRoot;
	protected static final String TEST_OUTPUT_INFIX = "D:\\eclipse\\workspace\\Dataset\\";
	public static IFile testCaseFile = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelectionService selectionService = window.getSelectionService();
		selectionService.addSelectionListener(new ISelectionListener() {
			@Override
			public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
				// TODO Auto-generated method stub
				ISelection selection = selectionService.getSelection();
			}
		});

		String pathString = Platform.getLocation().toString();
		ISelection window1 = HandlerUtil.getCurrentSelection(event);
//		IProject project = getProject();

		try {
			handleCommand1(selection, window);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void handleCommand1(ISelection window1, IWorkbenchWindow window) throws Exception {
//		String string = window1.toString();
//		String[] splitStrings = string.split("[, :]");
//		String offset = splitStrings[3];
//		String startLine = splitStrings[7];
//		String length = splitStrings[11];
//		String selectString = splitStrings[15];
		IProject project = getProject();
		if (project != null) {
			List<IFile> javaFiles = getAllJavaFiles(project);

//			for (IFile javaFile : javaFiles) {// 获取测试用例文件
//				if (javaFile.toString().contains(Constant.Rename_TEST_CASE_PATH)) {
//					testCaseFile = javaFile;
//					break;
//				}
//			}

			ConvertRuleTest ruleTest = new ConvertRuleTest();// -------------------------
			ruleTest.run(project, javaFiles, testCaseFile);

		} else {
			System.out.println("No project selected.");
		}
	}

	public static IProject getProject() {
		IProject project = null;
		if (project == null) {
			ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow()
					.getSelectionService();
			ISelection selection = selectionService.getSelection();
			if (selection instanceof IStructuredSelection) {
				Object element = ((IStructuredSelection) selection).getFirstElement();
				if (element instanceof IResource) {
					project = ((IResource) element).getProject();
				} else if (element instanceof PackageFragmentRootContainer) {
					IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
					project = jProject.getProject();
				} else if (element instanceof IJavaElement) {
					IJavaProject jProject = ((IJavaElement) element).getJavaProject();
					project = jProject.getProject();
				}
			}
		}
		return project;
	}

	public static IJavaProject findJavaProject(String projectName) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < projects.length; ++i) {
			if (JavaCore.create(projects[i]).getPath().lastSegment().contains(projectName)) {
				return JavaCore.create(projects[i]);
			}
		}
		return null;
	}

	public List<IFile> getAllJavaFiles(IProject project) {
		List<IFile> javaFiles = new ArrayList<>();
		try {
			project.accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE && "java".equals(resource.getFileExtension())) {
						javaFiles.add((IFile) resource);
					}
					return true;
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return javaFiles;
	}

}
