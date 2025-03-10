package Action;

import Controller.ExtractMethod;
import Model.ExpressionInfo;
import Model.ExtractMethodInfo;
import SaveInfo.VariableInfo;
import Utils.ParseFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static Utils.SaveFile.getVirtualFile;

public class ExtractMethodAction extends AnAction {
    private List<ExtractMethodInfo> allExpressions = new ArrayList<>();
    List<ExtractMethodInfo> variableInfoList = new ArrayList<>();



    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        ExtractMethodHandler extractMethodHandler = new ExtractMethodHandler();
        Project project = e.getProject();
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor editor = fileEditorManager.getSelectedTextEditor();
        if (project != null) {

            VirtualFile projectBaseDir = project.getBaseDir();

            List<PsiFile> javaFiles = new ArrayList<>();
            ParseFile.collectJavaFiles(javaFiles, projectBaseDir, project);
            Map<String, VirtualFile> virtualFileMap = getVirtualFile(project);

            for (PsiFile psiFile : javaFiles) {// find extract range
                findExtactMethod(psiFile, project);
            }
            for(ExtractMethodInfo methodInfo : allExpressions){
                ExtractMethod extractMethod = new ExtractMethod(methodInfo);
                try {
                    extractMethod.performExtractMethod(editor,methodInfo.getPsiFile(),project,methodInfo.getOffset(),methodInfo.getLength());
                } catch (PrepareFailedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void findExtactMethod(PsiFile psiFile, Project project) {
        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiMethod) {
                    PsiMethod psiMethod = (PsiMethod) element;
                    String filePath = psiFile.getVirtualFile().getPath();
                    String methodName = psiMethod.getName();
                    List<String> methodParameterTypes = getMethodSignature((PsiMethod) element);
                    String className = psiMethod.getContainingClass().getName();
                    int startLine = getLineNumber(psiMethod.getBody().getLBrace(), project);
                    int endLine = getLineNumber(psiMethod.getBody().getRBrace(), project);
                    startLine = randomStratLine(startLine, endLine);
                    endLine =  randomEndLine(startLine, endLine);
                    ExtractMethodInfo extractMethodInfo = new ExtractMethodInfo(filePath, psiMethod, psiFile, startLine, endLine, methodParameterTypes);
                    allExpressions.add(extractMethodInfo);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
    }

    public static int getLineNumber(PsiElement psiElement, Project project) {
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiElement.getContainingFile());
        int offset = psiElement.getTextOffset();
        return document.getLineNumber(offset);
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

    public static int randomStratLine(int start, int end){
        int randomNumber = 0;
        if(start<end){
            Random random = new Random();
            randomNumber = random.nextInt(start, end);
        }
        return randomNumber;
    }

    public static int randomEndLine(int start, int end){
        int randomNumber = 0;
        if(start<end){
            Random random = new Random();
            randomNumber = random.nextInt(start, end);
        }
        return randomNumber;
    }
}
