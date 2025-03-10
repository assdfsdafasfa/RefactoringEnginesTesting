package refactoring.random.gpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import refactoring.random.select.Constant;

public class GptApi {

	public static void main(String[] args) throws IOException, InterruptedException {
		String newName = printMessage(Constant.RENAME_METRHOD_PROMPT + "new ProcessBuilder(command);");
		// python传递的参数！
		System.out.println("str:" + newName);
	}

	public static String printMessage(String code) {
		String result = "";
		try {
			String[] command = new String[] { Constant.PYTHON_PATH, Constant.GPT_PATH, code };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			Process process = processBuilder.start();
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			BufferedReader bf = new BufferedReader(ir);
			result = bf.readLine();
			bf.close();
			ir.close();
		} catch (IOException e) {

		}
		return result;

	}
}
