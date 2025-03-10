package refactoring.random.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.internal.corext.dom.GenericVisitor;
import org.eclipse.jdt.internal.corext.dom.Selection;

public class NameCollector extends GenericVisitor {
	private List<String> names = new ArrayList<>();
	private Selection fSelection;

	public NameCollector(ASTNode node) {
		fSelection = Selection.createFromStartLength(node.getStartPosition(), node.getLength());
	}

	@Override
	protected boolean visitNode(ASTNode node) {
		if (node.getStartPosition() > fSelection.getInclusiveEnd())
			return true;
		if (fSelection.coveredBy(node))
			return true;
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		names.add(node.getIdentifier());
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		boolean result = super.visit(node);
		if (!result)
			names.add(node.getName().getIdentifier());
		return result;
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		boolean result = super.visit(node);
		if (!result)
			names.add(node.getName().getIdentifier());
		return result;
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		names.add(node.getDeclaration().getName().getIdentifier());
		return false;
	}

	List<String> getNames() {
		return names;
	}
}
