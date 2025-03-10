package Model;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;

import java.util.List;

public class ExtractMethodInfo {
    private String path;
    private PsiMethod method;
    private PsiFile psiFile;
    private int offset;
    private int length;

    private List<String> paraType;

    private  String className;
    public ExtractMethodInfo(String path, PsiMethod method, PsiFile psiFile, int offset, int length, List<String> paraType){
        this.path = path;
        this.method = method;
        this.psiFile = psiFile;
        this.offset = offset;
        this.length = length;
        this.paraType = paraType;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public List<String> getParaType() {
        return this.paraType;
    }

    public void setParaType(final List<String> paraType) {
        this.paraType = paraType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PsiMethod getMethod() {
        return method;
    }

    public void setMethod(PsiMethod method) {
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
