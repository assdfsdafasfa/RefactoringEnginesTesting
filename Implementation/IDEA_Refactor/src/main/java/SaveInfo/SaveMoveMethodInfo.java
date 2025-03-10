package SaveInfo;

import java.util.List;

public class SaveMoveMethodInfo {
    private int no; // 序号
    private String projectName; // 项目名称
    private String methodName; // method名称
    private String filePath; // 所在文件的相对路径
    private int offset; // 偏移量
    private int length; // 长度

    private List<String> parameterTypes;

    private boolean success = false;

    public int getNo() {
        return this.no;
    }

    public void setNo(final int no) {
        this.no = no;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public List<String> getParameterTypes() {
        return this.parameterTypes;
    }

    public void setParameterTypes(List<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }
}
