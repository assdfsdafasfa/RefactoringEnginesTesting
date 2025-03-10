package Controller;

import Utils.SaveFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.refactoring.rename.RenameJavaMethodProcessor;
import com.intellij.refactoring.rename.RenameJavaVariableProcessor;
import com.intellij.refactoring.rename.RenameProcessor;

public class RenameVariable {
    private PsiElement psiElement;
    private static PsiFile psiFile;
    private Document document;
    private String filePath;
    private Editor editor;
    private Project project;
    private int offset;
    private boolean success = false;
    public RenameVariable(Project project, PsiFile psiFile, int offset, String filePath, PsiElement psiElement) {
        this.project = project;
        this.psiFile = psiFile;
        this.offset = offset;
        this.filePath = psiFile.getVirtualFile().getPath();
        document = Editors.getCurrentDocument(filePath);
        editor = Editors.createSourceEditor(project, filePath, "java", false);
        this.psiElement = psiElement;
    }
    public void renameVariableRefactoring() {
        if(canRename(psiElement) ){
            RenameProcessor renameProcessor = new RenameProcessor(project, psiElement,"newVariableName",true,true);
            renameProcessor.run();
            setSuccess(true);
            String savePath = "D:\\IDEA_build\\saveFile\\"+project.getName()+"\\"+((PsiLocalVariable) psiElement).getName()+"_Variable"+".java";
            SaveFile.saveCurrentFileToNewFile(project,filePath,savePath);
//            renameJavaMethodProcessor.findReferences(element, (SearchScope) psiFile,true);
        }else{
            System.out.println("not method refactoring");
        }
    }

    private static boolean canRename(PsiElement element) {
        Project project = element.getProject();
        RenameJavaVariableProcessor renameJavaVariableProcessor = new RenameJavaVariableProcessor();
        if(renameJavaVariableProcessor.canProcessElement(element)){
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
