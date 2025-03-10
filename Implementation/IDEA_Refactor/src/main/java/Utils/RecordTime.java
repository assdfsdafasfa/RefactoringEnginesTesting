package Utils;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordTime {
    public static boolean compareTime(LocalDateTime time1, LocalDateTime time2){
        int comparisonResult = time1.compareTo(time2);
        if (comparisonResult < 0) {
            return true;
        } else {
            return  false;
        }
    }

    public static List<Path> recordFileTime(List<PsiFile> psiFileList, LocalDateTime startTime) {
        List<Path> modifierFilePath = new ArrayList<>();
        for (int i = 0; i < psiFileList.size(); i++) {
            VirtualFile virtualFile = psiFileList.get(i).getVirtualFile();
            if (virtualFile != null) {
                Path filePath = FileSystems.getDefault().getPath(virtualFile.getPath());
                try {
                    BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
                    LocalDateTime modificationTime = attrs.lastModifiedTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                    if(compareTime(startTime, modificationTime)){
                        modifierFilePath.add(filePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return modifierFilePath;
    }

}
