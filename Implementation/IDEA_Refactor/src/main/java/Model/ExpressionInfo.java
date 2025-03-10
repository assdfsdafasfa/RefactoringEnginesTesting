package Model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.List;

public class ExpressionInfo {
    private String path;
    private PsiElement exp;
    private PsiFile psiFile;
    private int offset;
    private int length;

    private String methodName;

    private List<String> methdoParameterTypes;

    private  String className;
    public ExpressionInfo(String path, PsiElement exp, PsiFile psiFile, int offset, int length, String methodName, List<String> methdoParameterTypes, String className){
        this.path = path;
        this.exp = exp;
        this.psiFile = psiFile;
        this.offset = offset;
        this.length = length;
        this.methodName = methodName;
        this.methdoParameterTypes = methdoParameterTypes;
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public List<String> getMethdoParameterTypes() {
        return this.methdoParameterTypes;
    }

    public void setMethdoParameterTypes(final List<String> methdoParameterTypes) {
        this.methdoParameterTypes = methdoParameterTypes;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PsiElement getExp() {
        return exp;
    }

    public void setExp(PsiElement exp) {
        this.exp = exp;
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
