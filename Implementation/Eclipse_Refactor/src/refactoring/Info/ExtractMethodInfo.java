package refactoring.Info;

public class ExtractMethodInfo {
	private int no;
	private String method;
	private String projectName;
 	private String path;
    private int offset;
    private int length;
    private String typeName;
    private boolean isSuccess;
    public ExtractMethodInfo(int no, boolean isSuccess) {
    	this.no = no;
    	this.isSuccess = isSuccess;
    }
    public ExtractMethodInfo(int no, String method, String projectName, String path,  int offset, int length, String typeName){
    	this.no=no;
    	this.projectName = projectName;
		this.method = method;
        this.path = path;
        this.offset = offset;
        this.length = length;
        this.typeName = typeName;
    }
    
    public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
	public String getOldName() {
		return method;
	}
	public void setOldName(String method) {
		this.method = method;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
