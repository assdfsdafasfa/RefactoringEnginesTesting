/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.test.refactoring;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;
import org.netbeans.jmi.javamodel.*;
import org.netbeans.junit.diff.LineDiff;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RefactoringElement;
import org.netbeans.modules.refactoring.api.RefactoringSession;

import org.netbeans.junit.ide.ProjectSupport; // Key add this line

/** LogTestCase
 * @author Jan Becicka
 */
public class RefactoringTestCase extends LogTestCase {

    protected static TypeClass typeProxy;
    protected static JavaClass jc;
    
    private static boolean USE_DIFF;
    
    // Kely add these fields
    public File input;     
    public String projectName; // = "default"
    public String refactoringName; // = "encapsulatefield";
    public String subDirectoryName; // = "classwitharrayfield2";
    public String fileName; // = "A.java";
    public String[] tests;
    public static String workspaceDir; //= "C:\\1_Workspace\\Netbeans 6.0 M3\\refactoring\\test\\work\\sys\\data\\projects\\default\\src\\";
    public static String workDirectoryForEclipse;
    public static String tempDir;
    //String absolutePath;
    public static String outputDirectory;
    public int rangeOfTests;
    public int groupOfTests;
    
    public int numTotalInputTests;
    public int numTotalCompiledTests;
    public int numTotalRefactoredTests;
    public boolean statusOfTests[];// store the status of tests
    public int currentNumOfTest;// current # of test that is being run
    //public String currentErrorMessage;
    public BufferedWriter logFile;// logFile for the test
    public String baseName;// name of the .arg file
    public String errorMessages[];// store the error messages
    public String typeOfTest;// generatorTest or eclipseTest
    public long startTime;
    public long endTime;
    
    public RefactoringTestCase(java.lang.String testName) {
        super(testName);
        System.out.println("super.RefactoringTestCase>>>>>>>>>>");
        USE_DIFF = false;
        if (System.getProperty("use.diff") != null && System.getProperty("use.diff").equals("true")) {
            USE_DIFF=true;
        }        
    }
    
    protected void setUp() {
        super.setUp();
        setJavaClass("java.lang.String");
    }
    
    protected void setJavaClass(String name) {
        Utility.beginTrans(true);
        try {
            jc = (JavaClass) Utility.findClass(name);
            typeProxy = ((JavaModelPackage) jc.refOutermostPackage()).getType();
        } finally {
            Utility.endTrans();
        }
    }
    
    /**
     * Stores problems into log file.
     */
    public void logProblems(Problem problem) {
        Problem p=problem;
        if (p != null) {
            ArrayList list=new ArrayList();
            while (p != null) {
                if (p.isFatal()) {
                    list.add("Problem fatal: "+p.getMessage());
                } else {
                    list.add("Problem: "+p.getMessage());
                }
                p=p.getNext();
            }
            Collections.sort(list);
            for (int i=0;i < list.size();i++) {
                log(list.get(i));
            }
        }
    }
    
    /**
     * Stores problems into ref file. Problems should be sorted.
     * @return true if problem is not null and one of them is fatal
     */
    public boolean refProblems(Problem problem) {
        Problem p=problem;
        boolean ret=false;
        if (p != null) {
            ArrayList list=new ArrayList();
            while (p != null) {
                if (p.isFatal()) {
                    ret=true;
                    list.add("Problem fatal: "+p.getMessage());
                } else {
                    list.add("Problem: "+p.getMessage());
                }
                p=p.getNext();
            }
            Collections.sort(list);
            for (int i=0;i < list.size();i++) {
                ref(list.get(i));
            }
        }
        return ret;
    }
    
    /**
     * Makes diffs of files in map (before refactoring) with refactored files.
     * @param files map of resource to temporary file
     * @param result usages after refactoring
     */
    protected void compareResources(HashMap files, RefactoringSession session, String name, String renamed) throws Exception {
        Collection result = session.getRefactoringElements();
        ref("Found "+String.valueOf(result.size())+" occurance(s).");
        File folder=getWorkDir();
        Object[] ress=files.keySet().toArray();
        Arrays.sort(ress);
        for (int i=0;i < ress.length;i++) {
            if (ress[i].equals("empty")) {
                continue;
            }
            Res res=(Res)(files.get(ress[i]));
            File oldfile=res.backup;
            if (oldfile == null || !oldfile.exists()) { //resource has new name
                log("compareResources: backuped old file is null - changed resource ");
            }
            File actualfile=new File(classPathWorkDir, (String)ress[i]);
            if (!actualfile.exists()) { //moved
                if ((name.replace('.','/')+".java").equals(ress[i])) { //class name
                    actualfile=new File(classPathWorkDir, renamed.replace('.','/')+".java");
                } else if (((String)ress[i]).indexOf(name.replace('.','/')) == 0) { //package name
                    String rsnm=(String)ress[i]; // a/b/c/A.java
                    String nm=name.replace('.','/');  // a/b/c
                    rsnm=renamed.replace('.','/')+rsnm.substring(nm.length()); // b/b/c/A.java
                    actualfile=new File(classPathWorkDir, rsnm);
                }
            }
            ref("\n"+ress[i]+"\n");
            Collections.sort(res.texts);
            for (int j=0;j < res.texts.size();j++) {
                ref("      "+res.texts.get(j));
            }
            LineDiff ldiff=new LineDiff();
            File diff=File.createTempFile("xtest", "refactoring");
            if(actualfile.exists()) {
                ldiff.diff(actualfile, oldfile, diff);
                ref("\nFile diff:\n");
                ref(diff);
                diff.delete();
            }
            if(USE_DIFF) {
                Utility.copyFile(oldfile, new File(getWorkDir(), oldfile.getName()+".bak"));
                oldfile.delete();
                if(actualfile.exists()) Utility.copyFile(actualfile, new File(getWorkDir(), actualfile.getName()));
            } else {
                ref("Original file:");
                ref("-------------------------------------");
                ref(oldfile);
                ref("Modified file:");
                ref("-------------------------------------");
                if(actualfile.exists()) {                                        
                    ref(actualfile);
                } else {
                    ref("File was deleted");
                }
            }
        }
        ref("");
        Res empty=(Res)(files.get("empty"));
        if (empty != null) {
            for (int i=0;i < empty.texts.size();i++) {
                ref(empty.texts.get(i));
            }
        }
    }
    
    /**
     * makes map of files mapped to resources names of usages, files are backuped into tmp files
     */
    protected HashMap getResources(RefactoringSession session) throws Exception {
        Collection result = session.getRefactoringElements();
        ArrayList list=new ArrayList();
        Res empty=new Res();
        HashMap files=new HashMap();
        for (Iterator it=result.iterator();it.hasNext();) {
            Object o=it.next();
            if (o instanceof RefactoringElement) {
                RefactoringElement wue=(RefactoringElement) o;
                Element el = wue.getJavaElement();
                if (el != null && el.getResource() != null && el.getResource().isValid()) {
                    Res res;
                    if (!list.contains(el.getResource())) {
                        list.add(el.getResource());
                        res=new Res();
                        files.put(el.getResource().getName(), res);
                    } else {
                        res=(Res)(files.get(el.getResource().getName()));
                    }
                    res.texts.add(getDisplayText(wue));
                } else {
                    empty.texts.add(getDisplayText(wue));
                }
            }
        }
        files.put("empty", empty);
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Resource)o1).getName().compareTo(((Resource)o2).getName());
            }
        });
        File folder=getWorkDir();
        for (int i=0;i < list.size();i++) {
            Resource r=(Resource)(list.get(i));
            Res res=(Res)(files.get(r.getName()));
            String name=r.getName().replace('/', '_')+".test";
            name=name.replace('\\', '_');
            File fl=new File(folder, name);
            if (!fl.getParentFile().exists()) {
                fl.getParentFile().mkdirs();
            }
            PrintStream ps=new PrintStream(new FileOutputStream(fl));
            ps.print(r.getSourceText());
            ps.close();
            res.backup=fl;
        }
        return files;
    }
    
    protected void refUsages(RefactoringSession session) {
        Collection result = session.getRefactoringElements();
        ArrayList list=new ArrayList();
        HashMap map=new HashMap();
        for (Iterator it=result.iterator();it.hasNext();) {
            Object o=it.next();
            if (o instanceof RefactoringElement) {
                RefactoringElement wue=(RefactoringElement) o;
                Element el = wue.getJavaElement();
                if (el != null && el.getResource() != null) {
                    String s;
                    s=el.getResource().getName().replace(File.separatorChar,'/');
                    list=(ArrayList)(map.get(s));
                    if (list == null) {
                        list=new ArrayList();
                        map.put(s, list);
                    }
                    list.add(getDisplayText(wue));
                } else {
                    log("refUsages without resource");
                    log(getDisplayText(wue));
                    map.put(getDisplayText(wue), "");
                }
            }
        }
        ref("Found "+String.valueOf(result.size())+" occurance(s).");
        Object[] keys=map.keySet().toArray();
        Arrays.sort(keys);
        for (int i=0;i < keys.length;i++) {
            ref("");
            if (map.get(keys[i]) instanceof ArrayList) {
                ref(keys[i]);
                list=(ArrayList)(map.get(keys[i]));
                Collections.sort(list);
                for (int j=0;j < list.size();j++) {
                    ref("      "+list.get(j));
                }
            } else {
                ref(keys[i]);
            }
        }
        ref("");
    }
    
    protected String getDisplayText(RefactoringElement elm) {
        String app="";
        if (elm.getStatus() == RefactoringElement.WARNING) {
            app=" [ warning! ]";
        } else if (elm.getStatus() == RefactoringElement.GUARDED) {
            app=" [ error: code is in guarded block ]";
        }
        return elm.getDisplayText()+app;
    }
    
    protected String getModifier(int modifier) {
        String ret="";
        if (Modifier.isPublic(modifier)) {
            ret+="public";
        } else if (Modifier.isProtected(modifier)) {
            ret+="protected";
        } else if (Modifier.isPrivate(modifier)) {
            ret+="private";
        }
        if (Modifier.isAbstract(modifier)) {
            ret+=" abstract";
        } else if (Modifier.isFinal(modifier)) {
            ret+=" final";
        } else if (Modifier.isInterface(modifier)) {
            ret+=" interface";
        } else if (Modifier.isNative(modifier)) {
            ret+=" native";
        } else if (Modifier.isStatic(modifier)) {
            ret+=" static";
        } else if (Modifier.isSynchronized(modifier)) {
            ret+=" synchronized";
        } else if (Modifier.isTransient(modifier)) {
            ret+=" transient";
        } else if (Modifier.isVolatile(modifier)) {
            ret+=" volatile";
        }
        return ret.trim();
    }
    
    class Res {
        public File backup;
        public ArrayList texts;
        
        public Res() {
            texts=new ArrayList();
        }
    }
    
  // Kely add these methods 
  public ArrayList copyFiles(String path , String packageName ) throws IOException {    
                //System.out.println("copyFiles, path: "+ path + " packageName: "+ packageName);
  		File m = new File(path);
  		File[] listOfFiles = m.listFiles();
  		ArrayList list= new ArrayList();
                
                //System.out.println("listOfFiles.length: "+ listOfFiles.length);
  		for (int i = 0; i < listOfFiles.length; i++) {
                        //System.out.println("listOfFiles[i]: "+ listOfFiles[i]);
  			ArrayList l = listOfFiles(listOfFiles[i]);
                        //System.out.println("l: "+ l.size());
  			if (!l.isEmpty())
  				list.addAll(l);
  		}
                
                //System.out.println("list.size(): "+ list.size());
                String prefix = "";
                if (typeOfTest.equals("eclipseTest"))
                    prefix = workDirectoryForEclipse.replace("\\\\", File.separator) + File.separator + subDirectoryName.replace("\\\\", File.separator);
                else 
                    prefix = tempDir.replace("\\\\", File.separator) + File.separator + refactoringName + File.separator + subDirectoryName;   
  		for (int i = 0; i < list.size(); i++) {                        
  			File src = (File)list.get(i);
  			String absPath = src.getAbsolutePath();                          
  			String pathForClass = absPath.substring(0, absPath.indexOf(src.getName())-1);                        
                        int index = pathForClass.indexOf(prefix);
                        String auxPackage = pathForClass.substring(index+prefix.length()+1);
                        System.out.println("packageName: "+ packageName);
                        System.out.println("auxPackage: "+ auxPackage);
  			String className = src.getName().substring(0, src.getName().indexOf(".java"));
                        System.out.println("copyFiles before copyFile, src: "+ absPath);
            copyFile(absPath, packageName + "." + auxPackage.replace(File.separator, "."), workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + auxPackage + File.separator + src.getName());            
                        
  		}
                return list;
   }
    
    // Kely Add these required method for our tests
    public void copyFile(String srcForClass, String packageName, String dstForClass) throws IOException {
        System.out.println("copyFile, packageName: " + packageName);
        System.out.println(" srcForClass: "+ srcForClass);
        System.out.println(" dstForClass: "+ dstForClass);
        String rootForNetbeans = workspaceDir;
        File src = new File(srcForClass);
        boolean success;
        String packageNameAux = packageName;
        
        while (packageNameAux.indexOf(".") > 0) {
            int position = packageNameAux.indexOf(".");
            String directory = packageNameAux.substring(0, position);
            File newFile = new File(rootForNetbeans + directory);
            if (!newFile.exists()) newFile.mkdir();
            rootForNetbeans += directory + File.separator;
            packageNameAux = packageNameAux.substring(position + 1);
        }        
        
        File newFile = new File(rootForNetbeans + packageNameAux);        
        if (!newFile.exists()) newFile.mkdir();        
        rootForNetbeans += packageNameAux + File.separator;
        File dst = new File(dstForClass);         
        BufferedReader in2 = new BufferedReader(new FileReader(src));                        
        BufferedWriter out2 = new BufferedWriter(new FileWriter(dst));        
        String line;
        boolean packageLine = false;
        while ((line = in2.readLine()) != null) {            
            if (!line.equals("") && !packageLine && (line.indexOf("//") != 0)) {
                if (!packageName.equals("")) {
                    out2.write("package " + packageName + ";\n");
                    if (line.indexOf("package") < 0)
                        out2.write(line + "\n");
                } else
                    out2.write(line + "\n");
                packageLine = true;
            } else
                out2.write(line + "\n");
        }
        in2.close();
        out2.close();
    }
    
    public String[] newCopyFiles() throws IOException {
        System.out.println("start newCopyFiles>>>>>>");
        ArrayList list = new ArrayList();
        if (!input.isDirectory()) throw new RuntimeException("input not a directory");
        File[] testDirectories = input.listFiles();        
        for (int i = groupOfTests * (rangeOfTests - 1); i < (groupOfTests * rangeOfTests) && i < testDirectories.length ; i++) {            
            //numTotalInputTests++;
            File f = testDirectories[i];
            String name = f.getName();
            //list.add(name);            
            
            if (name.indexOf("test") == 0){// Kely add this condition to filtrate any other file                
                numTotalInputTests++;
                //absolutePath = f.getAbsolutePath();
                File out = new File(f.getAbsolutePath() + File.separator + "out" + File.separator + "PRE_REFACTOR_NOT_COMPILE");                            
                if (!out.exists()){                    
                    numTotalCompiledTests++;
                    list.add(name);
                    copyFiles(f.getAbsolutePath() + File.separator + "in", refactoringName + "." + subDirectoryName.replace("\\\\", "."));
//                    copyFile(f.getAbsolutePath() + File.separator + "in" +  File.separator + fileName,
//                        refactoringName + "." + subDirectoryName + "." + name, workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + name + File.separator + fileName);
                }
            }
        }
        String[] tests = (String[])list.toArray(new String[0]);
        java.util.Arrays.sort(tests);
        System.out.println("Copied files for test");
        return tests;
    }
    
//    public String[] newCopyFiles() throws IOException {
//        System.out.println("start newCopyFiles>>>>>>");
//        ArrayList list = new ArrayList();
//        if (!input.isDirectory()) throw new RuntimeException("input not a directory");
//        File[] testDirectories = input.listFiles();        
//        for (int i = groupOfTests * (rangeOfTests - 1); i < (groupOfTests * rangeOfTests) && i < testDirectories.length ; i++) {            
//            //numTotalInputTests++;
//            File f = testDirectories[i];
//            String name = f.getName();
//            //list.add(name);            
//            if (name.indexOf("test") == 0){// Kely add this condition to filtrate any other file                
//                numTotalInputTests++;
//                //absolutePath = f.getAbsolutePath();
//                File out = new File(f.getAbsolutePath() + File.separator + "out" + File.separator + "PRE_REFACTOR_NOT_COMPILE");            
//                        
//                if (!out.exists()){
//                    numTotalCompiledTests++;
//                    list.add(name);
//                    copyFile(f.getAbsolutePath() + File.separator + "in" +  File.separator + fileName,
//                        refactoringName + "." + subDirectoryName + "." + name, workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + name + File.separator + fileName);
//                }
//            }
//        }
//        String[] tests = (String[])list.toArray(new String[0]);
//        java.util.Arrays.sort(tests);
//        System.out.println("Copied files for test");
//        return tests;
//    }
   
    // this is a hack to get command-line arguments to this class through files
    // because we don't know how to send them otherwise
    public static String[] getArgsFromFile(String baseName) throws IOException {
        ArrayList args = new ArrayList();
        File argsFile = new File(".." + File.separator + ".." + File.separator + ".." + File.separator + "refactoring" + File.separator + "test" + File.separator + baseName + ".args");
        
        if (argsFile.exists()) {
            System.out.println("getArgsFromFile read file OK");
            BufferedReader in = new BufferedReader(new FileReader(argsFile));
            String line;
            while ((line = in.readLine()) != null)
                args.add(line);
            in.close();
        }        
        return (String[])args.toArray(new String[0]);
    }

    public static void getCommon_Parameters() throws IOException {
        //String tempdir = System.getProperty("java.io.tmpdir");
        String []data = getArgsFromFile("testOur_1_Common");
        //String tempdir= null;
        if (data.length == 4) {
            workspaceDir = data[0] + File.separator;
            tempDir = data[1];
            workDirectoryForEclipse = data[2];
            outputDirectory = data[3];
        } 
        if (tempDir == null) {
            System.out.println("Temp Dir is not directory");
            throw new IllegalArgumentException("Temp dir is not specified");
        }
        if (workDirectoryForEclipse == null) {
            System.out.println("workDirectoryForEclipse is not directory");
            throw new IllegalArgumentException("workDirectoryForEclipse is not specified");
        }
        
        if (outputDirectory == null) {
            System.out.println("outputDirectory is not directory");
            throw new IllegalArgumentException("outputDirectory is not specified");
        }
        
        String separator = System.getProperty("file.separator");
        if (!tempDir.endsWith(separator)) {
            tempDir += separator;
        }
        System.out.println("getSystemTempDir: "+ tempDir);
        //return tempdir;
    }
    
    public void initializeResults (int numOfTests, boolean b) {
        statusOfTests = new boolean[numOfTests];
        errorMessages = new String[numOfTests];
        for(int i=0; i < numOfTests; i++){
           statusOfTests[i]= false;
           errorMessages[i]="";
        }       
        if (b) tests = new String[numOfTests];
    }
    
    public void setArgsFromFile(String baseName, String typeOfTest) throws IOException {
        System.out.println(">>>>>> setArgsFromFile");
        String[] args = getArgsFromFile(baseName);          
        if (args.length == 6) {            
            projectName = args[0];
            refactoringName = args[1];            
            subDirectoryName = args[2];            
            fileName = args[3]; //             
            groupOfTests = Integer.parseInt(args[4]); //
            rangeOfTests = Integer.parseInt(args[5]); //
            initializeResults(groupOfTests, false);
            this.typeOfTest = typeOfTest;
        }
        getCommon_Parameters();
        String dirName;
        if (typeOfTest.equals("generatorTest")){
            dirName = tempDir + File.separator + refactoringName + File.separator + subDirectoryName;        
            logFile = new BufferedWriter(new FileWriter(tempDir + File.separator + refactoringName + File.separator + baseName + "_Log.txt", true));    
        } else if (typeOfTest.equals("eclipseTest")){
            dirName = workDirectoryForEclipse + File.separator + subDirectoryName;  
            logFile = new BufferedWriter(new FileWriter(workDirectoryForEclipse + File.separator + refactoringName + "_" + subDirectoryName.replace("\\\\", "_") + "_Log.txt", true));
        } else {
            System.out.println("typeOfTest can only be generatorTest or eclipseTest");
            throw new IllegalArgumentException("typeOfTest is not specified");
        }   
        input = new File(dirName);        
        initializeCounters();        
        this.baseName = baseName;        
        startTime = System.currentTimeMillis();
    }
    
    public void prepareProject() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>  Preparing the project");
        try {
            tests = newCopyFiles();
            cleanOutputDirectories(); 
        } catch (Exception e){
            System.out.println("Error "+ e);
        }
        ProjectSupport.openProject(new File(getDataDir(), "projects/" + projectName));
        classPathWorkDir=new File(getDataDir(), ("projects." + projectName + ".src").replace('.', File.separatorChar));        
    }
    
    /**protected void closeProject(String projectName) {
     * ProjectSupport.closeProject(projectName);
     * }*/
    
    public void cleanOutputDirectories(){
        String outputDir="";
        for (int i = 0; i < tests.length; i++) {
            // this lines are repeated
            if (typeOfTest.equals("generatorTest"))              
                outputDir = tempDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory;
            else if (typeOfTest.equals("eclipseTest"))
                outputDir = workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory;
            //System.out.println("outputDir is: "+ outputDir);
            deleteDirectory(new File(outputDir));
        }    
    }
    
    public void MoveFiles_Or_Directories(String srcDir, String dstDir){
        File originDir = new File(srcDir);
        File destinationDir = new File(dstDir);
        ArrayList l = listOfFiles(originDir); 
        for (int i = 0; i < l.size(); i++) {                        
            File src = (File)l.get(i);            
            MoveFile_Or_Directory(src, destinationDir);
 	}        
    }

    public boolean deleteDirectory(File dir) {        
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDirectory(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }
    
    public boolean MoveFile_Or_Directory(File src, File dst){        
        boolean success = src.renameTo(new File(dst, src.getName()));
        //if (!success) System.out.println("There was an error moving the file or directory, src is: "+ src);        
        return success;
    }      
    
    
    public void MoveFiles_Or_DeleteFiles (String srcDir, String dstDir, String className, boolean status, String errorMessage, int type) throws IOException {
          File dstFile = new File(dstDir);
          if (!dstFile.exists()) dstFile.mkdir();
          
          if (status){ // checking which was the status of the refactoring     
            System.out.println("MoveFiles_Or_DeleteFiles status true");  
            MoveFiles_Or_Directories(srcDir, dstDir);
            File info = null;
            if (type == 1)
                info = new File(dstDir, "REFACTORING_WAS_PERFORMED");            
            else 
                info = new File(dstDir, className + "_REFACTORING_WAS_PERFORMED");            
            if (!info.exists()) info.createNewFile();      
            
          } else {                        
            BufferedWriter out = null;
            if (type == 1)
                out = new BufferedWriter(new FileWriter(dstDir + File.separator + "REFACTORING_NOT_PERFORMED"));            
            else 
                out = new BufferedWriter(new FileWriter(dstDir + File.separator + className + "_REFACTORING_NOT_PERFORMED"));            
            out.write(errorMessage);
            out.close();                      
         
            deleteDirectory(new File(srcDir));
          }            
            if (new File(srcDir).exists()) new File(srcDir).delete();                    
    }
    
    protected void tearDown() {
      super.tearDown();      
      try { 
      String dstDir = "";     
      for (int i = 0; i < tests.length; i++) {
            //String srcDir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i] ;            
          String srcDir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + "in";            
            if (typeOfTest.equals("generatorTest"))              
              dstDir = tempDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i] + File.separator+ outputDirectory;
            else if (typeOfTest.equals("eclipseTest"))
              dstDir = workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory;
            System.out.println("TearDown: srcDir: "+ srcDir + " dstDir: "+ dstDir);
            MoveFiles_Or_DeleteFiles (srcDir, dstDir, "", statusOfTests[i], errorMessages[i], 1);
            deleteDirectory(new File(srcDir));            
      }              
      printSummaryCounters();        
        
      } catch (IOException e) {
          System.out.println("Error in printSummaryCounters: "+ e);
      }              
      //closeProject(getDataDir()+ "\\projects\\default");
    }  
    
    public void initializeCounters(){
        numTotalInputTests = 0;
        numTotalCompiledTests = 0;
        numTotalRefactoredTests = 0;        
    }    
    
   public void updateRangeNumber() throws IOException {
        BufferedReader in2 = new BufferedReader(new FileReader(".." + File.separator + ".." + File.separator + ".." + File.separator + "refactoring" + File.separator + "test" + File.separator + baseName + ".args"));        
        String lines="", line;
        int numOfLines=0;
        int numRange=0;
        while ((line = in2.readLine()) != null) {             
             if (numOfLines < 5){
                lines+=line+"\n";
                numOfLines++;
             } else
                numRange = Integer.parseInt(line);
        }        
        in2.close();
        numRange++;
        
        BufferedWriter out2 = new BufferedWriter(new FileWriter(".." + File.separator + ".." + File.separator + ".." + File.separator + "refactoring" + File.separator + "test" + File.separator + baseName + ".args"));
        out2.write(lines);
        out2.write("" + numRange);
        out2.close();        
   }    
    
    public void printSummaryCounters() throws IOException {
        try {        
        endTime = System.currentTimeMillis();
        double time = (endTime - startTime) / 1000.0;
        System.out.println("Results for Refactoring: " + refactoringName + " Type of Test: "+ subDirectoryName + " is >>>>>>>>>");        
        System.out.println("Total Time is seconds was: "+ time);
        System.out.println("The total # of input tests was: "+ numTotalInputTests);
        System.out.println("The total # of compileds tests was: "+ numTotalCompiledTests);
        System.out.println("The total # of refactored tests was: "+ numTotalRefactoredTests);
        
        logFile.write("\n\n Summary \n\n");
        logFile.write("Results for Refactoring: " + refactoringName + " Type of Test: "+ subDirectoryName + " is >>>>>>>>>\n");
        logFile.write("Total Time in seconds was: "+ time + "\n");
        logFile.write("Process Files, Range: "+ rangeOfTests + " Group of tests: "+ groupOfTests + "\n\n");
        logFile.write("The total # of input tests was: "+ numTotalInputTests + "\n");
        logFile.write("The total # of compileds tests was: "+ numTotalCompiledTests + "\n");
        logFile.write("The total # of refactored tests was: "+ numTotalRefactoredTests + "\n\n");
        //System.out.println("before writing tests not refactored");                
        for (int i=0; i < tests.length; i++) {
            if (!statusOfTests[i]) {
                System.out.println("i: "+ i);
                System.out.println("Test: "+ tests[i] + " was not refactored");
                logFile.write("Test: " + tests[i] + " was not refactored \t (" + errorMessages[i] + ")\n");
            }
        }        
        logFile.close();
        
        if (typeOfTest.equals("generatorTest")) updateRangeNumber();
        
        System.out.println("finish printSummaryCounters>>>>>>>>>");
        } catch (Exception e){
            System.out.println("Error in printSummaryCounters: "+ e);
        }
    }

 
    
//    public int compileClass(String className){
//        int errorCode = com.sun.tools.javac.Main.compile(new String[] {
//            "-classpath", "bin",
//            "-d", "/temp/dynacode_classes",
//            "dynacode/sample/PostmanImpl.java" });
//        return errorCode;        
//    }
    
    // For Eclipse's tests
    public ArrayList listOfFiles(File input) {
        ArrayList list = new ArrayList();
        if (input.isDirectory()) {
            File[] listOfFiles = input.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                ArrayList listAux = listOfFiles(listOfFiles[i]);
                for (int j = 0; j < listAux.size(); j++) {
                    File f = (File)listAux.get(j);
                    if (f.isDirectory()) list.add(f);
                    else if (f.getAbsolutePath().indexOf(".java") > 0) list.add(f);
                }
            }
        }
        //System.out.println("input.isFile():"+ input.isFile());
        //System.out.println("input.getAbsolutePath().indexOf(.java) > 0: "+ (input.getAbsolutePath().indexOf(".java") > 0));
        //System.out.println("(input.getAbsolutePath().indexOf(out) < 0): " + (input.getAbsolutePath().indexOf("out") < 0));
        if (input.isFile() && (input.getAbsolutePath().indexOf(".java") > 0) && (input.getAbsolutePath().indexOf("//out") < 0)){
            //System.out.println("input is added to list: "+ input.getName());
            list.add(input);
        }    
        return list;
    }
    
}

