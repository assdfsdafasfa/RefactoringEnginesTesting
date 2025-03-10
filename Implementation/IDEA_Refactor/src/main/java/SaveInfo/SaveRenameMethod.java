package SaveInfo;

public class SaveRenameMethod {
    private int no; // 序号
    private String projectName; // 项目名称
    private String methodName; // method名称
    private String filePath; // 所在文件的相对路径
    private int offset; // 偏移量
    private int length; // 长度
    private boolean success = false;

    // 构造函数
    public SaveRenameMethod() {
    }

    // 构造函数
    public SaveRenameMethod(int no, String projectName,
                            String methodName, String filePath, int offset, int length) {
        this.no = no;
        this.projectName = projectName;
        this.methodName = methodName;
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

    public String getMehtodName() {
        return methodName;
    }

    public void setMehtodName(String methodName) {
        this.methodName = methodName;
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
