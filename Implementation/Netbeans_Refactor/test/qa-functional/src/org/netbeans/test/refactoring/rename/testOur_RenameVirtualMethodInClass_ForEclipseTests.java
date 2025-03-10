package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;

public class testOur_RenameVirtualMethodInClass_ForEclipseTests extends RenameTestCase {

    public testOur_RenameVirtualMethodInClass_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenameVirtualMethodInClass_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    

    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenameVirtualMethodInClass_ForEclipseTests.class);
 }

    public void test_1() throws Exception {
        System.out.println("tests.length: "+ tests.length);
        String method="m";
        String newMethod = "k";
        String className = "A";        
        String methodSignature[] = new String []{};
        
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest
                System.out.println("before renameMethod, tests[i]: "+ tests[i]);
                if (tests[i].equals("test15")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"int"}; className = "A";
                } else if (tests[i].equals("test16")){
                    method="m"; newMethod ="fred"; methodSignature = new String []{"int"}; className = "A";
                } else if (tests[i].equals("test17")){
                    method="m"; newMethod ="kk"; methodSignature = new String []{"int"}; className = "A";
                } else if (tests[i].equals("test18")){
                    method="m"; newMethod ="kk"; methodSignature = new String []{"int"}; className = "B";
                } else if (tests[i].equals("test19") || tests[i].equals("test2")){
                    method="m"; newMethod ="fred"; methodSignature = new String []{}; className = "A";
                } else if (tests[i].equals("test20") || tests[i].equals("test21")){
                    method="m"; newMethod ="fred"; methodSignature = new String []{"int"}; className = "A";
                } else if (tests[i].equals("test24")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"java.lang.String"}; className = "A";
                } else if (tests[i].equals("test34")){
                    method="A"; newMethod ="foo"; methodSignature = new String []{}; className = "A";
                } else if (tests[i].equals("test35")){
                    method="foo"; newMethod ="bar"; methodSignature = new String []{"java.lang.Object"}; className = "A";
                } else if (tests[i].equals("test36")){
                    method="foo"; newMethod ="bar"; methodSignature = new String []{"java.lang.String"}; className = "A";
                } else if (tests[i].equals("test37")){
                    method="foo"; newMethod ="bar"; methodSignature = new String []{"renamemethod.RenameVirtualMethodInClass.test37.in.A"}; className = "A";
                } else if (tests[i].equals("test40")){
                    method="method"; newMethod ="method2"; methodSignature = new String []{"A.1LocalClass"}; className = "A";
                } else if (tests[i].equals("testGenerics1")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"G"}; className = "A";
                } else if (tests[i].equals("testGenerics2")){
                    method="add"; newMethod ="addIfPositive"; methodSignature = new String []{"E"}; className = "A";
                } else if (tests[i].equals("testGenerics3")){
                    method="add"; newMethod ="addIfPositive"; methodSignature = new String []{"T"}; className = "A";
                } else if (tests[i].equals("testGenerics4")){
                    method="takeANumber"; newMethod ="doit"; methodSignature = new String []{"java.lang.Number"}; className = "A";
                } else if (tests[i].equals("testGenerics5")){
                    method="covariant"; newMethod ="variant"; methodSignature = new String []{}; className = "A";
               }  else if (tests[i].equals("testEnum1")){
                    method="getNameLength"; newMethod ="getNameSize"; methodSignature = new String []{}; className = "A";
               }  else if (tests[i].equals("testEnum2")){
                    method="getSquare"; newMethod ="get2ndPower"; methodSignature = new String []{}; className = "A";
               }  else if (tests[i].equals("testEnum3")){
                    method="getSquare"; newMethod ="get2ndPower"; methodSignature = new String []{}; className = "A";
               }  else if (tests[i].equals("testEnumFail1")){
                    method="value"; newMethod ="valueOf"; methodSignature = new String []{"java.lang.String"}; className = "A";
               }  else if (tests[i].equals("testFail1")){
                    method="toString"; newMethod ="k"; methodSignature = new String []{}; className = "A";
               }  else if (tests[i].equals("testFail9")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"int"}; className = "A";
               }  else if (tests[i].equals("testFail14")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"int"}; className = "A";
               }  else if (tests[i].equals("testFail31")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"java.lang.String"}; className = "A";
               }  else if (tests[i].equals("testFail32")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"java.lang.Object"}; className = "A";
               }  else if (tests[i].equals("testFail33")){
                    method="toString"; newMethod ="k"; methodSignature = new String []{}; className = "A";
               }  else if (tests[i].equals("testFail34")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"java.lang.String"}; className = "A";
               }  else if (tests[i].equals("testFail40")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"int"}; className = "A";
               }  else if (tests[i].equals("testVarargs1")){
                    method="runall"; newMethod ="runThese"; methodSignature = new String []{"java.lang.Runnable[]"}; className = "A";
               }  else if (tests[i].equals("testVarargs2")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"java.lang.String[]"}; className = "A";
               }  else {
                    method="m"; newMethod ="k"; methodSignature = new String []{}; className = "A";
                }
                
                renameMethod(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + className, method, methodSignature, newMethod, true, false);                                
                                               
                System.out.println("***************************" + i);                
            } catch (AssertionFailedError e) {
                errorMessages[currentNumOfTest]=e.toString();
                System.out.println("fail test ***************************" + i + " error " + e);
            } catch (Exception e) {
                errorMessages[currentNumOfTest]=e.toString();
                System.out.println("fail test ***************************" + i + " error " + e);
            }
        }
    }
     

 
//   protected void tearDown() {             
//      //super.tearDown();
//      System.out.println("tearDown>>>>>");        
//      try {
//      String fileName = "A.java";      
//      for (int i = 0; i < tests.length; i++) {
//            String src = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + fileName;
//            String dst = workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory;// +  File.separator + fileName;
//            File dstFile = new File(dst);
//            if (!dstFile.exists()) dstFile.mkdir();
//            else {
//              File dstPrevious = new File(dstFile.getAbsolutePath(), fileName);
//              if (dstPrevious.exists()) dstPrevious.delete();
//            }
//          
//          if (statusOfTests[i]){// Kely add this condition            
//            if (new File(src).exists()) {                                
//                if (!MoveFile_Or_Directory(new File(src), new File(dst))) System.out.println("There was a problem moving test# " + i);
//            }        
//                        
//            File info = new File(workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i] + File.separator + outputDirectory, "REFACTORING_WAS_PERFORMED");            
//            if (!info.exists()) info.createNewFile();            
//            
//            String dir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i];
//            new File(dir).delete();
//          } else {            
//            if (new File(src).exists()) new File(src).delete();                
//            BufferedWriter out = new BufferedWriter(new FileWriter(workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + tests[i]+ File.separator + outputDirectory + File.separator + "REFACTORING_NOT_PERFORMED"));            
//            out.write(errorMessages[i]);
//            out.close();
//            
//            String dir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + tests[i];
//            if (new File(dir).exists()) new File(dir).delete();
//          }
//      }
//              
//        printSummaryCounters();
//      } catch (IOException e) {
//          System.out.println("Error in printSummaryCounters: "+ e);
//      }              
//        
//    } 


        
}
