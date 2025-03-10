package refactoring.file.parse;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class AnlysisSource {
	static List<File> fileList = new ArrayList<>();
	static List<String> pathList = new ArrayList<>();
	static List<MethodDeclaration> mdList = new ArrayList<>();
	public static void main(String []args) throws IOException {
		String path = "D:/GodClass/chen/46";
		File file = new File(path);
		fun(file);
		for(int i=0;i<fileList.size();i++) {
			savePath(fileList.get(i).toString());
		}
		
		for(int i=0;i<fileList.size();i++) {
			CompilationUnit cu = getCompilationUnit(fileList.get(i).toString());
			getMethod(cu, mdList);
			saveMethod(mdList,pathList.get(i));
			mdList.clear();
		}
	}
	private static void saveMethod(List<MethodDeclaration> mdList, String savePath) throws IOException {
		for(int i=0;i<mdList.size();i++) {
			String methodPath = savePath+"/"+i+".txt";
			File writeFile = new File(methodPath);
			writeFile.createNewFile();
			FileWriter fw = new FileWriter(writeFile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(mdList.get(i).toString());
		}
	}
	
	//获取保存路径
	private static void savePath(String filePath) {
		int lastIndex = filePath.lastIndexOf("/");
		String savePath = filePath.substring(0, lastIndex);
		pathList.add(savePath);
	}
	// 获取所有文件
	private static void fun(File file) {
		File[] files = file.listFiles();
		for(File fs:files) {
			if(fs.isDirectory()) {
				fun(fs);
			}else {
				fileList.add(fs);
			}
		}
	}
	
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

	private static void getMethod(ASTNode cuu,final List<MethodDeclaration> fd) {
		cuu.accept(new ASTVisitor() {
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				fd.add(node);
				return true;
			}
		});
	}
}
