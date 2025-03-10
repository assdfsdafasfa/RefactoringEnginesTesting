package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderDeletion {
	public static void main(String[] args) {
		// 指定两个文件夹路径
		String parentFolderPath = "/path/to/parent/folder";
		String folderToDeletePath = "/path/to/folder/to/delete";

		// 将路径转换为 Path 对象
		Path parentFolder = Paths.get(parentFolderPath);
		Path folderToDelete = Paths.get(folderToDeletePath);

		// 检查要删除的文件夹是否存在于父文件夹中
		if (Files.exists(parentFolder) && Files.exists(folderToDelete) && parentFolder.startsWith(folderToDelete)) {
			// 如果要删除的文件夹存在于父文件夹中，则删除
			try {
				Files.walk(folderToDelete).sorted((p1, p2) -> -p1.compareTo(p2)) // 逆序遍历，确保删除子文件夹之前先删除父文件夹
						.forEach(path -> {
							try {
								Files.delete(path);
							} catch (Exception e) {
								System.err.println("Failed to delete: " + path);
							}
						});
				System.out.println("Folder deleted successfully.");
			} catch (Exception e) {
				System.err.println("Failed to delete folder: " + folderToDeletePath);
			}
		} else {
			System.out.println("Folder does not exist or is not under parent folder.");
		}
	}
}
