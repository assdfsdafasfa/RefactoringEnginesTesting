package Controller;

import Model.FieldInfo;
import Utils.SaveFile;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.intellij.refactoring.introduceVariable.IntroduceVariableBase;
import com.intellij.refactoring.introduceVariable.IntroduceVariableHandler;
import com.intellij.refactoring.rename.RenameHandler;
import com.intellij.refactoring.rename.RenameJavaVariableProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameField implements Runnable{
    private PsiField psiField;
    private PsiFile psiFile;
    private Document document;
    private String filePath;
    private Editor editor;
    private Project project;

    private FieldInfo fieldInfo;
    private boolean success = false;
//    RenameJavaVariableProcessor variableProcessor = new RenameJavaVariableProcessor();

    public RenameField(Project project,PsiField field, FieldInfo fieldInfo) {
        this.project = project;
        this.filePath = fieldInfo.getFilePath();
        this.fieldInfo = fieldInfo;
        document = Editors.getCurrentDocument(filePath);
        editor = Editors.createSourceEditor(project, filePath, "java", false);
        psiField = field;
    }

    public  void renameField() {
            try{
                ApplicationManager.getApplication().invokeLater(() -> {
//                    System.out.println("path:"+fieldInfo.getFilePath());
                    RefactoringFactory factory = RefactoringFactory.getInstance(project);
                    RenameRefactoring renameRefactoring = factory.createRename(psiField, "newName");
                    RenameHandler renameHandler = new RenameHandler() {
                        @Override
                        public boolean isAvailableOnDataContext(@NotNull DataContext dataContext) {
                            return false;
                        }

                        @Override
                        public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {

                        }

                        @Override
                        public void invoke(@NotNull Project project, PsiElement @NotNull [] elements, DataContext dataContext) {

                        }
                    };
                    if (renameRefactoring.isPreviewUsages()) {
                        System.out.println("_");
                        String savePath = "D:\\IDEA_build\\saveFile\\" + project.getName() + "\\" + fieldInfo.getField().getName() + ".java";
                        SaveFile.saveCurrentFileToNewFile(project, fieldInfo.getFilePath(), savePath);
                    } else {
                        renameRefactoring.setSearchInComments(false);
                        renameRefactoring.setSearchInNonJavaFiles(false);
                        renameRefactoring.run();
                        setSuccess(true);
                        for (PsiReference psiReference : psiField.getReferences()) {
                            psiReference.handleElementRename("newName");
                        }
                        String savePath = "D:\\IDEA_build\\saveFile\\" + project.getName() + "\\" + fieldInfo.getField().getName() + ".java";
                        SaveFile.saveCurrentFileToNewFile(project, fieldInfo.getFilePath(), savePath);
                        VirtualFileManager.getInstance().syncRefresh();
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }

    }

    private String fieldName;
    @Override
    public void run() {

    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
