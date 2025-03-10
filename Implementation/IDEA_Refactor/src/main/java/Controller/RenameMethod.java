package Controller;

import Utils.Constant;
import Utils.SaveFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenameHandler;
import com.intellij.refactoring.rename.RenameHandlerRegistry;
import com.intellij.refactoring.rename.RenameJavaMethodProcessor;
import com.intellij.refactoring.rename.RenameProcessor;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenameHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class RenameMethod implements Runnable{
    private PsiElement psiElement;
    private static PsiFile psiFile;
    private Document document;
    private String filePath;
    private Editor editor;
    private Project project;
    private boolean success = false;
    private  Map<String, VirtualFile> virtualFileMap;
    private int i;
    public RenameMethod(Map<String, VirtualFile> virtualFileMap, Project project, PsiFile psiFile, int offset, int length, String filePath, PsiElement psiElement, int i) {
        this.project = project;
        this.psiFile = psiFile;
        this.filePath = psiFile.getVirtualFile().getPath();
        document = Editors.getCurrentDocument(filePath);
        editor = Editors.createSourceEditor(project, filePath, "java", false);
        this.psiElement = psiElement;
        this.i = i;
        this.virtualFileMap = virtualFileMap;
    }

    public void renameMethodRefactoring() {
        if(canRename(psiElement) ){
            RenameProcessor renameProcessor = new RenameProcessor(project, psiElement,((PsiMethod) psiElement).getName()+"New1",true,true);

            renameProcessor.run();
            setSuccess(true);
        }else{
            setSuccess(false);
            System.out.println("not method refactoring");
        }
        String psiClass = ((PsiMethod) psiElement).getContainingClass().getName();
        String savePath = Constant.FILE_SAVE_POSTION + project.getName() + "\\" + Constant.RENAME_METHOD + "\\"+ i +"\\"+ psiClass +".java";
        SaveFile.saveCurrentFileToNewFile(virtualFileMap, project,filePath,savePath);
        System.out.println("i:"+i+"-保存成功");
    }

    @Override
    public void run() {

    }
    private static boolean canRename(PsiElement element) {
        Project project = element.getProject();
        RenameJavaMethodProcessor renameJavaMethodProcessor = new RenameJavaMethodProcessor();
        if(renameJavaMethodProcessor.canProcessElement(element)){
            return true;
        }
        return false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
