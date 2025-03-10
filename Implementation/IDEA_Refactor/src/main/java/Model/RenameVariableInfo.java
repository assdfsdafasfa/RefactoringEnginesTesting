package Model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class RenameVariableInfo {
    private String path;
    private PsiElement variable;
    private PsiFile psiFile;
    private int offset;
    private int length;

    public RenameVariableInfo(String path, PsiElement variable, PsiFile psiFile, int offset, int length){
        this.path = path;
        this.variable = variable;
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

    public PsiElement getMethod() {
        return variable;
    }

    public void setMethod(PsiElement variable) {
        this.variable = variable;
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
