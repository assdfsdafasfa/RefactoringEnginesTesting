package Controller;

import Utils.SaveFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.refactoring.rename.RenameJavaClassProcessor;
import com.intellij.refactoring.rename.RenameJavaMethodProcessor;
import com.intellij.refactoring.rename.RenameProcessor;

public class RenameType {
    private PsiElement psiElement;
    private static PsiFile psiFile;
    private Document document;
    private String filePath;
    private Editor editor;
    private Project project;
    private boolean success = false;
    public RenameType(Project project, PsiFile psiFile, int offset, int length, String filePath, PsiElement psiElement) {
        this.project = project;
        this.psiFile = psiFile;
        this.filePath = psiFile.getVirtualFile().getPath();
        document = Editors.getCurrentDocument(filePath);
        editor = Editors.createSourceEditor(project, filePath, "java", false);
        this.psiElement = psiElement;
    }

    public void renameTypeRefactoring() {
        if(canRename(psiElement) ){
            RenameProcessor renameProcessor = new RenameProcessor(project, psiElement,"NewTypeName",true,true);
            renameProcessor.run();
            setSuccess(true);
//            System.out.println("1111111111");
            String savePath = "D:\\IDEA_build\\saveFile\\"+project.getName()+"\\"+"NewTypeName";
            System.out.println("path:"+savePath);
            SaveFile.saveCurrentFileToNewFile1(project,filePath,savePath,"NewTypeName");
//            renameJavaMethodProcessor.findReferences(element, (SearchScope) psiFile,true); no save file
        }else{
            System.out.println("not method refactoring");
        }

    }

    private static boolean canRename(PsiElement element) {
        RenameJavaClassProcessor renameJavaClassProcessor = new RenameJavaClassProcessor();
        if(renameJavaClassProcessor.canProcessElement(element)){
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
