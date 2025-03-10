package refactoring.file.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public class SaveFile {
	public static void saveContentToFile(String filePath, String content) {
		try {
			// 使用 java.nio.file.Path 创建文件夹路径
			java.nio.file.Path folderPath = Paths.get(filePath).getParent();
			// 如果文件夹路径不存在，则创建
			if (!Files.exists(folderPath)) {
				Files.createDirectories(folderPath);
			}
			// 使用 java.nio.file.Path 创建文件路径
			java.nio.file.Path filePathObj = Paths.get(filePath);

			// 如果文件不存在，则创建
			if (!Files.exists(filePathObj)) {
				Files.createFile(filePathObj);
				// 示例：写入一些内容到文件
				writeContentToFile(filePath, content);
				System.out.println("file write finish--:" + filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeContentToFile(String filePath, String content) {
		try {
			// 创建 FileWriter
			FileWriter fileWriter = new FileWriter(filePath);

			// 将内容写入文件
			fileWriter.write(content);

			// 关闭 FileWriter
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getLocalFilePath(IFile file) {
		// 获取文件的本地文件系统路径
		IPath location = file.getLocation();
		if (location != null) {
			// 获取本地文件系统路径的字符串表示
			return location.toOSString();
		} else {
			// 如果获取不到本地路径，可以根据项目的位置和文件的相对路径拼接
			// 请替换为实际的项目名和相对路径
			return "D:\\AllProject\\RefactoringProject\\java\\" + file.getProjectRelativePath().toString();
		}
	}

	// Check whether the file is in the save path, if false, save the file to a
	// specific folder
	public static void saveAllModifierFile(List<Path> filePath, String folderPath) {
		for (int i = 0; i < filePath.size(); i++) {
			Path destinationPath = Paths.get(folderPath).resolve(filePath.get(i).getFileName());
			// 检查目标文件夹是否存在，不存在则创建
			if (!Files.exists(destinationPath)) {
				try {
					Files.createDirectories(destinationPath.getParent());
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			// 检查文件是否存在于目标文件夹
			if (!Files.exists(destinationPath)) {
				try {
					// 复制文件到目标文件夹
					Files.copy(filePath.get(i), destinationPath, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("File copied successfully.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("File already exists in the destination folder.");
			}
		}
	}

}
