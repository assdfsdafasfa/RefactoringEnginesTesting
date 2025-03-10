package refactoring.file.parse;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.core.refactoring.descriptors.RefactoringSignatureDescriptorFactory;

public class CompilationFile {
	 public static CompilationUnit getCompilationUnit(String javaFilePath) {
		    byte[] input = null;
		    try {
		        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFilePath));
		        input = new byte[bufferedInputStream.available()];
		        bufferedInputStream.read(input);
		        bufferedInputStream.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			ASTParser astParser = ASTParser.newParser(AST.JLS18);
			astParser.setKind(ASTParser.K_COMPILATION_UNIT);
			astParser.setSource(new String(input).toCharArray());
			astParser.setResolveBindings(true);
			astParser.setBindingsRecovery(true);
			CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
			return unit;
	}
	 public static CompilationUnit parse(ICompilationUnit compilationUnit) {
	        ASTParser parser = ASTParser.newParser(AST.JLS18);
	        parser.setKind(ASTParser.K_COMPILATION_UNIT);
	        parser.setSource(compilationUnit);
	        parser.setResolveBindings(true);

	        return (CompilationUnit) parser.createAST(null);
	    }

	 
	 public IType getType(ICompilationUnit cu, String name) throws JavaModelException {
			for (IType type : cu.getAllTypes()) {
				if (type.getTypeQualifiedName('.').equals(name) || type.getElementName().equals(name)) {
					return type;
				}
			}
			return null;
		}
	private RenameJavaElementDescriptor createRefactoringDescriptor(IType type, String newName) {
			RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_TYPE);
			descriptor.setJavaElement(type);
			descriptor.setNewName(newName);
			descriptor.setUpdateReferences(true);
			return descriptor;
		}
	
	public static ICompilationUnit getCompilationUnitFromFile(String filePath) throws JavaModelException {
     IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
     IFile file = root.getFileForLocation(new Path(filePath));
     if (file != null && file.exists()) {
    	 ICompilationUnit javaElement = (ICompilationUnit) JavaCore.create(file);    
         return javaElement;
     } 
     return null;
 }

}
