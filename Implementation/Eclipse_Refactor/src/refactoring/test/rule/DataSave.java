package refactoring.test.rule;

public class DataSave {
	private String commitId;
	private String oldRe;
	private String newRe;
	private String type;
	private String classPath;
	private String projectNameString;
	
public String getProjectNameString() {
		return projectNameString;
	}
	public void setProjectNameString(String projectNameString) {
		this.projectNameString = projectNameString;
	}
	//	public DataSave(String commitId, String oldRe, String newRe, String type, String classPath) {
//		this.commitId=commitId;
//		this.oldRe=oldRe;
//		this.newRe=newRe;
//		this.type=type;
//		this.classPath=classPath;
//	}
//
//	public DataSave() {
//		// TODO Auto-generated constructor stub
//	}
//
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getOldRe() {
		return oldRe;
	}
	public void setOldRe(String oldRe) {
		this.oldRe = oldRe;
	}
	public String getNewRe() {
		return newRe;
	}
	public void setNewRe(String newRe) {
		this.newRe = newRe;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
}