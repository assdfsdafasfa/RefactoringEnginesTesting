package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;


public class testOur_RenameMethodInInterface_ForEclipseTests extends RenameTestCase {

    public testOur_RenameMethodInInterface_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenameMethodInInterface_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }    

    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenameMethodInInterface_ForEclipseTests.class);
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
                if (tests[i].equals("testAnnotation0")){
                    method="name"; newMethod ="ident"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testAnnotation1")){
                    method="value"; newMethod ="number"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testAnnotation2")){
                    method="thing"; newMethod ="value"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testAnnotation3")){
                    method="value"; newMethod ="num"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testAnnotation4")){
                    method="value"; newMethod ="num"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testGenerics01")){
                    method="getXYZ"; newMethod ="zYXteg"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail8")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"int"}; interfaceName = "I";
                } else if (tests[i].equals("testFail21")){
                    method="m"; newMethod ="k"; methodSignature = new String []{"java.lang.String"}; interfaceName = "I";
                } else if (tests[i].equals("testFail22")){ 
                    method="m"; newMethod ="k"; methodSignature = new String []{"java.lang.Object"}; interfaceName = "I";
                } else if (tests[i].equals("testFail23")){
                    method="toString"; newMethod ="k"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail30") || tests[i].equals("testFail31")){
                    method="toString"; newMethod ="k"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail32") || tests[i].equals("testFail33")){
                    method="m"; newMethod ="toString"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail34")){
                    method="m"; newMethod ="equals"; methodSignature = new String []{"java.lang.Object"}; interfaceName = "I";
                } else if (tests[i].equals("testFail35")){
                    method="m"; newMethod ="equals"; methodSignature = new String []{"java.lang.Object"}; interfaceName = "I";
                } else if (tests[i].equals("testFail36")){
                    method="m"; newMethod ="getClass"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail37")){
                    method="m"; newMethod ="hashCode"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail38")){
                    method="m"; newMethod ="notify"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail39")){
                    method="m"; newMethod ="notifyAll"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail40")){
                    method="m"; newMethod ="wait"; methodSignature = new String []{"long", "int"}; interfaceName = "I";
                } else if (tests[i].equals("testFail41")){
                    method="m"; newMethod ="wait"; methodSignature = new String []{"long"}; interfaceName = "I";
                } else if (tests[i].equals("testFail42")){
                    method="m"; newMethod ="wait"; methodSignature = new String []{}; interfaceName = "I";
                } else if (tests[i].equals("testFail43")){
                    method="m"; newMethod ="wait"; methodSignature = new String []{}; interfaceName = "I";
                } else {
                    method="m"; newMethod ="k"; methodSignature = new String []{}; interfaceName = "I";
                }
                
                //if (!tests[i].equals("testFail6") && !tests[i].equals("testFail16") && !tests[i].equals("testFail5"))
                    renameMethod(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + interfaceName, method, methodSignature, newMethod, true, false);                                                
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
