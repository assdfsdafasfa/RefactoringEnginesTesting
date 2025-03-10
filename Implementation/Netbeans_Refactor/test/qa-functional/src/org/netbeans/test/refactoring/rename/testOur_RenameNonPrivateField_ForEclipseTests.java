package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;

public class testOur_RenameNonPrivateField_ForEclipseTests extends RenameTestCase {

    public testOur_RenameNonPrivateField_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenameNonPrivateField_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenameNonPrivateField_ForEclipseTests.class);
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

                if (tests[i].equals("test18")){
                   field = "field"; newField="member"; className = "A";
                } else if (tests[i].equals("test19") || tests[i].equals("test20")){
                   field = "list"; newField="items";  className = "A";
                } else if (tests[i].equals("test21")){
                   field = "fValue"; newField="fOrdinal";  className = "A";
                } else if (tests[i].equals("test22")){
                   field = "tee"; newField="thing";  className = "A";
                } else if (tests[i].equals("testAnnotation1") || tests[i].equals("testAnnotation2")){
                   field = "ZERO"; newField="ZORRO";  className = "A";
                } else if (tests[i].equals("testBug5821")){
                   field = "test"; newField="test1";  className = "A";
                } else if (tests[i].equals("testGenerics4")){
                   field = "count"; newField="number";  className = "A";
                } else if (tests[i].equals("testStaticImport")) {
                   field = "PI"; newField = "e";  className = "A";                    
                } else if (tests[i].equals("testFail5")) {
                   field = "f"; newField = "g"; className = "B.A";                     
                } else {
                    field = "f"; newField="g"; className = "A";
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
