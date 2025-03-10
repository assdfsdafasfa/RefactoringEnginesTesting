package refactoring.Info;

import java.util.List;

public class ExpressionInfo {
	private int no;
	private String expression;
	private String projectName;
 	private String path;
    private int offset;
    private int length;
    private String methodName;
    private List<String> methdoParameterTypes;
    private String className;

    private boolean isSuccess;
    
    public ExpressionInfo(int no, boolean isSuccess){
    	this.isSuccess = isSuccess;
		this.no = no;
    }
    
 public ExpressionInfo(int no, String expression, String projectName, String path,  int offset, int length, String methodName, List<String> methdoParameterTypes){
    	
    	this.projectName = projectName;
		this.no = no;
		this.expression = expression;
        this.path = path;
        this.offset = offset;
        this.length = length;
        this.methodName = methodName;
        this.methdoParameterTypes = methdoParameterTypes;
    }
    
    public ExpressionInfo(int no, String expression, String projectName, String path,  int offset, int length, String methodName, List<String> methdoParameterTypes, String className){
    	
    	this.projectName = projectName;
		this.no = no;
		this.expression = expression;
        this.path = path;
        this.offset = offset;
        this.length = length;
        this.methodName = methodName;
        this.methdoParameterTypes = methdoParameterTypes;
        this.className = className;
    }
    
    public boolean isSuccess() {
		return isSuccess;
	}


	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
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
    public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getMethdoParameterTypes() {
        return this.methdoParameterTypes;
    }

    public void setMethdoParameterTypes(List<String> methdoParameterTypes) {
        this.methdoParameterTypes = methdoParameterTypes;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

