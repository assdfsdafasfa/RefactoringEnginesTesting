package rendon.selecet.generation.prompt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RulePattern {
	public static void main(String[] args) {
		String pathString = "D:\\aa_桌面文件\\RefactoringTest测试用例\\rule.txt";
		List<String> readData = readData(pathString);
		for (String bugMess : readData) {
//			String bugMess = "RenameMethodRefactoring_no_binary=Related method ''{0}''(declared in ''{1}'') is binary. Refactoring cannot be performed.";
//			System.out.println("bugmes:" + bugMess);
			matchString(bugMess);
		}
	}

	public static void matchString(String bugMessage) {
		// move method
		if (bugMessage.contains("cannot be performed")) {
			int isPosition = bugMessage.indexOf("is");
			int endPosition = bugMessage.indexOf(".");
			String errorReason = bugMessage.substring(isPosition + 2, endPosition);
			System.out.println("error reason: " + errorReason);
		}
	}

	public static List<String> readData(String path) {
		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			// 逐行读取文本文件内容
			while ((line = reader.readLine()) != null) {
				// 对每一行数据进行处理，这里简单地输出到控制台
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	// move method
	// "cannot be moved" "cannot be used to move" "Cannot move" "could not be
	// resolved" "conflicts" "could not be fully resolved" "cannot be updated"
	// "already exists"

	// error reason:
//			error reason:  syntax errors
//			error reason:  binary
//			error reason:  read-only
}
