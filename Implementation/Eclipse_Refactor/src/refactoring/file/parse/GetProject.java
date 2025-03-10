package refactoring.file.parse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class GetProject {
	public static IJavaProject getJavaProject(String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project.exists() && project.isOpen()) {
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject != null && javaProject.exists()) {
				return javaProject;
			}
		}
		throw new CoreException(null);
	}
}
