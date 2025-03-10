package refactoring.handlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.util.IClassFileDisassembler;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringExecutionHelper;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.BaseNewWizardMenu;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import refactoring.Info.ExpressionInfo;
import refactoring.Info.RenameMethodInfo;
import refactoring.file.parse.CompilationFile;
import refactoring.file.parse.GetProject;
import refactoring.file.parse.RecordTime;
import refactoring.file.parse.SaveFile;
import refactoring.read.data.ExtractVariableData;
import refactoring.rename.data.RenameMethodData;
import refactoring.undo.UndoHistory;
import refactoring.visitor.EclipseExtractTempRefactoring;

public class ExtractVariableHandler {
	public static ArrayList<Integer> extractList;
    static List<String>  noFindList = new ArrayList<>();
    static List<ExpressionInfo> saveExpression = new ArrayList<>();  
	public static void handlerExtractVariable(ExecutionEvent event){
		String jsonName = "IEMS_Java_Test_ExtactVariable";
		String handlerProjectName = "IEMS_Java_Test";
		List<ExpressionInfo> exprssionInfos = ExtractVariableData.readJson(jsonName);
		IJavaProject javaProject;
		try {
			javaProject = GetProject.getJavaProject(handlerProjectName);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			for (int i = 0; i < exprssionInfos.size(); i++) {
				ExpressionInfo expressionInfo = exprssionInfos.get(i);
				IPath path = Path.fromOSString(expressionInfo.getPath().replaceAll("IDEA", "Eclipse"));
				IFile file = workspace.getRoot().getFileForLocation(path);
				IJavaElement element = JavaCore.create(file);
				ICompilationUnit iCompilationUnit = (ICompilationUnit) element;
				if (iCompilationUnit != null) {
					IType[] allType = iCompilationUnit.getAllTypes();
					if (allType.length > 0) {
						String iType = allType[0].getElementName();
						CompilationUnit astRoot = CompilationFile.parse(iCompilationUnit);
						Expression modifierExpression = extractExpressionsFromIMethod(astRoot,
								expressionInfo.getMethodName(), expressionInfo.getMethdoParameterTypes(),
								expressionInfo.getExpression());

						if (modifierExpression != null) {
							boolean isSuccess = false;   
							int offset = modifierExpression.getStartPosition();
							int length = modifierExpression.getLength();
							if (extractedEclipseApproach(astRoot, offset, length, "newName")) {
								isSuccess = true;
								System.out.println("重构成功");
							} else {
								System.out.println("重构失败");
							}
							ExpressionInfo savExpressionInfo = new ExpressionInfo(expressionInfo.getNo(),isSuccess);
							saveExpression.add(savExpressionInfo);
							// 获取 ICompilationUnit
							String fileContent = iCompilationUnit.getSource();
							SaveFile.saveContentToFile(
									"D:\\IDEA_build\\saveFile\\Eclipse\\" + expressionInfo.getProjectName()
											+ "\\ExtractVariable\\" + expressionInfo.getNo() + "\\" + iType + ".java",
									fileContent);

//					List<java.nio.file.Path> modifierPath = RecordTime.recordFileTime(javaFiles, time1);
//					SaveFile.saveAllModifierFile(modifierPath, "D:\\IDEA_build\\saveFile\\Eclipse\\"
//							+ expressionInfo.getProjectName() + "\\ExtractVariable\\" + expressionInfo.getNo() + "\\");
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
			
            // save JSON
//			JSONArray jsonArray = new JSONArray();
			for(int n=0;n< saveExpression.size();n++) {
//				JSONObject jsonObject = new JSONObject();
//			    try {
//					jsonObject.put("no", saveExpression.get(n).getNo());
//				    jsonObject.put("success", saveExpression.get(n).isSuccess());
//				    jsonArray.put(jsonObject.toString());;
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			    System.out.println("no:"+saveExpression.get(n).getNo());
			    System.out.println("success:"+saveExpression.get(n).isSuccess());
			}	

			try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:/IDEA_build/saveFile/Json/"+exprssionInfos.get(0).getProjectName()+"_Eclipse_ExtactVariable"+".txt"))) {
	            for (ExpressionInfo line : saveExpression) {
	                writer.write("no:"+line.getNo()+" "+"success:"+line.isSuccess());
	                writer.newLine(); // 添加换行符
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
//            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/Json/"+exprssionInfos.get(0).getProjectName()+"_Eclipse_ExtactVariable"+".json")) {
//                writer.write(jsonArray.toString());
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
			
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static boolean extractedEclipseApproach(CompilationUnit compilationUnit, int offset, int length,
			String newName) {
		EclipseExtractTempRefactoring extractTempRefactoring = new EclipseExtractTempRefactoring(compilationUnit,
				offset, length);
		extractTempRefactoring.setTempName(newName);

		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

			RefactoringExecutionHelper helper = new RefactoringExecutionHelper(extractTempRefactoring,
					RefactoringCore.getConditionCheckingFailedSeverity(), RefactoringSaveHelper.SAVE_NOTHING, shell,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			helper.perform(true, true);
		} catch (Exception e) {
			e.printStackTrace();
			extractList = extractTempRefactoring.replaceList;
			return extractTempRefactoring.checkCondition;
		}
		extractList = extractTempRefactoring.replaceList;
		return extractTempRefactoring.checkCondition;
	}

	private static Expression extractExpressionsFromIMethod(CompilationUnit astRoot, String methodName,
		List<String> parameterTypes, String expression) throws JavaModelException {
//		System.out.println("expression:" + expression + "--" + parameterTypes);
		List<MethodDeclaration> methodDeclarations = getMethodDeclaration(astRoot);
		for (int i = 0; i < methodDeclarations.size(); i++) {
			MethodDeclaration methodDeclaration = methodDeclarations.get(i);
			String currentMethodName = methodDeclaration.getName().toString();
			List<SingleVariableDeclaration> parameters = methodDeclaration.parameters();
			List<String> currentParaTypes = new ArrayList<>();
			if (parameterTypes != null) {
				for (int a = 0; a < parameters.size(); a++) {
					currentParaTypes.add(parameters.get(a).getType().toString());
				}
			}
//			System.out.println("currentParaTypes:"+currentParaTypes);
			if (currentMethodName.equals(methodName) && parameterTypes.equals(currentParaTypes)) {
				if (methodDeclaration != null) {
					List<Expression> modifierExp = new ArrayList<>();
					methodDeclaration.accept(new ASTVisitor() {
	                    @Override
	                    public boolean visit(ExpressionStatement expressionStatement) {
//	                        System.out.println("Expression: " + expressionStatement.getExpression());
	                        if(expressionStatement.getExpression().toString().equals(expression)) {
	                        	modifierExp.add(expressionStatement.getExpression());
		                        return true;
	                        }
							return false;
	                    }
	                });
					
					if(modifierExp.size()>0) {
						return modifierExp.get(0);
					}else {
						return null;
					}
				}
			}
		}
		return null;
	}

	public static List<MethodDeclaration> getMethodDeclaration(ASTNode cuu) {
		List<MethodDeclaration> methodDeclarations = new ArrayList<>();
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				methodDeclarations.add(node);
				return true;
			}
		});
		return methodDeclarations;
	}
}
