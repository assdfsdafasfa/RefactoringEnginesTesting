package refactoring.convert.rule;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.refactoring.descriptors.ExtractClassDescriptor;
import org.eclipse.jdt.internal.corext.refactoring.structure.ExtractClassRefactoring;
public class ConvertExtractClassToRename {
	public static void convertExtractClassToRename(IProject project, List<IFile> javaFiles, IFile testCaseFile,
			CompilationUnit cu, String sourceClass, String methodName, String targetClass)
			throws JavaModelException, IllegalArgumentException {

cu.accept(new ASTVisitor(){
@Override
public boolean visit(TypeDeclaration node) {
	if (node.getName().getIdentifier().equals("Person")) {
		try {
//			performExtractClass(cu, node);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return super.visit(node);
}

});}

private static void performExtractClass(CompilationUnit cu, ExtractClassDescriptor typeNode) throws Exception {
	// Create the Extract Class Refactoring
ExtractClassRefactoring refactoring = new ExtractClassRefactoring(typeNode);
	// Set the new class name
//	refactoring.setNewClassName("ContactInfo");
//	// Add fields to extract
//	refactoring.addFieldToExtract("address");
//	refactoring.addFieldToExtract("phoneNumber");
//	// Add methods to extract
//	refactoring.addMethodToExtract("getAddress");
//	refactoring.addMethodToExtract("setAddress");
//	refactoring.addMethodToExtract("getPhoneNumber");
//	refactoring.addMethodToExtract("setPhoneNumber");

	// Perform the refactoring
	NullProgressMonitor monitor = new NullProgressMonitor();
	if (refactoring.checkInitialConditions(monitor).isOK() && refactoring.checkFinalConditions(monitor).isOK()) {
//		TextEdit edits = refactoring.createChange(monitor).getEdit();
//		Document document = new Document(cu.toString());
//		edits.apply(document);

//		System.out.println("Refactored Code:\n" + document.get());
	} else {
		System.out.println("Refactoring conditions not met.");
	}
}
}
