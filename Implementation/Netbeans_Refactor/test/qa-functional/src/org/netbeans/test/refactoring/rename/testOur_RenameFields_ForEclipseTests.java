package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;

public class testOur_RenameFields_ForEclipseTests extends RenameTestCase {

    public testOur_RenameFields_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenameFields_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    

    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenameFields_ForEclipseTests.class);
 }

    public void test_1() throws Exception {
        System.out.println("tests.length: "+ tests.length);
        String field="f";
        String newField = "g";
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest                                                  
                System.out.println("before renameField, tests[i]: "+ tests[i]);
// Added for RenameNonPrivate                
                if (tests[i].equals("test18")){
                   field = "field"; newField="member";
                } else if (tests[i].equals("test19") || tests[i].equals("test20")){
                   field = "list"; newField="items"; 
                } else if (tests[i].equals("test21")){
                   field = "fValue"; newField="fOrdinal"; 
                } else if (tests[i].equals("test22")){
                   field = "tee"; newField="thing"; 
                } else if (tests[i].equals("testAnnotation1") || tests[i].equals("testAnnotation2")){
                   field = "ZERO"; newField="ZORRO"; 
                } else if (tests[i].equals("testBug5821")){
                   field = "test"; newField="test1"; 
                } else if (tests[i].equals("testGenerics4")){
                   field = "count"; newField="number"; 
                } 
                else {
                    field = "f"; newField="g";
                }   
// Added for RenamePrivate                   
//                if (tests[i].equals("testFail8")){
//                     field = "gg"; newField ="f";
//                } else if (tests[i].equals("test3")){
//                    field = "f"; newField ="gg";
//                } else if (tests[i].equals("test4") || tests[i].equals("test5") || tests[i].equals("test6")){
//                    field = "fMe"; newField ="fYou";
//                } else if (tests[i].equals("test7") || tests[i].equals("test9")){
//                    field = "fBig"; newField ="fSmall";
//                } else if (tests[i].equals("test10") || tests[i].equals("test11")){
//                    field = "fList"; newField ="fElements";
//                } else {
//                    field = "f";  newField="g";
//                }
                renameField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".A", field, newField, true, false);
                System.out.println("***************************" + i);                
            } catch (Exception e) {
                errorMessages[currentNumOfTest]=e.toString();
                System.out.println("fail test ***************************" + i + " error " + e);
            }
        }
    }
     
 
 
   protected void tearDown() {             
      //super.tearDown();
      System.out.println("tearDown>>>>>");        
      try {
      String fileName = "A.java";      
      for (int i = 0; i < tests.length; i++) {
            String src = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + fileName;
            String dst = workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory;// +  File.separator + fileName;
            File dstFile = new File(dst);
            if (!dstFile.exists()) dstFile.mkdir();
            else {
              File dstPrevious = new File(dstFile.getAbsolutePath(), fileName);
              if (dstPrevious.exists()) dstPrevious.delete();
            }
          
          if (statusOfTests[i]){// Kely add this condition            
            if (new File(src).exists()) {                
                System.out.println("src.exists(): "+ new File(src).exists());
                if (!MoveFile_Or_Directory(new File(src), new File(dst))) System.out.println("There was a problem moving test# " + i);
            }        
                        
            File info = new File(workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory, "REFACTORING_WAS_PERFORMED");            
            if (!info.exists()) info.createNewFile();            
            
            String dir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i];
            new File(dir).delete();
          } else {            
            if (new File(src).exists()) new File(src).delete();    
            System.out.append("before writing out");
            BufferedWriter out = new BufferedWriter(new FileWriter(workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i]+ File.separator + outputDirectory + File.separator + "REFACTORING_NOT_PERFORMED"));
            System.out.append("after writing out");
            out.write(errorMessages[i]);
            out.close();
            
            String dir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i];
            if (new File(dir).exists()) new File(dir).delete();
          }
      }
              
        printSummaryCounters();
      } catch (IOException e) {
          System.out.println("Error in printSummaryCounters: "+ e);
      }              
        
    } 


        
}
