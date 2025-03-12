package refactoring.file.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ParseFile {
	public static CompilationUnit parseJavaFileToAST(IFile file) {
		ASTParser parser = ASTParser.newParser(AST.JLS20); // Adjust the JLS version if needed
		String fileContent = getFileContent(file);
		parser.setSource(fileContent.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		return (CompilationUnit) parser.createAST(null);
	}

	public static String getFileContent(IFile file) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getContents()))) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
