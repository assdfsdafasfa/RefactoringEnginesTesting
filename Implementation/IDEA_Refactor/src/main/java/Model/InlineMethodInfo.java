package Model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;

import java.util.List;

public class InlineMethodInfo {
    private String path;
    private PsiElement method;
    private PsiFile psiFile;
    private int offset;
    private int length;

    private List<String> paraTypes;
    public InlineMethodInfo(String path, PsiElement method, PsiFile psiFile, int offset, int length, List<String> paraTypes){
        this.path = path;
        this.method = method;
        this.psiFile = psiFile;
        this.offset = offset;
        this.length = length;
        this.paraTypes = paraTypes;
    }
    public List<String> getParaTypes() {
        return this.paraTypes;
    }
    public void setParaTypes(final List<String> paraTypes) {
        this.paraTypes = paraTypes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PsiElement getMethod() {
        return method;
    }

    public void setMethod(PsiElement method) {
        this.method = method;
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
