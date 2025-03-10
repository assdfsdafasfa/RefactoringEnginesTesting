package refactoring.file.consistency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import refactoring.Info.ExpressionInfo;
import refactoring.read.data.ExtractVariableData;

public class CompareExtractVariableJson {

	static Map<String, String> dataMap = new HashMap<>();
	static Map<String, String> errorMap = new HashMap<>();
	public static void main(String[] args){
		String eclipseFilePath = "D:\\IDEA_build\\saveFile\\Json\\IEMS_Java_Test_Eclipse_ExtactVariable.txt";
		String ideaFilePath = "D:\\IDEA_build\\saveFile\\Json\\IEMS_Java_Test_ExtactVariable.json";
		readEclipseData(eclipseFilePath);
		List<ExpressionInfo> exprssionInfos = readJson(ideaFilePath);
		
		for (Entry<String, String> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            for(int i=0;i<exprssionInfos.size();i++) {
            	ExpressionInfo  expressionInfo = exprssionInfos.get(i);
            	if(String.valueOf(expressionInfo.getNo()).equals(key)) {
                  String value = entry.getValue();
            	  if(!value.equals(String.valueOf(expressionInfo.isSuccess()))) {
            		  errorMap.put(key, value);
            		}else {
            			System.out.println("no:"+key);
            		}
            	  break;
            	}
            }
        }
		System.out.println("------------------------------");
		for(Entry<String, String> entry : errorMap.entrySet()) {
			String keyString = entry.getKey();
			System.out.println("不一致："+" no:"+keyString);
		}
	}
	
	public static List<ExpressionInfo> readJson(String ideaFilePath) {
		List<ExpressionInfo> extractExpressionInfos = new ArrayList<>();
		Path jsonFilePath = Paths.get(ideaFilePath);
		try {
			String jsonContent = new String(Files.readAllBytes(jsonFilePath));
			JSONArray jsonArray = JSONObject.parseArray(jsonContent);
			List<Map<String, Object>> jsonListMap = JSON.parseObject(jsonContent,
					new TypeReference<List<Map<String, Object>>>() {
					});
			for(int i=0;i<jsonListMap.size();i++) {
				Map<String, Object> temp = jsonListMap.get(i); 
				int no = (int) temp.get("no");
				boolean success = (boolean) temp.get("success"); 
				ExpressionInfo renameMethodInfo = new ExpressionInfo(no, success);
				extractExpressionInfos.add(renameMethodInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return extractExpressionInfos;
	}
	
	public static void readEclipseData(String filePath) {
		Path path = Paths.get(filePath);
		try {
            // 使用 Files 读取所有行到 List<String>
            List<String> lines = Files.readAllLines(path);

            // 遍历输出每一行
            for (String line : lines) {
            	 String[] words = line.split(" ");
            	 int pos1 = words[0].indexOf(":");
            	 int pos2 = words[1].indexOf(":");
            	 
            	 words[0].substring(pos1+1);
            	 words[1].substring(pos2+1);
            	 
            	 dataMap.put(words[0].substring(pos1+1), words[1].substring(pos2+1));
                 // 遍历输出拆分后的单词
//                 for (String word : words) {
//                	 int pos1 = word.indexOf(":");
//                     System.out.println(word.substring(pos+1));
//                 }
            }
		}catch (IOException e) {
            e.printStackTrace();
        }
	}
}
