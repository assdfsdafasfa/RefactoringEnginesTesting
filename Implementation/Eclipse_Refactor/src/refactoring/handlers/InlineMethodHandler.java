package refactoring.handlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.internal.corext.refactoring.code.InlineMethodRefactoring;
import org.eclipse.jdt.internal.corext.refactoring.util.RefactoringASTParser;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;

import refactoring.Info.InlineMethodInfo;
import refactoring.file.parse.GetProject;
import refactoring.file.parse.SaveFile;
import refactoring.read.data.InlineMethodData;
import refactoring.undo.UndoHistory;

public class InlineMethodHandler {

	public static void handlerInlineMethod(ExecutionEvent event) {
		String jsonName = "IEMS_Java_Test_ExtactVariable";
		String handlerProjectName = "IEMS_Java_Test";
		List<InlineMethodInfo> inlineMethodInfos = InlineMethodData.readJson(jsonName);
		IJavaProject javaProject;
		List<InlineMethodInfo> saveMethodInfos = new ArrayList<>();
		try {
			javaProject = GetProject.getJavaProject(handlerProjectName);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			for (int i = 0; i < inlineMethodInfos.size(); i++) {
				InlineMethodInfo methodInfo = inlineMethodInfos.get(i);
				IPath path = Path.fromOSString(methodInfo.getPath().replaceAll("IDEA", "Eclipse"));
				IFile file = workspace.getRoot().getFileForLocation(path);
				IJavaElement element = JavaCore.create(file);
				ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
				if (iCompilationUnit != null) {
					IType[] allType = iCompilationUnit.getAllTypes();
					if (allType.length > 0) {
						IType iType = getType(allType, methodInfo.getTypeName());
						if (iType != null) {
							IMethod iMethod = setMethods(iType, methodInfo.getMethod(), methodInfo.getParameterType());
							if (iMethod != null) {
								boolean isSuccess = false;
								InlineMethodRefactoring refactoring = InlineMethodRefactoring.create(iCompilationUnit,
										new RefactoringASTParser(AST.getJLSLatest()).parse(iCompilationUnit, true),
										iMethod.getNameRange().getOffset(), iMethod.getNameRange().getLength());
								RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
								final PerformRefactoringOperation op = new PerformRefactoringOperation(refactoring, 3);
								JavaCore.run(op, new NullProgressMonitor());

								InlineMethodInfo savExpressionInfo = new InlineMethodInfo(methodInfo.getNo(),
										isSuccess);
								saveMethodInfos.add(savExpressionInfo);

								String fileContent = iCompilationUnit.getSource();
								SaveFile.saveContentToFile(
										"D:\\IDEA_build\\saveFile\\Eclipse\\" + methodInfo.getProjectName()
												+ "\\InlineMethod\\" + methodInfo.getNo() + "\\" + iType + ".java",
										fileContent);

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
				}
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:/IDEA_build/saveFile/Json/"
					+ inlineMethodInfos.get(0).getProjectName() + "_Eclipse_ExtactVariable" + ".txt"))) {
				for (InlineMethodInfo line : saveMethodInfos) {
					writer.write("no:" + line.getNo() + " " + "success:" + line.isSuccess());
					writer.newLine(); // 添加换行符
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static IType getType(IType[] iTypes, String typeName) {
		for (IType type : iTypes) {
			if (typeName.equals(type.getFullyQualifiedName())) {
				return type;
			}
		}
		return null;
	}

	private static IMethod setMethods(IJavaElement element, String methodName, String[] paraTypes) {
		if (element instanceof IType) {
			IType type = (IType) element;
			IMethod[] approches = null;
			try {
				approches = type.getMethods();
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (IMethod method : approches) {
				String[] currMethod = method.getParameterTypes();
				if (currMethod.equals(paraTypes) && method.getElementName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}
}
