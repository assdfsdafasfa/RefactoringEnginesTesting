package Model;

import com.intellij.psi.PsiField;

public class FieldInfo {
    private String projectName; // 项目名称
    private String className;
    private String fieldType; // 类型
    private PsiField field; // 变量名称
    private String filePath; // 所在文件的相对路径
    private int offset; // 偏移量
    private int length; // 长度
    private boolean success = false;

    // 构造函数
    public FieldInfo() {
    }

    // 构造函数
    public FieldInfo(String className,
                     PsiField fieldName, String fieldType,String filePath, int offset, int length) {
        this.className = className;
        this.field = fieldName;
        this.fieldType = fieldType;
        this.filePath = filePath;
        this.offset = offset;
        this.length = length;
    }

    // Getter 和 Setter 方法

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public PsiField getFieldName() {
        return field;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PsiField getField() {
        return field;
    }

    public void setField(PsiField field) {
        this.field = field;
    }
}
