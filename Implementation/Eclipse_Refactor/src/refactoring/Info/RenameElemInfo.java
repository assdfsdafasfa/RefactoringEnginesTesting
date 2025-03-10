package refactoring.Info;

public class RenameElemInfo {
	private String oldName;
	private String projectName;
 	private String path;
    private int offset;
    private int length;
    public RenameElemInfo(String oldName, String projectName, String path,  int offset, int length){
    	this.projectName = projectName;
		this.oldName = oldName;
        this.path = path;
        this.offset = offset;
        this.length = length;
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
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}

