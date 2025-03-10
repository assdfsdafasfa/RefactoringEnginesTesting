package refactoring.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.internal.preferences.StringPool;
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
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;

import ast.Entity.Entity;
import refactoring.Info.ExpressionInfo;
import refactoring.Info.MoveMethodInfo;
import refactoring.file.parse.GetProject;
import refactoring.read.data.ExtractVariableData;
import refactoring.read.data.MoveMethodData;

public class MoveMethodHandler {
	static List<IMethod> methods = new ArrayList<>();
	public static void handlerMoveMethod(ExecutionEvent event){
		String jsonName = "IEMS_Java_Test_ExtactVariable";
		String handlerProjectName = "IEMS_Java_Test";
		List<MoveMethodInfo> moveMethodInfos = MoveMethodData.readJson(jsonName);
		IJavaProject javaProject;
		try {
			javaProject = GetProject.getJavaProject(handlerProjectName);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			for (int i = 0; i < moveMethodInfos.size(); i++) {
				MoveMethodInfo moveMethodInfo = moveMethodInfos.get(i);
				IPath path = Path.fromOSString(moveMethodInfo.getPath().replaceAll("IDEA", "Eclipse"));
				IFile file = workspace.getRoot().getFileForLocation(path);
				IJavaElement element = JavaCore.create(file);
				ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
				if (iCompilationUnit != null) {
					IType[] allType = iCompilationUnit.getAllTypes();
					//寻找正确的IType
					if(allType.length>0) {
						IType iType = getType(allType, moveMethodInfo.getTypeName());
						if(iType != null) {
							//获取选定的Imethod
							IMethod iMethod =  setMethods(iType, moveMethodInfo.getMethod(), moveMethodInfo.getParameterType());
							if(iMethod != null) {
								Entity methodEntity = new Entity(iMethod);
								MethodDeclaration methodDeclaration = (MethodDeclaration) methodEntity.getAssociatedNode();
								CompilationUnit compilationUnit = (CompilationUnit) methodEntity.getUnit();	
						        MoveInstanceMethodProcessor processor= new MoveInstanceMethodProcessor(iMethod, JavaPreferencesSettings.getCodeGenerationSettings(javaProject));
						        Refactoring ref= new MoveRefactoring(processor);
								RefactoringStatus result1= ref.checkInitialConditions(new NullProgressMonitor());
								RefactoringStatus result2= ref.checkFinalConditions(new NullProgressMonitor());
								if (!result1.isOK() || !result2.isOK()) {
									//不能refactoring
								}else {	
									try {
										final PerformRefactoringOperation op = new PerformRefactoringOperation(ref, 2);
										op.run(new NullProgressMonitor());
									} catch (CoreException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
			}catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	public static IType getType(IType[] iTypes, String typeName) {
		for(IType type : iTypes) {
			if(typeName.equals(type.getFullyQualifiedName())) {
				return type;
			}
		}
		return null;
	}
	private static IMethod setMethods(IJavaElement element, String methodName, String[] paraTypes){
		if(element instanceof IType){
			IType type = (IType) element;
			IMethod[] approches = null;
			try {
				approches = type.getMethods();
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(IMethod method : approches) {
				String[] currMethod =  method.getParameterTypes();
				if(currMethod.equals(paraTypes) && method.getElementName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}
}
