package Action;

import Controller.RenameType;
import Model.RenameTypeInfo;
import SaveInfo.SaveRenameType;
import Utils.ParseFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class RenameTypeAction extends AnAction {
    private List<RenameTypeInfo> allTypeList = new ArrayList<>();

    List<SaveRenameType> saveTypeList = new ArrayList<>();
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {

            VirtualFile projectBaseDir = project.getBaseDir();

            List<PsiFile> javaFiles = new ArrayList<>();
            ParseFile.collectJavaFiles(javaFiles, projectBaseDir, project);

            for (PsiFile psiFile : javaFiles) {
                findType(psiFile);
            }

            for (int i = 0; i < Math.min(allTypeList.size(), 10); i++) {
                RenameTypeInfo renameTypeInfo = allTypeList.get(i);
                int offset = renameTypeInfo.getOffset();
                String filePath = renameTypeInfo.getPsiFile().getVirtualFile().getPath();
                PsiElement psiElement  = renameTypeInfo.getType();
                RenameType renameType = new RenameType(project, renameTypeInfo.getPsiFile(), offset, renameTypeInfo.getLength(),filePath,psiElement);
                renameType.renameTypeRefactoring();

                //save method
                SaveRenameType saveRenameType = new SaveRenameType();
                saveRenameType.setNo(i);
                saveRenameType.setProjectName(project.getName());
                saveRenameType.setTypeName(((PsiClass) psiElement).getName().toString());
                saveRenameType.setOffset(offset);
                saveRenameType.setLength(renameTypeInfo.getLength());
                saveRenameType.setFilePath(filePath);
                saveRenameType.setSuccess(saveRenameType.isSuccess());

                saveTypeList.add(saveRenameType);
            }
            // save JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(saveTypeList);
            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/TypeOutput.json")) {
                writer.write(json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void findType(PsiFile psiFile) {
        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiClass) {
                    String filePath = psiFile.getVirtualFile().getPath();
                    PsiElement ele = element;
                    int offset = element.getTextOffset();
                    int length = element.getTextLength();

                    RenameTypeInfo renameMethodInfo = new RenameTypeInfo(filePath, ele, psiFile, offset, length);
                    allTypeList.add(renameMethodInfo);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
    }
}
