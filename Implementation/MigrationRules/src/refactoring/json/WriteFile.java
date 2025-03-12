package refactoring.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WriteFile {
	public static void main(String[] args) {
		String folderPath = "C:\\Users\\m1523\\Desktop\\TestCase\\Data"; // 替换为你的文件夹路径
		String savePath = "C:\\Users\\m1523\\Desktop\\TestCase\\TestCase\\";
		int targetColumnIndex = 3; // 假设要读取第三列（索引从0开始）
		// 获取文件夹下所有CSV文件
		File folder = new File(folderPath);
		File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				int dotIndex = fileName.lastIndexOf(".");
				fileName = fileName.substring(0, dotIndex);
				System.out.println("Reading file: " + fileName);
				try {
					// 逐行读取CSV文件
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					while ((line = br.readLine()) != null) {
						// 以逗号为分隔符分割每行数据
						String[] columns = line.split(",");
						if (columns.length > targetColumnIndex) {
							if (!columns[targetColumnIndex].contains("id")) {
							// 输出指定列的数据
//							System.out.println(columns[targetColumnIndex]); // 输出第三列的数据

							File f = new File(savePath + fileName, fileName + columns[targetColumnIndex] + ".java");
							File parentDir = f.getParentFile();

							if (!parentDir.exists()) {
								boolean dirsCreated = parentDir.mkdirs(); // 创建目录
								if (dirsCreated) {
									System.out.println("Directories created successfully.");
								} else {
									System.out.println("Failed to create directories or directories already exist.");
								}
							}
							if (!f.exists()) {
								// 创建文件
								boolean isFileCreated = f.createNewFile();
								if (isFileCreated) {
									System.out.println("Java code written successfully!");
								}
							}
						}
						}
					}
					br.close(); // 关闭 BufferedReader
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("No CSV files found in the specified directory.");
		}
	}
}
