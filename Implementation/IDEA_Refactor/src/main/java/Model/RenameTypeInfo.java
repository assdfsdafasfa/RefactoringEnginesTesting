package Model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class RenameTypeInfo {
    private String path;
    private PsiElement type;
    private PsiFile psiFile;
    private int offset;
    private int length;

    public RenameTypeInfo(String path, PsiElement type, PsiFile psiFile, int offset, int length){
        this.path = path;
        this.type = type;
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

    public PsiElement getType() {
        return type;
    }

    public void setType(PsiElement type) {
        this.type = type;
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
