package refactoring.file.consistency;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CompareFile {
	public static void main(String[] args) throws ParseException {
        // 示例：比较两个 Java 文件是否一致
        String filePathIDEA = "D:\\IDEA_build\\saveFile\\Demo\\newMethodName.java";
        String filePathEclipse = "D:\\IDEA_build\\saveFile\\Test\\testnewMethodName.java";

        try {       
//          String content1 = removeWhitespace(new String(Files.readAllBytes(Paths.get(filePathIDEA))));
//          String content2 = removeWhitespace(new String(Files.readAllBytes(Paths.get(filePathEclipse))));
//          boolean filesAreEqual = content1.equals(content2);
        	     	
            // 读取文件内容为字符串列表
            List<String> lines1 = Files.readAllLines(Paths.get(filePathIDEA));
            List<String> lines2 = Files.readAllLines(Paths.get(filePathEclipse));
            // 比较两个文件的内容是否相同
            boolean filesAreEqual = compareFileContents(lines1, lines2);

            if (filesAreEqual) {
                System.out.println("The files are identical.");
            } else {
                System.out.println("The files are different.");
            }
            
            // 获取 AST
            CompilationUnit ast1 = getAST(filePathIDEA);
            CompilationUnit ast2 = getAST(filePathEclipse);

            // 比较两个 AST 是否相同
            boolean astsAreEqual = compareASTs(ast1, ast2);

            if (astsAreEqual) {
                System.out.println("The ASTs are identical.");
            } else {
                System.out.println("The ASTs are different.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private static String removeWhitespace(String input) {
	        // 移除空格
	        return input.replaceAll("\\s", "");
	    }
	
    public static boolean compareFileContents(List<String> lines1, List<String> lines2) {
        // 检查行数是否相同
        if (lines1.size() != lines2.size()) {
            return false;
        }
        // 逐行比较文件内容
        for (int i = 0; i < lines1.size(); i++) {
        	String content1 = removeWhitespace(lines1.get(i));
        	String content2 = removeWhitespace(lines2.get(i));
            if (!content1.equals(content2)) {
                return false;
            }
        }
        return true;
    }
    
    public static CompilationUnit getAST(String filePath) throws IOException, ParseException {
        // 读取文件内容
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        // 创建 AST
        ASTParser parser = ASTParser.newParser(AST.JLS18);
        parser.setSource(content.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        return (CompilationUnit) parser.createAST(null);
    }

    public static boolean compareASTs(CompilationUnit ast1, CompilationUnit ast2) {
        // 使用 ASTMatcher 比较 AST 是否相同
        ASTMatcher matcher = new ASTMatcher();
        return ast1.subtreeMatch(matcher, ast2);
    }
    
    public static List<String> getFile(String filePath) {
    	List<File> allFileList = new ArrayList<>();
    	List<String> fileName = new ArrayList<>();
    	File dir = new File(filePath);
    	// 判断文件夹是否存在
        if (!dir.exists()) {
            System.out.println("目录不存在");
            return null;
        }
        getAllFile(dir, allFileList);

        for (File file : allFileList) {
            fileName.add(file.getName());
        }
        return fileName;
    }
    
    public static void getAllFile(File fileInput, List<File> allFileList) {
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                getAllFile(file, allFileList);
            } else {
                allFileList.add(file);
            }
        }
    }
}
