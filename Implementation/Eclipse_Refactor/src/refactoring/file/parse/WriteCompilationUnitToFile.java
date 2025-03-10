package refactoring.file.parse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;

public class WriteCompilationUnitToFile {
	public static String writeDataToFile(ICompilationUnit iCompilationUnit, String projectName, String fileName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("YourProjectName");
		try {
			String targetPath = "D:\\eclipse\\workspace\\Dataset\\"+projectName;
			IFolder folder = project.getFolder(targetPath.substring(0, targetPath.lastIndexOf("\\")));
			IFile file = folder.getFile(fileName+".java");
			 if (!folder.exists()) {
	                createFolder(folder);
	            }
			 if (!file.exists()) {
	                file.create(new ByteArrayInputStream(iCompilationUnit.getSource().getBytes()), true, null);
	            } else {
	                file.setContents(new ByteArrayInputStream(iCompilationUnit.getSource().getBytes()), true, false, null);
	         }
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	 private static void createFolder(IFolder folder) throws CoreException {
	        if (folder.getParent() instanceof IFolder) {
	            createFolder((IFolder) folder.getParent());
	        }
	        if (!folder.exists()) {
	            folder.create(true, true, null);
	        }
	    }
	
	protected static final Charset ENCODING= StandardCharsets.UTF_8;
	private static IJavaProject getJavaProject(String projectName) throws CoreException {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        if (project.exists() && project.isOpen()) {
            IJavaProject javaProject = JavaCore.create(project);
            if (javaProject != null && javaProject.exists()) {
                return javaProject;
            }
        }
        throw new CoreException(null);
    }

    private static ICompilationUnit getCompilationUnit(IJavaProject javaProject, String sourceFilePath) throws JavaModelException {
        IJavaElement javaElement = javaProject.findElement(new Path(sourceFilePath));
        if (javaElement instanceof ICompilationUnit) {
            return (ICompilationUnit) javaElement;
        }
        return null;
    }

    private static String formatCode(String code) {
        CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
        TextEdit textEdit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, code, 0, code.length(), 0, null);
        if (textEdit != null) {
            try {
                textEdit.apply(new Document(code));
//                code = textEdit.getDocument().get();
            } catch (MalformedTreeException | BadLocationException e) {
                e.printStackTrace();
            }
        }
        return code;
    }

    private static void writeCodeToFile(String code, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(code);
            System.out.println("Code written to file: " + outputFile.getAbsolutePath());
        }
    }
    
	public String getFileContents(String fileName) throws IOException {
		return getContents(getFileInputStream(fileName));
	}
	public static String getContents(IFile file) throws IOException, CoreException {
		return getContents(file.getContents());
	}
	public static String getContents(InputStream in) throws IOException {
		StringBuilder sb= new StringBuilder(300);
		try (BufferedReader br= new BufferedReader(new InputStreamReader(in, ENCODING))){
			int read= 0;
			while ((read= br.read()) != -1)
				sb.append((char) read);
		}
		return sb.toString();
	}
	public static InputStream getFileInputStream(String fileName) throws IOException {
		return getTestResourceStream(fileName);
	}
	
	public static InputStream getTestResourceStream(String fileName) throws IOException {
		IPath path= new Path("resources").append(fileName);
		URL url= new URL(getBundle().getEntry("/"), path.toString());
		return url.openStream();
	}
	public final static Bundle getBundle() {
//		ClassLoader cl = getClass().getClassLoader();
//		if (cl instanceof BundleReference)
//			return ((BundleReference) cl).getBundle();
		return null;
	}
}
