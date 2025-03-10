package Action;

import Controller.RenameVariable;
import Model.*;
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

public class RenameLocalVariableAction extends AnAction {
    private List<RenameVariableInfo> allLocalVAariableList = new ArrayList<>();

    List<SaveRenameVariable> saveLocalVAariableList = new ArrayList<>();
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {

            VirtualFile projectBaseDir = project.getBaseDir();

            List<PsiFile> javaFiles = new ArrayList<>();
            ParseFile.collectJavaFiles(javaFiles, projectBaseDir, project);

            for (PsiFile psiFile : javaFiles) {
                findVariable(psiFile);
            }

            for (int i = 0; i < Math.min(allLocalVAariableList.size(), 1000); i++) {
                RenameVariableInfo renameVariableInfo = allLocalVAariableList.get(i);
                PsiElement psiElement  = renameVariableInfo.getMethod();
                int offset = renameVariableInfo.getOffset();
                String filePath = renameVariableInfo.getPsiFile().getVirtualFile().getPath();
                RenameVariable renameVariable = new RenameVariable(project, renameVariableInfo.getPsiFile(), offset, filePath, psiElement);
                renameVariable.renameVariableRefactoring();

                SaveRenameVariable variableInfo = new SaveRenameVariable();
                variableInfo.setNo(i);
                variableInfo.setProjectName(project.getName());
                variableInfo.setVariableName(variableInfo.getVariableName());
                variableInfo.setOffset(offset);
                variableInfo.setLength(variableInfo.getLength());
                variableInfo.setFilePath(filePath);
                variableInfo.setSuccess(variableInfo.isSuccess());

                saveLocalVAariableList.add(variableInfo);
            }
            // save JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(saveLocalVAariableList);

            try (Writer writer = new FileWriter("/AutoRefactor/src/main/resources/results/renameVariables.json")) {
                writer.write(json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void findVariable(PsiFile psiFile) {
        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof PsiLocalVariable) {
                    String filePath = psiFile.getVirtualFile().getPath();
                    PsiElement ele = element;
                    int offset = element.getTextOffset();
                    int length = element.getTextLength();

                    RenameVariableInfo renameMethodInfo = new RenameVariableInfo(filePath, ele, psiFile, offset, length);
                    allLocalVAariableList.add(renameMethodInfo);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
    }
}
