package json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ResultJson {
	public static JSONArray jsonData = new JSONArray();
	public static void handleAPIJsonFile(int no,boolean isSuccess) {
		JSONObject jsonObject = new JSONObject(new LinkedHashMap());
		jsonObject.put("no", no);
		jsonObject.put("isSuccess", isSuccess);
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
