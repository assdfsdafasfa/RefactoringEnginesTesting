package refactoring.read.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import refactoring.Info.ExpressionInfo;
import refactoring.Info.ExtractMethodInfo;

public class ExtractMethodData {
	public static List<ExtractMethodInfo> readJson(String projectName) {
		List<ExtractMethodInfo> extractMethodDatas = new ArrayList<>();
		Path jsonFilePath = Paths.get("D:/IDEA_build/saveFile/Json/"+ projectName +".json");
		try {
			String jsonContent = new String(Files.readAllBytes(jsonFilePath));
			JSONArray jsonArray = JSONObject.parseArray(jsonContent);
			List<Map<String, Object>> jsonListMap = JSON.parseObject(jsonContent,
					new TypeReference<List<Map<String, Object>>>() {
					});
			for(int i=0;i<jsonListMap.size();i++) {
				Map<String, Object> temp = jsonListMap.get(i); 
				int no = (int) temp.get("no");
				String proName = temp.get("projectName").toString();
				String filePath = temp.get("filePath").toString();
				String methodName = temp.get("methodName").toString();
				int offset = (int) temp.get("offset");
				int length = (int) temp.get("length");
				String typeName = temp.get("typeName").toString();
				ExtractMethodInfo renameMethodInfo = new ExtractMethodInfo(no, methodName, proName, filePath, offset, length,typeName);
				extractMethodDatas.add(renameMethodInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return extractMethodDatas;
	}
}
