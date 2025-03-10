package Action;

import Controller.MoveMethod;
import Model.MoveMethodInfo;
import Model.RenameMethodInfo;
import SaveInfo.SaveMoveMethodInfo;
import Utils.ParseFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.move.moveInstanceMethod.MoveInstanceMethodHandler;
import com.intellij.refactoring.move.moveInstanceMethod.MoveInstanceMethodProcessor;
import com.intellij.refactoring.move.moveMembers.*;
import com.intellij.refactoring.openapi.impl.MoveInstanceMethodRefactoringImpl;
import jnr.ffi.annotations.In;
import org.apache.tools.ant.taskdefs.Move;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static Utils.SaveFile.getVirtualFile;

public class MoveMethodAction extends AnAction {
    List<MoveMethodInfo> allMehtodList = new ArrayList<>();

    List<SaveMoveMethodInfo> saveMoveMethodInfos = new ArrayList<>();
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

            for(int i=0;i<Math.min(allMehtodList.size(), 5);i++){
                MoveMethodInfo moveMethodInfo = allMehtodList.get(i);
                PsiElement element = moveMethodInfo.getMethod();
                PsiReference reference = element.getReference();
                Random random = new Random();
                int randomNumber = random.nextInt(0, ParseFile.allClassNameList.size()-1);
                PsiClass targetClass = findTargetClass(project, ParseFile.allClassNameList.get(randomNumber));
                LinkedHashSet<PsiMember> memberSet = new LinkedHashSet<>();
                memberSet.add((PsiMethod) element);
                MoveMethod moveMethod = new MoveMethod(i,(PsiMethod) element,memberSet, project, randomNumber,moveMethodInfo.getPsiFile().getName());
                moveMethod.preformMoveMethod();
                SaveMoveMethodInfo saveMoveMethodInfo = new SaveMoveMethodInfo();
                saveMoveMethodInfo.setNo(i);
                saveMoveMethodInfo.setMethodName(((PsiMethod) element).getName());
                saveMoveMethodInfo.setFilePath(moveMethodInfo.getPath());
                saveMoveMethodInfo.setParameterTypes(moveMethodInfo.getParaType());
                saveMoveMethodInfo.setProjectName(project.getName());
                saveMoveMethodInfos.add(saveMoveMethodInfo);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(saveMoveMethodInfos);

            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/Json/"+project.getName()+"MoveMethod"+".json")) {
                writer.write(json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
//        moveInstanceMethodProcessor.run();
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

                    List<String> paraType = getMethodParameterTypes((PsiMethod) ele);
                    MoveMethodInfo moveMethodInfo = new MoveMethodInfo(filePath, ele, psiFile, offset, length, paraType);
                    allMehtodList.add(moveMethodInfo);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
    }

    private PsiClass findTargetClass(Project project, String className) {
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, className + ".java", GlobalSearchScope.projectScope(project));
        if (psiFiles.length > 0) {
            PsiFile psiFile = psiFiles[0];
            return PsiTreeUtil.getChildOfType(psiFile, PsiClass.class);
        }
        return null;
    }

    private static List<String> getMethodParameterTypes(PsiMethod psiMethod) {

        PsiParameterList parameterList = psiMethod.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();
        List<String> parameterTypes = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            PsiParameter parameter = parameters[i];
            String parameterType = parameter.getType().toString();
            int pos = parameterType.indexOf(":");
            if(pos!=-1) {
                parameterType = parameterType.substring(pos+1);
            }
            parameterTypes.add(parameterType);
        }
        return parameterTypes;
    }
}

