package Utils;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.psi.PsiFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveFile {
    public static void saveCurrentFileToNewFile(Map<String, VirtualFile> virtualFileMap, Project project, String filePath, String newFilePath) {
        boolean isFileExist = false;
        for (Map.Entry<String, VirtualFile> entry : virtualFileMap.entrySet()) {
            VirtualFile currentFile = entry.getValue();
            String currentFilePath = entry.getKey();
            if (currentFilePath.equals(filePath)) {
                isFileExist = true;
                if (currentFile != null) {
                    String fileContent = FileDocumentManager.getInstance().getDocument(currentFile).getText();
                    try {
                        Path path = Paths.get(newFilePath);
                        Files.createDirectories(path.getParent());
                        Files.write(path, fileContent.getBytes());
                        System.out.println("File created successfully at: " + path.toAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("存在filePath，但是currentFile为null");
                }
            }
        }
        if(isFileExist){

        }else{
            System.out.println("File not found, cannot be saved");
            System.out.println("filePath-"+filePath);
        }
    }

    public static void saveCurrentFileToNewFile(Project project, String filePath, String newFilePath) {
        boolean isFileExist = false;
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        VirtualFile[] openFiles = fileEditorManager.getOpenFiles();
        for (VirtualFile currentFile : openFiles) {
            if (currentFile.getPath().equals(filePath)) {
                isFileExist = true;
                if (currentFile != null) {
                    String fileContent = FileDocumentManager.getInstance().getDocument(currentFile).getText();
                    try {
                        Path path = Paths.get(newFilePath);
                        Files.createDirectories(path.getParent());
                        Files.write(path, fileContent.getBytes());
                        System.out.println("File created successfully at: " + path.toAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("存在filePath，但是currentFile为null");
                }
            }
        }
        if(isFileExist){

        }else{
            System.out.println("File not found, cannot be saved");
            System.out.println("filePath-"+filePath);
            for (VirtualFile currentFile : openFiles) {
                System.out.println("currentFile-"+currentFile);
            }
        }
    }
    public static void saveCurrentFileToNewFile1(Project project, String filePath, String newFilePath, String className) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        VirtualFile[] openFiles = fileEditorManager.getOpenFiles();
        System.out.println("11111");
        for (VirtualFile currentFile : openFiles) {
            System.out.println("currentFileName:"+currentFile.getName());
            if (currentFile.getName().contains(className)) {
                if (currentFile != null) {
                    String fileContent = FileDocumentManager.getInstance().getDocument(currentFile).getText();
                    System.out.println("fileContent:"+fileContent);
                    try {
                        Path path = Paths.get(newFilePath);
                        Files.createDirectories(path.getParent());
                        Files.write(path, fileContent.getBytes());
                        System.out.println("File created successfully at: " + path.toAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Check whether the file is in the save path, if false, save the file to a specific folder
    public static void saveAllModifierFile(List<Path> filePath, String folderPath) throws IOException {
        try {
            Path path = Paths.get(folderPath);
            for(int i=0; i< filePath.size();i++) {
                if (!isFileInFolder(filePath.get(i), path)) {
                    copyFileToFolder(filePath.get(i), path);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isFileInFolder(Path filePath, Path folderPath) {
        Path normalizedFilePath = filePath.normalize();
        Path normalizedFolderPath = folderPath.normalize();
        return normalizedFilePath.startsWith(normalizedFolderPath);
    }
    public static void copyFileToFolder(Path sourceFilePath, Path targetFolderPath) throws IOException {
        try {
            Path targetFilePath = targetFolderPath.resolve(sourceFilePath.getFileName());
            Files.copy(sourceFilePath, targetFilePath);
        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists in the target folder.");
        }
    }

    public static Map<String, VirtualFile> getVirtualFile(Project project){
        Map<String, VirtualFile> virtualFileMap = new HashMap<>();
        VirtualFile baseDir = project.getBaseDir();
        VfsUtil.visitChildrenRecursively(baseDir, new VirtualFileVisitor<Object>() {
            @Override
            public boolean visitFile(VirtualFile file) {
                virtualFileMap.put(file.getPath(),file);
                return true;
            }
        });
        return virtualFileMap;
    }
}
