package Utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.util.ArrayList;
import java.util.List;

public class ParseFile {

    public static List<String> allClassNameList = new ArrayList<>();
    public static void collectJavaFiles(List<PsiFile> javaFiles, VirtualFile directory, Project project) {
        if (directory.isDirectory()) {
            for (VirtualFile child : directory.getChildren()) {

                collectJavaFiles(javaFiles, child, project);
            }
        } else if (directory.getName().endsWith(".java")) {

            PsiFile psiFile = PsiManager.getInstance(project).findFile(directory);
            if (psiFile != null) {
                javaFiles.add(psiFile);
            } else {
                System.out.println("NULL!!!!");
            }
            int s = directory.getName().lastIndexOf("/");
            String dir = directory.getName().substring(s);
            s = dir.lastIndexOf(".");
            dir = dir.substring(0,s);
            allClassNameList.add(dir);
        }
    }
}
