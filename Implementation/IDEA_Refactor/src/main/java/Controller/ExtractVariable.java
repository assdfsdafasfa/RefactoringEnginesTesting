package Controller;

import Utils.Constant;
import Utils.SaveFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.refactoring.introduceVariable.IntroduceVariableBase;
import com.intellij.refactoring.introduceVariable.IntroduceVariableHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractVariable implements Runnable {

    private PsiExpression expr;
    private PsiFile psiFile;
    private Document document;
    private String filePath;
    private Editor editor;
    private Project project;
    private boolean success = false;
    // 变量名称
    private String variableName;
    private List<PsiExpression> successfullyRefactoredExpressions = new ArrayList<>(); // 存储成功重构的表达式
    private  Map<String, VirtualFile> virtualFileMap;

    private String className;

    private int i;

    public ExtractVariable(Map<String, VirtualFile> virtualFileMap, Project project, PsiFile psiFile, int offset, PsiElement expression, String classMame, int i) {
        this.project = project;
        this.psiFile = psiFile;
        this.filePath = psiFile.getVirtualFile().getPath();
        document = Editors.getCurrentDocument(filePath);
        editor = Editors.createSourceEditor(project, filePath, "java", false);
        expr = findPsiExpression(offset, expression);
        this.virtualFileMap = virtualFileMap;
        this.className = classMame;
        this.i = i;
    }

    public PsiExpression findPsiExpression(int offset, PsiElement expression) {
        PsiElement element = psiFile.findElementAt(offset);
        while (element != null) {

            if (element.equals(expression)) {
                break;
            }

            if (element.getParent() != null) {
                element = element.getParent();
            } else {
                break;
            }
        }
        if (element instanceof PsiExpression) {
            return (PsiExpression) element;
        }
        return null;
    }

    public void extractVariable() {
        if (expr == null) {
            return;
        }
        try {
            IntroduceVariableHandler handler = new IntroduceVariableHandler();
            Map<String, IntroduceVariableBase.JavaReplaceChoice> choices = handler.getPossibleReplaceChoices(project, expr);
            IntroduceVariableBase.JavaReplaceChoice choice = IntroduceVariableBase.JavaReplaceChoice.NO;
            int maxReplacedExpr = 0;
            for (Map.Entry<String, IntroduceVariableBase.JavaReplaceChoice> e : choices.entrySet()) {
                IntroduceVariableBase.JavaReplaceChoice c = e.getValue();
                if (choice == IntroduceVariableBase.JavaReplaceChoice.NO && !e.getKey().contains("will change")) {
                    choice = c;
                }
                if (e.getKey().contains("all occurrences but write")) {
                    choice = c;
                    break;
                } else if (e.getKey().contains("occurrences") && !e.getKey().contains("will change")) {
                    String regEx = "[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(e.getKey());
                    int v = Integer.parseInt(m.replaceAll("").trim());
                    if (v >= maxReplacedExpr) {
                        choice = c;
                        maxReplacedExpr = v;
                    }
                }
            }
            try{
                // 尝试执行重构操作
                handler.invokeImpl(project, expr, null, choice, editor);
                setSuccess(true);
                String savePath = Constant.FILE_SAVE_POSTION + project.getName() + "\\" + Constant.EXTRACT_VARIABLE + "\\"+ i +"\\"+ className +".java";
                SaveFile.saveCurrentFileToNewFile(virtualFileMap, project,filePath,savePath);
            } catch (Exception e){
                String savePath = Constant.FILE_SAVE_POSTION + project.getName() + "\\" + Constant.EXTRACT_VARIABLE + "\\"+ i +"\\"+ className +".java";
                SaveFile.saveCurrentFileToNewFile(virtualFileMap, project,filePath,savePath);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            System.err.println("Exception occurred while processing expression: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public String getVariableName() {
        return variableName;
    }
    @Override
    public void run() {
//        // successfullyRefactoredExpressions
//        for (PsiExpression successfullyRefactoredExpression : successfullyRefactoredExpressions) {
//            String variableName = successfullyRefactoredExpression.getText();
//            System.out.println("Successfully refactored expression: " + variableName);
//        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
