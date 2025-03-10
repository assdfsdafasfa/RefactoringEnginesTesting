package Controller;

import Utils.Constant;
import Utils.ParseFile;
import Utils.SaveFile;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.refactoring.move.moveMembers.MockMoveMembersOptions;
import com.intellij.refactoring.move.moveMembers.MoveMembersOptions;
import com.intellij.refactoring.move.moveMembers.MoveMembersProcessor;

import java.util.LinkedHashSet;
import java.util.Map;

public class MoveMethod {
    public int no;
    public PsiMethod psiMethod;

    public LinkedHashSet<PsiMember> memberSet;

    public Project project;

    public int randomNumber;

    String filePath;
    Map<String, VirtualFile> virtualFileMap;

    public String className;
    public MoveMethod(int no, PsiMethod psiMethod, LinkedHashSet<PsiMember> memberSet, Project project, int randomNumber,String className){
        this.no = no;
        this.psiMethod = psiMethod;
        this.memberSet = memberSet;
        this.project = project;
        this.randomNumber = randomNumber;
        this.className = className;
    }

    public void preformMoveMethod(){
        MoveMembersOptions moveMembersOptions = new MockMoveMembersOptions(ParseFile.allClassNameList.get(randomNumber), memberSet);
        new MoveMembersProcessor(project, null, moveMembersOptions).run();// run refactoring
        FileDocumentManager.getInstance().saveAllDocuments();

        String savePath = Constant.FILE_SAVE_POSTION + project.getName() + "\\" + Constant.EXTRACT_VARIABLE + "\\"+ no +"\\"+ className +".java";


        SaveFile.saveCurrentFileToNewFile(virtualFileMap, project,filePath,savePath);
    }
}
