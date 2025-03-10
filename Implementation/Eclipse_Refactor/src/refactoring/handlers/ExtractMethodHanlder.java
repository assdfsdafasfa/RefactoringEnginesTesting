package refactoring.handlers;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.junit.internal.matchers.StacktracePrintingMatcher;

import json.ResultJson;
import refactoring.Info.ExtractMethodInfo;
import refactoring.file.parse.GetProject;
import refactoring.file.parse.SaveFile;
import refactoring.read.data.ExtractMethodData;
import refactoring.undo.UndoHistory;

public class ExtractMethodHanlder {
	String projectName = " ";
	public void readDataInfo(String projectName) throws CoreException {
		List<ExtractMethodInfo> allDataInfo = ExtractMethodData.readJson(projectName);
		IJavaProject javaProject;
		javaProject = GetProject.getJavaProject(projectName);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		for(int i=0;i<allDataInfo.size();i++) {
			boolean isSuccess = false;
			ExtractMethodInfo methodInfo = allDataInfo.get(i);
			IPath path = Path.fromOSString(methodInfo.getPath().replaceAll("IDEA", "Eclipse"));
			IFile file = workspace.getRoot().getFileForLocation(path);
			IJavaElement element = JavaCore.create(file);
			ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
			if (iCompilationUnit != null) {
			   //获取选定的range
			   performExtractMethodRefactoring(allDataInfo.get(i).getOffset(), allDataInfo.get(i).getLength(),allDataInfo.get(i).getMethod(), isSuccess);
			   saveRefactoringData(iCompilationUnit, methodInfo);
			   ResultJson.handleAPIJsonFile(methodInfo.getNo(),isSuccess);//存储是否能重构记录
			   if (file.exists()) {
					try {
						TextFileChange textFileChange = new TextFileChange("Sample Change", file);
						UndoHistory.undoRefactoringHistory(file);
						file.refreshLocal(IFile.DEPTH_INFINITE, new NullProgressMonitor());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("File does not exist");
				}
			}
		}
	}
	
	public void performExtractMethodRefactoring(int start, int length, String methodName, boolean isSuccess) {
		ICompilationUnit unit = null;
		ExtractMethodRefactoring refactoring = new ExtractMethodRefactoring(unit, start, length);
		refactoring.setMethodName(methodName);
		try {
			if (refactoring.checkInitialConditions(new NullProgressMonitor()).isOK()
					&& refactoring.checkFinalConditions(new NullProgressMonitor()).isOK()) {
				System.out.println("执行重构");
				isSuccess = true;
				final PerformRefactoringOperation op= new PerformRefactoringOperation(
						refactoring, 0);
				JavaCore.run(op, new NullProgressMonitor());
			} else {
				System.out.println("不能重构");
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveRefactoringData(ICompilationUnit iCompilationUnit,ExtractMethodInfo extractMethodInfo) throws JavaModelException {
		String fileContent = iCompilationUnit.getSource();
		SaveFile.saveContentToFile(
				"D:\\IDEA_build\\saveFile\\Eclipse\\" + extractMethodInfo.getProjectName()
						+ "\\ExtractVariable\\" + extractMethodInfo.getNo() + "\\" + extractMethodInfo.getTypeName() + ".java",
				fileContent);
	}
}
