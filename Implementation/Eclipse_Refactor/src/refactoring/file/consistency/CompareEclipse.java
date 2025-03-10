package refactoring.file.consistency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import refactoring.Info.RenameMethodInfo;
import refactoring.rename.data.RenameMethodData;

public class CompareEclipse {
	static String eclipseFolderPath = "D:\\IDEA_build\\saveFile\\Eclipse\\commons-codec\\RenameMethod\\";
	static String ideaFolderPath = "D:\\IDEA_build\\saveFile\\IDEA\\commons-codec\\RenameMethod\\";

	public static void main(String[] args) {
		int sum = 0;
		String projectNameString = " ";
		List<String> diffList = new ArrayList<>();
		List<RenameMethodInfo> renameMethodInfos = RenameMethodData.readJson(projectNameString);
		for (int i = 0; i < renameMethodInfos.size(); i++) {
			int no = renameMethodInfos.get(i).getNo();
			String eclipseFolderPath1 = eclipseFolderPath + no;
			String ideaFolderPath1 = ideaFolderPath + no;
			System.out.println("PARH:" + eclipseFolderPath);
			if (getFolderName(eclipseFolderPath1)) {
				List<String> ecNameStrings = CompareFile.getFile(eclipseFolderPath1);
				for (int a = 0; a < ecNameStrings.size(); a++) {
					String ecString = javaString(eclipseFolderPath1 + "\\" + ecNameStrings.get(a));
					String methodName = renameMethodInfos.get(i).getMethod();
					if (methodName.contains("New1")) {
						if (!ecString.contains(methodName)) {
							diffList.add(eclipseFolderPath1);
						}
					} else {
						String methodName1 = methodName + "New1";
						if (!ecString.contains(methodName)) {
							diffList.add(eclipseFolderPath1);
						}
					}
				}
			}
		}
	System.out.println("不一致数量");
	for(int n=0;n<diffList.size();n++){
		System.out.println("Chayi:" + diffList.get(n));
	}
	}

	public static  boolean getFolderName(String filePath) {
		  File folder = new File(filePath);
	        if (folder.exists()) {
	            return true;
	        } 
	     return false;
	 }

	public static String javaString(String path){
		 	File file = new File(path);
	        StringBuilder result = new StringBuilder();
	        try{
	            // 构造一个BufferedReader类来读取文件
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String s = null;
	            // 使用readLine方法，一次读一行
	            while((s = br.readLine())!=null){
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }

}
