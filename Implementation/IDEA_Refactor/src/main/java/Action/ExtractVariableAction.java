package Action;

import Controller.ExtractVariable;
import Model.ExpressionInfo;
import SaveInfo.VariableInfo;
import Utils.ParseFile;
import Utils.ShuffleList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static Utils.SaveFile.getVirtualFile;

public class ExtractVariableAction extends AnAction {

    private List<ExpressionInfo> allExpressions = new ArrayList<>();

    List<VariableInfo> variableInfoList = new ArrayList<>();

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();

        if (project != null) {

            VirtualFile projectBaseDir = project.getBaseDir();

            List<PsiFile> javaFiles = new ArrayList<>();
            ParseFile.collectJavaFiles(javaFiles, projectBaseDir, project);
            Map<String, VirtualFile> virtualFileMap = getVirtualFile(project);
            for (PsiFile psiFile : javaFiles) {
                findMethod(psiFile);
            }
            Set<Integer> randomData = ShuffleList.randomExpressionList(allExpressions); // random select data
//            for (int i = 0; i < Math.min(allExpressions.size(), 1000); i++)
            for(Integer i:randomData){
                ExpressionInfo expression = allExpressions.get(i);
                int offset = expression.getOffset();
                String filePath = expression.getPsiFile().getVirtualFile().getPath();
//                System.out.println("PATH: " + filePath);
                System.out.println("EXPRESSION: " + expression.getExp());
                ExtractVariable extractVariable = new ExtractVariable(virtualFileMap, project, expression.getPsiFile(), offset, expression.getExp(), expression.getClassName(), i);
                extractVariable.extractVariable();
                VariableInfo variableInfo = new VariableInfo();
                variableInfo.setNo(i);
                variableInfo.setProjectName(project.getName());
                variableInfo.setExpressionName(expression.getExp().getText());
                variableInfo.setOffset(offset);
                variableInfo.setLength(expression.getLength());
                variableInfo.setFilePath(filePath);
                variableInfo.setSuccess(extractVariable.isSuccess());
                variableInfo.setMethodName(expression.getMethodName());
                variableInfo.setMethdoParameterTypes(expression.getMethdoParameterTypes());
                variableInfo.setClassName(expression.getClassName());
                variableInfoList.add(variableInfo);
            }
            // save JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(variableInfoList);

            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/Json/"+project.getName()+"_ExtactVariable"+".json")) {
                writer.write(json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//                if (psiFile != null && !psiFile.getName().equals("DummyHolder")) {
//                    System.out.println("psiFile::::" + psiFile);
//                    System.out.println(psiFile.getVirtualFile().getPath());
//                    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
//                    if (editor != null) {
//                        ExtractVariable extractVariable = new ExtractVariable(project, psiFile, offset, expression);
//                        extractVariable.extractVariable();
//                    } else {
//
//                        System.err.println("Cannot obtain valid editor for PsiFile: " + psiFile.getName());
//                    }
//                } else {
//
//                    System.err.println("PsiFile is null for expression: " + expression);
//                }
        }
    }

    private void findExpressions(String filePath, PsiFile psiFile, PsiMethod psiMethod, String  methodName, List<String> methodParameterTypes, String className) {

        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiExpression) {
                    PsiElement exp = element;
                    int offset = element.getTextOffset();
                    int length = element.getTextLength();
                    ExpressionInfo myExpression = new ExpressionInfo(filePath, exp, psiFile, offset, length, methodName, methodParameterTypes, className);
                    allExpressions.add(myExpression);
                }
                element.acceptChildren(this);
            }
//            private boolean isExpression(PsiElement element) {
//                return element instanceof PsiMethodCallExpression ||
//                       element instanceof PsiConditionalExpression ||
//                        element instanceof PsiLambdaExpression ||
//                        element instanceof PsiAssignmentExpression ||
//                        element instanceof PsiCallExpression ||
//                        element instanceof PsiTypeCastExpression ;
//            }

        };
        // PSI
        psiMethod.accept(visitor);
    }
    private void findMethod(PsiFile psiFile) {
        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiMethod) {
                    String filePath = psiFile.getVirtualFile().getPath();
                    String methodName = ((PsiMethod) element).getName();
                    List<String> methodParameterTypes = getMethodSignature((PsiMethod) element);
                    String className = ((PsiMethod) element).getContainingClass().getName();
                    findExpressions(filePath,psiFile,(PsiMethod) element,methodName, methodParameterTypes, className);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
    }

    private static List<String> getMethodSignature(PsiMethod psiMethod) {
        if (psiMethod == null) {
            return null;
        }
        List<String> methodParameterTypes = getMethodParameterTypes(psiMethod);
        return methodParameterTypes;
    }

    private static List<String> getMethodParameterTypes(PsiMethod psiMethod) {

        PsiParameterList parameterList = psiMethod.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();
        List<String> parameterTypes = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            PsiParameter parameter = parameters[i];
            String parameterType = parameter.getType().toString();
            int pos = parameterType.indexOf(":");
            if(pos!=-1) {
                parameterType = parameterType.substring(pos+1);
            }
            parameterTypes.add(parameterType);
        }
        return parameterTypes;
    }
}
