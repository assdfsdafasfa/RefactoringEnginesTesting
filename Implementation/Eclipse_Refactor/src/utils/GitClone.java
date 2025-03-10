package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GitClone {
	public static void main(String[] args) {
		List<String> projectList = getTxt();
		// 设置克隆命令
		for (String str : projectList) {
			String temp = "https://github.com/" + str + ".git";
			String[] gitCloneCommand = { "git", "clone", temp };
			// 创建 ProcessBuilder 对象，并设置命令
			ProcessBuilder processBuilder = new ProcessBuilder(gitCloneCommand);
			processBuilder.directory(new File("D:/top100copy"));

			try {
				// 启动进程
				Process process = processBuilder.start();

				// 读取进程输出
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}

				// 等待命令执行完毕
				int exitCode = process.waitFor();
				if (exitCode == 0) {
					System.out.println(str + ": Git clone completed successfully.");
				} else {
					System.err.println("Git clone failed with error code: " + exitCode);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> getTxt() {
		String filePath = "D:\\top100copy\\top101.txt";

		// 创建一个 List 来存储文件内容
		List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			// 逐行读取文件内容并添加到 List 中
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
}
