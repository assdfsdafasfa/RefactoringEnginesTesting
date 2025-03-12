package refactoring.file.parse;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
public class RecordTime {
	 public static boolean compareTime(LocalDateTime time1, LocalDateTime time2){
	        int comparisonResult = time1.compareTo(time2);
	        if (comparisonResult < 0) {
	            return true;
	        } else {
	            return  false;
	        }
	    }

	    public static List<Path> recordFileTime(List<IFile> fileList, LocalDateTime startTime) {
	        List<Path> modifierFilePath = new ArrayList<>();
	        for (int i = 0; i < fileList.size(); i++) {
	            IFile file = fileList.get(i);
	            long modificationTime = getFileModificationTime(file);
	            LocalDateTime modificationDateTime = LocalDateTime.ofInstant(
	                    java.util.Date.from(java.time.Instant.ofEpochMilli(modificationTime)).toInstant(),
	                    ZoneId.systemDefault()
	            );
	            if(compareTime(startTime, modificationDateTime)) {
	            	java.nio.file.Path path =  Paths.get(SaveFile.getLocalFilePath(file));
	            	modifierFilePath.add(path);
	            }
	        }
	        return modifierFilePath;
	    }
	    public static long getFileModificationTime(IFile file) {
	        // 获取文件的最新修改时间戳
			return file.getModificationStamp();
	    }
}
