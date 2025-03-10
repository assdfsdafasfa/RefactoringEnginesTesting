package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;


public class testOur_RenamePrivateMethod_ForEclipseTests extends RenameTestCase {

    public testOur_RenamePrivateMethod_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenamePrivateMethod_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }    

    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenamePrivateMethod_ForEclipseTests.class);
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

                if (tests[i].equals("test15") || tests[i].equals("test16") || tests[i].equals("test17")){
                   method="m"; newMethod ="k"; methodSignature = new String [] {"int"}; className = "A";
                } else if (tests[i].equals("test2")){
                   method = "m";  newMethod = "fred"; methodSignature = new String []{}; className = "A";
                } else if (tests[i].equals("test20")){
                   method = "m";  newMethod = "fred"; methodSignature = new String[]{"int"}; className = "A";
                } else if (tests[i].equals("test24")){
                   method="m"; newMethod ="k"; methodSignature = new String[]{"java.lang.String"}; className = "A";
                } else if (tests[i].equals("test25")){
                   method="m"; newMethod ="k"; methodSignature = new String[]{"java.lang.String[]"}; className = "A";
                } else if (tests[i].equals("test18")){
                   method="m"; newMethod ="kk"; methodSignature = new String[]{"int"}; className = "B";
                } else {
                    method="m"; newMethod ="k"; methodSignature = new String []{""}; className = "A";
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
