package refactoring.file.consistency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompareProject {
	 public static void main(String[] args) {
	        String project1Path = "path/to/project1";
	        String project2Path = "path/to/project2";

	        try {
	            List<Path> commonJavaFiles = findCommonJavaFiles(project1Path, project2Path);

	            for (Path javaFile : commonJavaFiles) {
	                boolean areContentsEqual = compareFileContents(javaFile, Paths.get(project2Path, javaFile.getFileName().toString()));
	                System.out.println(javaFile + " contents are equal: " + areContentsEqual);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private static List<Path> findCommonJavaFiles(String project1Path, String project2Path) throws IOException {
	        try (Stream<Path> project1Files = Files.walk(Paths.get(project1Path), Integer.MAX_VALUE);
	             Stream<Path> project2Files = Files.walk(Paths.get(project2Path), Integer.MAX_VALUE)) {

	            List<String> project1JavaFileNames = project1Files
	                    .filter(Files::isRegularFile)
	                    .filter(path -> path.toString().endsWith(".java"))
	                    .map(path -> path.getFileName().toString())
	                    .collect(Collectors.toList());

	            return project2Files
	                    .filter(Files::isRegularFile)
	                    .filter(path -> path.toString().endsWith(".java"))
	                    .filter(path -> project1JavaFileNames.contains(path.getFileName().toString()))
	                    .collect(Collectors.toList());
	        }
	    }

	    private static boolean compareFileContents(Path file1, Path file2) throws IOException {
	        String content1 = new String(Files.readAllBytes(file1));
	        String content2 = new String(Files.readAllBytes(file2));

	        return content1.equals(content2);
	    }
}
