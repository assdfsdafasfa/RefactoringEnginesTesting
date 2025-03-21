package Model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class RenameParameterInfo {
    private String path;
    private PsiElement parameter;
    private PsiFile psiFile;
    private int offset;
    private int length;

    public RenameParameterInfo(String path, PsiElement parameter, PsiFile psiFile, int offset, int length){
        this.path = path;
        this.parameter = parameter;
        this.psiFile = psiFile;
        this.offset = offset;
        this.length = length;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PsiElement getParameter() {
        return parameter;
    }

    public void setParameter(PsiElement parameter) {
        this.parameter = parameter;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public PsiFile getPsiFile() {
        return psiFile;
    }

    public void setPsiFile(PsiFile psiFile) {
        this.psiFile = psiFile;
    }
}
