package Action;

import Controller.RenameMethod;
import Model.RenameMethodInfo;
import SaveInfo.SaveRenameMethod;
import Utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.command.impl.UndoManagerImpl;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static Utils.SaveFile.getVirtualFile;

public class RenameMethodAction extends AnAction{
    private List<RenameMethodInfo> allMehtodList = new ArrayList<>();
    List<SaveRenameMethod> saveMethodList = new ArrayList<>();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {

            VirtualFile projectBaseDir = project.getBaseDir();

            List<PsiFile> javaFiles = new ArrayList<>();
            ParseFile.collectJavaFiles(javaFiles, projectBaseDir, project);

            Map<String, VirtualFile> virtualFileMap = getVirtualFile(project);
            for (PsiFile psiFile : javaFiles) {
                findMethod(psiFile);
            }
            Set<Integer> randomData = ShuffleList.randomList(allMehtodList); // random select data
            System.out.println("randomData_size:"+randomData.size());
            for(Integer n:randomData){
//                for (int i = 0; i < Math.min(allMehtodList.size(), 1); i++) {
                LocalDateTime time1 = LocalDateTime.now();
                RenameMethodInfo renameMethodInfo = allMehtodList.get(n);
                int offset = renameMethodInfo.getOffset();
                String filePath = renameMethodInfo.getPsiFile().getVirtualFile().getPath();
                PsiElement psiElement  = renameMethodInfo.getMethod();
                RenameMethod renameMethod = new RenameMethod(virtualFileMap, project, renameMethodInfo.getPsiFile(), offset, renameMethodInfo.getLength(),filePath,psiElement,n);
                renameMethod.renameMethodRefactoring();
                List<Path> modifierPath = RecordTime.recordFileTime(javaFiles, time1);
                try {
                    SaveFile.saveAllModifierFile(modifierPath, Constant.FILE_SAVE_POSTION + project.getName() + "\\" + Constant.RENAME_METHOD + "\\"+ n);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // UndoManager
                UndoManager undoManager = UndoManager.getInstance(project);
                FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
                VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);
                if (virtualFile != null) {
                    FileEditor[] fileEditors = fileEditorManager.openFile(virtualFile, true);
                    for (FileEditor fileEditor : fileEditors) {
                        if (undoManager.isUndoAvailable(fileEditor)) {
                            undoManager.undo(fileEditor);
                        }
                    }
                    System.out.println("undo执行完成");
                }

                //save method
                SaveRenameMethod saveRenameMethod = new SaveRenameMethod();
                saveRenameMethod.setNo(n);
                saveRenameMethod.setProjectName(project.getName());
                saveRenameMethod.setMehtodName(((PsiMethod) psiElement).getName().toString());
                saveRenameMethod.setOffset(offset);
                saveRenameMethod.setLength(renameMethodInfo.getLength());
                saveRenameMethod.setFilePath(filePath);
                saveRenameMethod.setSuccess(renameMethod.isSuccess());

                saveMethodList.add(saveRenameMethod);
            }
            // save JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(saveMethodList);
            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/Json/"+project.getName()+".json")) {
                writer.write(json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void findMethod(PsiFile psiFile) {
        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiMethod) {
                    String filePath = psiFile.getVirtualFile().getPath();
                    PsiElement ele = element;
                    int offset = element.getTextOffset();
                    int length = element.getTextLength();

                    RenameMethodInfo renameMethodInfo = new RenameMethodInfo(filePath, ele, psiFile, offset, length);
                    allMehtodList.add(renameMethodInfo);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
    }
}
