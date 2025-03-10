package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;

public class testOur_RenameTypeParameter_ForEclipseTests extends RenameTestCase {

    public testOur_RenameTypeParameter_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenameTypeParameter_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenameTypeParameter_ForEclipseTests.class);
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

                if (tests[i].equals("test7") || tests[i].equals("test8") || tests[i].equals("test9")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"int"}; className = "A";
                } else if (tests[i].equals("test10")) {
                    method="method"; newMethod ="newMethod"; methodSignature = new String []{}; className = "B";
                } else if (tests[i].equals("test11")) {
                    method="method2"; newMethod ="fred"; methodSignature = new String []{}; className = "A";
                } else if (tests[i].equals("testStaticImport1") || tests[i].equals("testStaticImport2")) {
                    method="m"; newMethod ="k"; methodSignature = new String []{}; className = "C";
                } else {
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
}
