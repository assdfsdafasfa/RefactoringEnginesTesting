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

import refactoring.Info.MoveMethodInfo;


public class MoveMethodData {
	public static List<MoveMethodInfo> readJson(String projectNameString) {
		List<MoveMethodInfo> moveMethodInfos = new ArrayList<>();
		Path jsonFilePath = Paths.get("D:/IDEA_build/saveFile/Json/"+ projectNameString +".json");
		try {
			String jsonContent = new String(Files.readAllBytes(jsonFilePath));
			JSONArray jsonArray = JSONObject.parseArray(jsonContent);
			List<Map<String, Object>> jsonListMap = JSON.parseObject(jsonContent,
					new TypeReference<List<Map<String, Object>>>() {
					});
			for(int i=0;i<jsonListMap.size();i++) {
				Map<String, Object> temp = jsonListMap.get(i); 
				int no = (int) temp.get("no");
				String projectName = temp.get("projectName").toString();
				String filePath = temp.get("filePath").toString();
				String methodName = temp.get("methodName").toString();
				int offset = (int) temp.get("offset");
				int length = (int) temp.get("length");
				String[] parameterTypes = (String[]) temp.get("parameterType");
				String typeName = temp.get("typeNames").toString(); 
				List<String> methodParameterTypes = (List<String>) temp.get("methdoParameterTypes"); 
				MoveMethodInfo renameMethodInfo = new MoveMethodInfo(no, methodName, projectName, filePath, offset, length,
										parameterTypes,typeName);
				moveMethodInfos.add(renameMethodInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return moveMethodInfos;
	}
}
