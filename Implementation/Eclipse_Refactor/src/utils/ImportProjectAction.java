package utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ImportProjectAction extends Action implements IObjectActionDelegate {
	private ISelection selection;

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	@Override
	public void run(IAction action) {
//		if (selection instanceof IStructuredSelection) {
//			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
//			Object firstElement = structuredSelection.getFirstElement();
//			if (firstElement instanceof IProject) {
//				IProject project = (IProject) firstElement;
//				String projectPath = project.getLocation().toOSString();
		importProject("D:\\AllProject\\Eclipse\\opendis7-source-generator");
		System.out.println("11111");
//			}
//		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	public static void importProject(String projectPath) {
		Display.getDefault().asyncExec(() -> {
			DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
			dialog.setMessage("Select project folder");
			dialog.setFilterPath(projectPath);
//			dialog.setText("123");
			String selectedDirectory = dialog.open();
			if (selectedDirectory != null) {
//				System.err.println("2222");
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IPath location = Path.fromOSString(selectedDirectory);
				IProject project = root.getProject(location.lastSegment());
				if (!project.exists()) {
					try {
//						System.err.println("333");
						project.create(null);
						project.open(null);
						project.refreshLocal(IProject.DEPTH_INFINITE, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}