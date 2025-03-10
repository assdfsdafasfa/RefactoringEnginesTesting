package refactoring.rename.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameNonVirtualMethodProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import json.NativeFieldJson;
import refactoring.Info.RenameMethodInfo;
import refactoring.file.parse.SaveFile;

public class RenameMethodData {
	 public static void main(String[] args) {
		 String projectNameString = "";
		 List<RenameMethodInfo> renameMethodInfos = readJson(projectNameString);
		 System.out.println("renameInfo:"+renameMethodInfos.size());
	 }
	public static List<RenameMethodInfo> readJson(String projectNameString) {
		List<RenameMethodInfo> renameMethodInfos = new ArrayList<>();
		Path jsonFilePath = Paths.get("D:/IDEA_build/saveFile/Json/"+ projectNameString +".json");
		try {
			String jsonContent = new String(Files.readAllBytes(jsonFilePath));
			JSONArray jsonArray = JSONObject.parseArray(jsonContent);
			List<Map<String, Object>> jsonListMap = JSON.parseObject(jsonContent,
					new TypeReference<List<Map<String, Object>>>() {
					});
			for(int i=0;i<jsonListMap.size();i++) {
				Map<String, Object> temp = jsonListMap.get(i); 
				int no = (int) temp.get("no");
				String projectName = temp.get("projectName").toString();
				String filePath = temp.get("filePath").toString();
				filePath = filePath.replaceAll("IDEA", "Eclipse");
				String methodName = temp.get("methodName").toString();
				int offset = (int) temp.get("offset");
				int length = (int) temp.get("length");
				RenameMethodInfo renameMethodInfo = new RenameMethodInfo(no, methodName, projectName, filePath, offset, length);
				renameMethodInfos.add(renameMethodInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return renameMethodInfos;
	}
}


//private static void testMethodRenameRefactoring(ExecutionEvent event) throws ExecutionException {
//	List<IMethod> methods = new ArrayList<>();
//	List<IType> types = new ArrayList<>();
//	IJavaProject javaProject;
//	try {
//		javaProject = getJavaProject("Test");
//		// 获取method
//		for (int a = 0; a < javaFiles.size(); a++) {
//			IFile file = javaFiles.get(a);
//			IJavaElement element = JavaCore.create(file);
//			ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
//			IType[] allTypes = iCompilationUnit.getAllTypes();
//			for (IType type : allTypes) {
//				getMethodInfo(type);
//			}
//		}
//
//		// 对每个method做refactoring
//		for (int n = 0; n < Math.min(methodInfos.size(), 100); n++) {
//			RenameMethodInfo renameElemInfo = methodInfos.get(n);
//			IMethod iMethod = renameElemInfo.getOldName();
//			RenameMethodProcessor processor = new RenameNonVirtualMethodProcessor(iMethod);
//			processor.setNewElementName("newMethodName");
//			processor.setUpdateReferences(true);
//			RenameRefactoring refactoring = new RenameRefactoring(processor);
//			RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
//			System.out.println("RefactoringStatus:" + status);
//			CompositeChange compositeChange = new CompositeChange("Undoable Change");
//			Change change = refactoring.createChange(new NullProgressMonitor());
//			if (status.isOK()) {	
//				change.perform(new NullProgressMonitor()); // 执行重构
////	            refactoring.createChange(new NullProgressMonitor()).perform(new NullProgressMonitor());		
//				compositeChange.add(change);		
//				System.out.println("change:"+change);
//				System.out.println("Method renamed successfully.");
//			} else {
//				System.out.println("Error: " + status.getMessageMatchingSeverity(RefactoringStatus.ERROR));
//			}
//
//			// 获取 ICompilationUnit
//			ICompilationUnit compilationUnit = iMethod.getCompilationUnit();
//			IFile file = (IFile) compilationUnit.getResource();
//			String fileContent = compilationUnit.getSource();
//			SaveFile.saveContentToFile("D:\\IDEA_build\\saveFile\\" + renameElemInfo.getProjectName() + "\\"
//					+ iMethod.getElementName() + ".java", fileContent);
//			System.out.println("文件保存完成");
//			
////			executeUndo(file, compositeChange, change);
//
//			 if (file.exists()) {
//		            try {
//		                // Create a simple TextFileChange for demonstration
//		                TextFileChange textFileChange = new TextFileChange("Sample Change", file);
//		                undoRefactoringHistory(file);
//
//		                // Refresh the file to reflect the changes
//		                file.refreshLocal(IFile.DEPTH_INFINITE, new NullProgressMonitor());
//		            } catch (Exception e) {
//		                e.printStackTrace();
//		            }
//		        } else {
//		            System.out.println("File does not exist.");
//		        }
//		    }
//
//	} catch (CoreException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
//}
