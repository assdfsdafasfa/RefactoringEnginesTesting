package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompleteFile {

	public static void main(String[] args) {
		String folderPath = "D:/top100copy/";
//		String fileSavePath = "D:/RefactoringDataset/";
		List<String> folderName = getFolderList(folderPath);

		for (String projectName : folderName) {
			String path = folderPath + projectName + "/.project";
//			System.out.println(projectName);
			File projectFile = new File(path);
			if (!projectFile.exists()) {
				String writeString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projectDescription>\r\n"
						+ "	<name>" + projectName + "</name>\r\n" + "	<comment></comment>\r\n" + "	<projects>\r\n"
						+ "	</projects>\r\n" + "	<buildSpec>\r\n" + "		<buildCommand>\r\n"
						+ "			<name>org.eclipse.jdt.core.javabuilder</name>\r\n" + "			<arguments>\r\n"
						+ "			</arguments>\r\n" + "		</buildCommand>\r\n" + "	</buildSpec>\r\n"
						+ "	<natures>\r\n" + "		<nature>org.eclipse.jdt.core.javanature</nature>\r\n"
						+ "	</natures>\r\n" + "</projectDescription>";

				String fileSavePath = "D:/top100copy/" + projectName + "/.project";
				wirteFile(writeString, fileSavePath);
			}
		}
	}

	public static List<String> getFolderList(String folderPath) {
		List<String> folderList = new ArrayList<>();
		File folder = new File(folderPath);
		if (folder.isDirectory()) {
			String[] subFolders = folder.list((current, name) -> new File(current, name).isDirectory());
			for (String subFolder : subFolders) {
				folderList.add(subFolder);
			}
		}
		return folderList;
	}

	public static void wirteFile(String write, String path) {
		try {
			FileWriter writer = new FileWriter(path);
			writer.write(write);
			writer.close();
			System.out.println(".project 文件写入成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
