package SaveInfo;

public class SaveRenameParameter {
    private int no; // 序号
    private String projectName; // 项目名称
    private String parameterName; // method名称
    private String filePath; // 所在文件的相对路径
    private int offset; // 偏移量
    private int length; // 长度
    private boolean success = false;

    // 构造函数
    public SaveRenameParameter() {
    }

    // 构造函数
    public SaveRenameParameter(int no, String projectName,
                              String parameterName, String filePath, int offset, int length) {
        this.no = no;
        this.projectName = projectName;
        this.parameterName = parameterName;
        this.filePath = filePath;
        this.offset = offset;
        this.length = length;
    }

    // Getter 和 Setter 方法
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

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String expressionName) {
        this.parameterName = parameterName;
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
