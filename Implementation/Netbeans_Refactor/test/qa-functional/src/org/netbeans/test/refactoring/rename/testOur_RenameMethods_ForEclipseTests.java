package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;


public class testOur_RenameMethods_ForEclipseTests extends RenameTestCase {

    public testOur_RenameMethods_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenameMethods_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    

    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenameMethods_ForEclipseTests.class);
 }

    public void test_1() throws Exception {
        System.out.println("tests.length: "+ tests.length);
        String method="m";
        String newMethod = "k";
        String interfaceName = "I";
        String methodSignature[] = new String []{};
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest                                                  
                System.out.println("before renameMethod, tests[i]: "+ tests[i]);
// Added for RenamePrivateMethod                
//                if (tests[i].equals("test15") || tests[i].equals("test16") || tests[i].equals("test17")){
//                   method="m"; newMethod ="k"; methodSignature = new String [] {"int"};
//                } else if (tests[i].equals("test2")){
//                   method = "m";  newMethod = "fred"; methodSignature = new String []{};
//                } else if (tests[i].equals("test20")){
//                   method = "m";  newMethod = "fred"; methodSignature = new String[]{"int"};
//                } else if (tests[i].equals("test24")){
//                   method="m"; newMethod ="k"; methodSignature = new String[]{"java.lang.String"};
//                } else if (tests[i].equals("test25")){
//                   method="m"; newMethod ="k"; methodSignature = new String[]{"java.lang.String []"};
//                } else {
//                    method="m"; newMethod ="k"; methodSignature = new String []{};
//                }  

// Added for RenameStaticMethod 
//                if (tests[i].equals("test7") || tests[i].equals("test8") || tests[i].equals("test9")){
//                    method="m"; newMethod ="k"; methodSignature = new String []{"int"};
//                } else {
//                    method="m"; newMethod ="k"; methodSignature = new String []{};
//                }                   
//                
//                if (!tests[i].equals("test10") && !tests[i].equals("test11") && !tests[i].equals("testStaticImport1") && !tests[i].equals("testStaticImport2") && !tests[i].equals("testFail2"))
//                    renameMethod(refactoringName + "." + subDirectoryName + "." + tests[i] + ".A", method, methodSignature, newMethod, true, false);                          
                
// Added for RenameMethodInInterface 
                if (tests[i].equals("testAnnotation0")){
                    method="name"; newMethod ="ident"; methodSignature = new String []{};
                } else if (tests[i].equals("testAnnotation1")){
                    method="value"; newMethod ="number"; methodSignature = new String []{};
                } else if (tests[i].equals("testAnnotation2")){
                    method="thing"; newMethod ="value"; methodSignature = new String []{};
                } else if (tests[i].equals("testAnnotation3")){
                    method="value"; newMethod ="num"; methodSignature = new String []{};
                } else if (tests[i].equals("testAnnotation4")){
                    method="value"; newMethod ="num"; methodSignature = new String []{};
                } else if (tests[i].equals("testGenerics01")){
                    method="getXYZ"; newMethod ="zYXteg"; methodSignature = new String []{};
                } else {
                    method="m"; newMethod ="k"; methodSignature = new String []{};
                }
                
                if (!tests[i].equals("testFail6") && !tests[i].equals("testFail16") && !tests[i].equals("testFail5"))
                    renameMethod(refactoringName + "." + subDirectoryName + "." + tests[i] + "." + interfaceName, method, methodSignature, newMethod, true, false);                                
                //renameMethod(refactoringName + "." + subDirectoryName + "." + tests[i] + ".A", method, methodSignature, newMethod, true, false);                                
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
                if (!MoveFile_Or_Directory(new File(src), new File(dst))) System.out.println("There was a problem moving test# " + i);
            }        
                        
            File info = new File(workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory, "REFACTORING_WAS_PERFORMED");            
            if (!info.exists()) info.createNewFile();            
            
            String dir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i];
            new File(dir).delete();
          } else {            
            if (new File(src).exists()) new File(src).delete();                
            BufferedWriter out = new BufferedWriter(new FileWriter(workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i]+ File.separator + outputDirectory + File.separator + "REFACTORING_NOT_PERFORMED"));            
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
