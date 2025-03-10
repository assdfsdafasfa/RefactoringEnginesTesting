package refactoring.random.select;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveRefactoringFile {

	public static void saveCode(String filePath, String code) throws IOException {
		Path path = Paths.get(filePath);
		// 检查目录是否存在，如果不存在则创建
		if (!Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		// 创建 BufferedWriter
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		// 写入字符串
		writer.write(code);
		// 关闭 BufferedWriter
		writer.close();
	}
}
