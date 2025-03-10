package Action;

import Controller.RenameField;
import Model.FieldInfo;
import SaveInfo.SaveRenameField;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import Utils.ParseFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class RenameFieldAction extends AnAction {
    private List<PsiField> fieldList = new ArrayList<>();
    List<FieldInfo> fieldInfoList = new ArrayList<>();
    List<SaveRenameField>  saveFieldData = new ArrayList<>();
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // 获取当前项目
        Project project = e.getProject();

        if (project != null) {
            VirtualFile projectBaseDir = project.getBaseDir();

            List<PsiFile> javaFiles = new ArrayList<>();
            ParseFile.collectJavaFiles(javaFiles, projectBaseDir, project);

            for (PsiFile psiFile : javaFiles) {
                findFields(psiFile);
            }

            for (int i = 0; i < Math.min(fieldInfoList.size(), 1); i++) {
                FieldInfo fieldInfo = fieldInfoList.get(i);
                RenameField renameField = new RenameField(project, fieldInfo.getFieldName(), fieldInfo);
                renameField.renameField();// rename

                SaveRenameField saveRenameField = new SaveRenameField();
                saveRenameField.setNo(i);
                saveRenameField.setProjectName(project.getName());
                saveRenameField.setField(fieldInfo.getField());
                saveRenameField.setOffset(fieldInfo.getOffset());
                saveRenameField.setLength(fieldInfo.getLength());
                saveRenameField.setFilePath(fieldInfo.getFilePath());
                saveRenameField.setSuccess(renameField.isSuccess());

                saveFieldData.add(saveRenameField);
            }

            // save JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(saveFieldData);
            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/output.json")) {
                writer.write(json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//            try {
//                Writer writer = new FileWriter("D:/IDEA_build/saveFile/output.json");
//                gson.toJson(saveFieldData, writer);
//                System.out.println("JSON file created successfully!");
//            } catch (IOException ex) {
//            throw new RuntimeException(ex);
        }

    }

    private void findFields(PsiFile psiFile) {

        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {

                if (isField(element)) {

                    String filePath = psiFile.getVirtualFile().getPath();
                    PsiField psiField = (PsiField) element;
                    int offset = element.getTextOffset();
                    int length = element.getTextLength();
                    PsiType psiType = psiField.getType();

                    PsiClass containingClass = psiField.getContainingClass();


                    FieldInfo fieldInfo = new FieldInfo(containingClass.toString(),psiField,psiType.toString(), filePath, offset, length);
                    fieldInfoList.add(fieldInfo);
                }

                element.acceptChildren(this);
            }

            private boolean isField(PsiElement element) {
                return element instanceof PsiField;
            }

        };

        psiFile.accept(visitor);
    }
}
