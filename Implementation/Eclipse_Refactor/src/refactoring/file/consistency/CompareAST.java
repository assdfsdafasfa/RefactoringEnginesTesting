package refactoring.file.consistency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CompareAST {
	// 生成測試用例數量？
	// 對於測試：1 rename 輸出所有調用位置， 比較序列
	// 2 inline/ extract 都是用junit生成測試用例； 沒有預期行爲，怎末看輸出
	// 3 move method: 可能會有參數不一直情況，只考慮可以測試情況，不能測試使用人工檢查
	public static void main(String[] args) {
		String filePath = "D:\\aa_桌面文件\\RefactoringTest测试用例\\TestCase\\RenameMethod\\Original\\1_import_method.java";
		String filePath1 = "D:\\aa_桌面文件\\RefactoringTest测试用例\\TestCase\\RenameMethod\\Original\\1_import_method.java";
		String source = readFileToString(filePath);
		String source1 = readFileToString(filePath1);
		CompilationUnit compilationUnit = parseSourceToAST(source);
		CompilationUnit compilationUnit1 = parseSourceToAST(source1);
		if (!compilationUnit.equals(compilationUnit1)) {
			System.out.println("Inconsistent refactoring behavior");
			System.out.println(filePath);
		}
	}

	private static String readFileToString(String filePath) {
		Path path = Paths.get(filePath);
		try {
			return new String(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static CompilationUnit parseSourceToAST(String sourceCode) {
		ASTParser parser = ASTParser.newParser(AST.JLS_Latest);
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		return (CompilationUnit) parser.createAST(null);
	}
}
