package Action;

import Controller.RenameParameter;
import Controller.RenameVariable;
import Model.RenameParameterInfo;
import Model.RenameVariableInfo;
import SaveInfo.SaveRenameParameter;
import SaveInfo.SaveRenameVariable;
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

public class RenameParameterAction extends AnAction {
    private List<RenameParameterInfo> allParameterList = new ArrayList<>();

    List<SaveRenameParameter> saveParameterList = new ArrayList<>();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {

            VirtualFile projectBaseDir = project.getBaseDir();

            List<PsiFile> javaFiles = new ArrayList<>();
            ParseFile.collectJavaFiles(javaFiles, projectBaseDir, project);

            for (PsiFile psiFile : javaFiles) {
                findParameter(psiFile);
            }

            for (int i = 0; i < Math.min(allParameterList.size(), 1000); i++) {
                RenameParameterInfo renameVariableInfo = allParameterList.get(i);
                PsiElement psiElement  = renameVariableInfo.getParameter();
                int offset = renameVariableInfo.getOffset();
                String filePath = renameVariableInfo.getPsiFile().getVirtualFile().getPath();
                RenameParameter renameParameter = new RenameParameter(project, renameVariableInfo.getPsiFile(), offset, filePath, psiElement);
                renameParameter.renameParameterRefactoring();

                SaveRenameParameter parameterInfo = new SaveRenameParameter();
                parameterInfo.setNo(i);
                parameterInfo.setProjectName(project.getName());
                parameterInfo.setParameterName(parameterInfo.getParameterName());
                parameterInfo.setOffset(offset);
                parameterInfo.setLength(parameterInfo.getLength());
                parameterInfo.setFilePath(filePath);
                parameterInfo.setSuccess(parameterInfo.isSuccess());

                saveParameterList.add(parameterInfo);
            }
            // save JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(saveParameterList);

            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/ParameterOutput.json")) {
                writer.write(json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void findParameter(PsiFile psiFile) {
        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiParameter) {
                    String filePath = psiFile.getVirtualFile().getPath();
                    PsiElement ele = element;
                    int offset = element.getTextOffset();
                    int length = element.getTextLength();

                    RenameParameterInfo renameParameterInfo = new RenameParameterInfo(filePath, ele, psiFile, offset, length);
                    allParameterList.add(renameParameterInfo);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
    }
}
