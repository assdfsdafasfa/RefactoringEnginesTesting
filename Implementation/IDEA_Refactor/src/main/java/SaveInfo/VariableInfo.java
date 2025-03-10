package SaveInfo;

import java.util.List;

public class VariableInfo {
    private int no; // 序号
    private String projectName; // 项目名称
    private String expressionName; // 表达式名称
    private String variableName; // 变量名称
    private String filePath; // 所在文件的相对路径
    private int offset; // 偏移量
    private int length; // 长度
    private boolean success = false;
    private String methodName;

    private List<String> methdoParameterTypes;

    private String className;
    // 构造函数
    public VariableInfo() {
    }

    // 构造函数
    public VariableInfo(int no, String projectName, String expressionName,
                        String variableName, String filePath, int offset, int length, String className) {
        this.no = no;
        this.projectName = projectName;
        this.expressionName = expressionName;
        this.variableName = variableName;
        this.filePath = filePath;
        this.offset = offset;
        this.length = length;
        this.className = className;
    }

    // Getter 和 Setter 方法
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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getExpressionName() {
        return expressionName;
    }

    public void setExpressionName(String expressionName) {
        this.expressionName = expressionName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
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
}

