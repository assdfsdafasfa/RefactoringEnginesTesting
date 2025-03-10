package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;

public class testOur_RenamePrivateField_ForEclipseTests extends RenameTestCase {

    public testOur_RenamePrivateField_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenamePrivateField_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }   

    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenamePrivateField_ForEclipseTests.class);
    }

    public void test_1() throws Exception {
        System.out.println("tests.length: "+ tests.length);
        String field="f";
        String newField = "g";
        String className = "A";
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest                
                System.out.println("before renameField, tests[i]: "+ tests[i]);

                if (tests[i].equals("testFail5")){
                     field = "f"; newField ="g"; className = "B.A";
                } else if (tests[i].equals("testFail8")){
                     field = "gg"; newField ="f"; className = "A";
                } else if (tests[i].equals("testFail9")){
                    field = "y"; newField ="e"; className = "getE";
                } else if (tests[i].equals("testFail10")){
                    field = "y"; newField ="e"; className = "setE";
                } else if (tests[i].equals("test3")){
                    field = "f"; newField ="gg"; className = "A";
                } else if (tests[i].equals("test4") || tests[i].equals("test5") || tests[i].equals("test6")){
                    field = "fMe"; newField ="fYou"; className = "A";
                } else if (tests[i].equals("test7") || tests[i].equals("test9")){
                    field = "fBig"; newField ="fSmall"; className = "A";
                } else if (tests[i].equals("test10") || tests[i].equals("test11")){
                    field = "fList"; newField ="fElements"; className = "Test.A";
                } else {
                    field = "f";  newField="g"; className = "A";
                }
                renameField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + className , field, newField, true, false);
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
