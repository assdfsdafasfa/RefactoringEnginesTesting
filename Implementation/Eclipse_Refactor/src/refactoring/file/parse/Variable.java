package refactoring.file.parse;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class Variable extends ASTVisitor {

	String variableName;
	ArrayList<Statement> referenceStatements;

	public Variable(String variableName) {
		this.variableName = variableName;
		this.referenceStatements = new ArrayList<>();
	}

	public ArrayList<Statement> getReferenceStatements() {
		return referenceStatements;
	}

	@Override
	public boolean visit(SimpleName astNode) {
		if (astNode.getIdentifier().equals(variableName)) {
			ASTNode surroundingBlock = astNode;
			while ((surroundingBlock = surroundingBlock.getParent()) != null) {
				if (surroundingBlock instanceof Statement) {
					break;
				}
			}
			if (surroundingBlock != null) {
				this.referenceStatements.add((Statement) surroundingBlock);
			}
		}
		return true;
	}
	
	public class LocalVariableVisitor extends ASTVisitor {
	    @Override
	    public boolean visit(MethodDeclaration node) {
	        // 获取方法名
	        String methodName = node.getName().getIdentifier();
	        // 遍历方法的局部变量
	        for (Object obj : node.parameters()) {
	            SingleVariableDeclaration var = (SingleVariableDeclaration) obj;
	            String varName = var.getName().getIdentifier();
	        }	        
	       // 遍历方法的参数
	        for (Object obj : node.parameters()) {
	            SingleVariableDeclaration parameter = (SingleVariableDeclaration) obj;
	            String parameterName = parameter.getName().getIdentifier();
	        }
	        return true;
	    }
	}
}
