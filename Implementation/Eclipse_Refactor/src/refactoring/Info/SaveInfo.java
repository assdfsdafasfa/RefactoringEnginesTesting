package refactoring.Info;

import java.util.ArrayList;
import java.util.List;

public class SaveInfo {
	 private int no; // 序号
	 private String projectName; // 项目名称
	 private String name;
	 private String filePath; // 所在文件的相对路径
	 private int offset; // 偏移量
	 private int length; // 长度
	 private boolean sucess = false;
	 private int refacotringTimes = 0;
	 private List<ExpressionInfo> refactoringList =new ArrayList<>();
	 
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public boolean isSucess() {
		return sucess;
	}
	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}
	public int getRefacotringTimes() {
		return refacotringTimes;
	}
	public void setRefacotringTimes(int refacotringTimes) {
		this.refacotringTimes = refacotringTimes;
	}
	public List<ExpressionInfo> getRefactoringList() {
		return refactoringList;
	}
	public void setRefactoringList(List<ExpressionInfo> refactoringList) {
		this.refactoringList = refactoringList;
	}
	 
}

