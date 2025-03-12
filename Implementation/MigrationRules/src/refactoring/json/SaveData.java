package refactoring.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.LinkedHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SaveData {
	static JSONArray jsonData = new JSONArray();

	private int no;
	private String refactoringType;
	private String oldName;
	private String newName;
	private String className;
	private String path;
	private String projectName;
	private int offset;
	private int length;

	public SaveData(RefactoringRecord refactoringRecord) {
		JSONObject jsonObject = new JSONObject(new LinkedHashMap());
		jsonObject.put("no", refactoringRecord.getNo());
		jsonObject.put("path", refactoringRecord.getPath());
		jsonObject.put("project name", refactoringRecord.getProjectName());
		jsonObject.put("class name", refactoringRecord.getClassName());
		jsonObject.put("refactoring type", refactoringRecord.getRefactoringType());
		jsonObject.put("old name", refactoringRecord.getOldName());
		jsonObject.put("new name", refactoringRecord.getNewName());
		jsonObject.put("offset", refactoringRecord.getOffset());
		jsonObject.put("length", refactoringRecord.getLength());
		jsonData.add(jsonObject);
	}

	public static boolean createJsonFile(String filePath) throws FileNotFoundException, UnsupportedEncodingException {
		String content = JSON.toJSONString(jsonData, SerializerFeature.PrettyFormat,
				SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
		boolean flag = true;
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(content);
			write.flush();
			write.close();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

}
