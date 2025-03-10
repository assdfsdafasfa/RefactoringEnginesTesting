package Controller;

import Utils.Constant;
import Utils.SaveFile;
import com.intellij.lang.Language;
import com.intellij.lang.refactoring.InlineActionHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.actions.InlineAction;
import com.intellij.refactoring.inline.InlineLocalHandler;
import com.intellij.refactoring.inline.InlineMethodHandler;
import com.intellij.refactoring.inline.InlineMethodProcessor;
import com.intellij.refactoring.inline.JavaInlineActionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InlineMethod implements Runnable {
    private PsiElement psiMethod;
    private PsiFile psiFile;
    private Document document;
    private String filePath;
    private Editor editor;
    private Project project;
    private boolean success = false;
    // 变量名称
    private String variableName;
    private List<PsiExpression> successfullyRefactoredExpressions = new ArrayList<>(); // 存储成功重构的表达式
    private Map<String, VirtualFile> virtualFileMap;

    private String className;

    private int offset;

    private int length;
    private int no;
    public InlineMethod(PsiElement psiMethod, int offset, int length, String filePath, int no) {
            this.psiMethod = psiMethod;
            this.offset = offset;
            this.length = length;
            this.filePath = filePath;
            this.no = no;
    }
    public void inlineMethodsTest(){
        InlineMethodHandler inlineMethodHandler = null;
        if(inlineMethodHandler.canInlineElement(psiMethod)){
            InlineMethodHandler.performInline(project, editor, (PsiMethod) psiMethod, false);
            success = true;
        }

        String savePath = Constant.FILE_SAVE_POSTION + project.getName() + "\\" + Constant.INLINE_METHOD + "\\"+ no +"\\"+ className +".java";
        SaveFile.saveCurrentFileToNewFile(virtualFileMap, project,filePath,savePath);
    }
    @Override
    public void run() {

    }
}
