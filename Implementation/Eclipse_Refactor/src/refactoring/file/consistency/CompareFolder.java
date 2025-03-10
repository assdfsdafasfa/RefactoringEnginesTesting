package refactoring.file.consistency;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CompareFolder {
	 static String eclipseFolderPath = "D:\\IDEA_build\\saveFile\\Eclipse\\commons-codec\\RenameMethod\\"; 
	 static String ideaFolderPath = "D:\\IDEA_build\\saveFile\\IDEA\\commons-codec\\RenameMethod\\"; 
	 public static void main(String[] args) throws ParseException, IOException {
		 List<String> eclipseFolder = getFolderName(eclipseFolderPath);
		 List<String> IDEAFolder = getFolderName(ideaFolderPath);
		 List<String> diffList = new ArrayList<>();
		 int sum=0;
		 for(int i=0;i<IDEAFolder.size();i++) {
			 if(listContains(eclipseFolder, IDEAFolder.get(i))) {
				 String ecPath = eclipseFolderPath+ IDEAFolder.get(i)+"\\";
				 String idPath = ideaFolderPath + IDEAFolder.get(i)+"\\";
				 List<String> ecNameStrings = CompareFile.getFile(ecPath);
				 List<String> idNameStrings = CompareFile.getFile(idPath);
				 if(ecNameStrings.size()!= idNameStrings.size()) {
					 System.out.println("更改文件数量不一致："+ecPath);
					 diffList.add(ecPath);
					 sum+=1;
				 }else {
					 for(int a=0;a<ecNameStrings.size();a++) {
						 if(listContains(idNameStrings, ecNameStrings.get(a))){
							 int num = getIndexByIndexOf(idNameStrings, ecNameStrings.get(a));
							 
					            List<String> linesEclipse = Files.readAllLines(Paths.get(ecPath+ecNameStrings.get(a)));
					            System.out.println("path:"+idPath+"num:"+num);
					            List<String> linesIDEA = Files.readAllLines(Paths.get(idPath+idNameStrings.get(num)));
					            // 比较两个文件的内容是否相同
					            boolean filesAreEqual = compareFileContents(linesEclipse, linesIDEA);

					            if (filesAreEqual) {
					                System.out.println("The files are identical.");
					            } else {
					            	sum+=1;
					            	diffList.add(ecPath);
					                System.out.println("The files are different.");
//					                System.out.println("---"+ecPath+ecNameStrings.get(a));
					            }
							 
						 }else {
							 sum+=1;
							 diffList.add(ecPath);
							 System.out.println("更改文件不一致："+ecPath);
						 }
					 }
				 }
			 }
//			 else {
//				 System.out.println("eclipse不包含："+IDEAFolder.get(i));
//			 }
		 }
		 System.out.println("不一致数量");
	 }
	 
	 public static <T> int getIndexByIndexOf(List<T> list, T target) {
		    return list.indexOf(target);
		}
	 
	  public static boolean listContains(List<String> list, String value) {
	        return list.contains(value);
	    }
	 
	 public static  List<String> getFolderName(String filePath) {
		  List<String> folderList = new ArrayList<>();
		  File folder = new File(filePath);
	        // 检查文件夹是否存在
	        if (folder.exists() && folder.isDirectory()) {
	            File[] subdirectories = folder.listFiles(File::isDirectory);
	            // 打印文件夹名称
	            if (subdirectories != null) {
	                for (File subdirectory : subdirectories) {
	                    folderList.add(subdirectory.getName());
	                }
	            }
	        } else {
	            System.out.println("指定的路径不是一个存在的文件夹。");
	        }
	     return folderList;
	 }
	 public static boolean compareFileContents(List<String> lines1, List<String> lines2) {
	        // 检查行数是否相同
	        if (lines1.size() != lines2.size()) {
	            return false;
	        }
	        // 逐行比较文件内容
	        for (int i = 0; i < lines1.size(); i++) {
	        	String content1 = removeWhitespace(lines1.get(i));
	        	String content2 = removeWhitespace(lines2.get(i));
	            if (!content1.equals(content2)) {
	                return false;
	            }
	        }
	        return true;
	    }
	 private static String removeWhitespace(String input) {
	        // 移除空格
	        return input.replaceAll("\\s", "");
	    }
}
