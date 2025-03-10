package refactoring.json;

public class RefactoringRecord {
	private int no;
	private String refactoringType;
	private String oldName;
	private String newName;
	private String className;
	private String path;
	private String projectName;
	private int offset;
	private int length;

	public RefactoringRecord(String projectName, int no, String oldName, String newName, String path, int offset,
			int length, String className, String refactoringType) {
		this.projectName = projectName;
		this.no = no;
		this.oldName = oldName;
		this.newName = newName;
		this.path = path;
		this.offset = offset;
		this.length = length;
		this.className = className;
		this.refactoringType = refactoringType;
	}

	public RefactoringRecord() {
		// TODO Auto-generated constructor stub
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getRefactoringType() {
		return refactoringType;
	}

	public void setRefactoringType(String refactoringType) {
		this.refactoringType = refactoringType;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

}
