package Action;

import Controller.InlineMethod;
import Model.InlineMethodInfo;
import SaveInfo.SaveInlineMethodInfo;
import Utils.ParseFile;
import Utils.ShuffleList;
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
import java.util.Map;
import java.util.Set;

import static Utils.SaveFile.getVirtualFile;

public class InlineMethodAction extends AnAction {
    private List<InlineMethodInfo> allCalledMethods = new ArrayList<>();
    private List<SaveInlineMethodInfo> saveMoveMethodInfos = new ArrayList<SaveInlineMethodInfo>();
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

            Set<Integer> randomData = ShuffleList.randomCalledMethodList(allCalledMethods);
            for(Integer i:randomData){
                InlineMethodInfo inlineMethodInfo = allCalledMethods.get(i);
                int offset = inlineMethodInfo.getOffset();
                int length = inlineMethodInfo.getLength();
                PsiElement psiMethod = inlineMethodInfo.getMethod();
                String filePath = inlineMethodInfo.getPsiFile().getVirtualFile().getPath();
//              InlineMethodHandler inlineMethodHandler = new InlineActionHandler();
                InlineMethod inlineMethod = new InlineMethod(psiMethod, offset, length, filePath, i);
                inlineMethod.inlineMethodsTest();

                SaveInlineMethodInfo saveInfo = new SaveInlineMethodInfo();
                saveInfo.setNo(i);
                saveInfo.setMethodName(((PsiMethod) psiMethod).getName());
                saveInfo.setFilePath(inlineMethodInfo.getPath());
                saveInfo.setParameterTypes(inlineMethodInfo.getParaTypes());
                saveInfo.setProjectName(project.getName());
                saveMoveMethodInfos.add(saveInfo);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(saveMoveMethodInfos);

            try (Writer writer = new FileWriter("D:/IDEA_build/saveFile/Json/"+project.getName()+"InlineMethod"+".json")) {
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
                if (element instanceof PsiMethodCallExpression) {
                    // 获取方法调用的信息
                    PsiReferenceExpression methodExpression = ((PsiMethodCallExpression) element).getMethodExpression();
                    PsiMethod calledMethod = (PsiMethod) methodExpression.resolve();
                    String filePath = psiFile.getVirtualFile().getPath();
                    int offset = element.getTextOffset();
                    int length = element.getTextLength();
                    List<String> paraType = getMethodParameterTypes(calledMethod);
                    InlineMethodInfo inlineMethodInfo = new InlineMethodInfo(filePath, element, psiFile, offset, length, paraType);
                    allCalledMethods.add(inlineMethodInfo);
                }
                element.acceptChildren(this);
            }
        };
        // PSI
        psiFile.accept(visitor);
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
