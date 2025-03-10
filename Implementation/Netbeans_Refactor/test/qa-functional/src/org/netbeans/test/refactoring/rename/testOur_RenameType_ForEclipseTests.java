package org.netbeans.test.refactoring.rename;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;

public class testOur_RenameType_ForEclipseTests extends RenameTestCase {

    public testOur_RenameType_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_RenameType_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_RenameType_ForEclipseTests.class);
    }

    public void test_1() throws Exception {
        System.out.println("tests.length: "+ tests.length);
        String className = "A";
        String newClassName = "B";
        
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest                                                  
                System.out.println("before renameClass, tests[i]: "+ tests[i]);

                if (tests[i].equals("testIllegalTypeName1") || tests[i].equals("testIllegalTypeName2")){
                    className = "A"; newClassName = "X";
                } else if (tests[i].equals("testIllegalTypeName3")) {
                    className = "A"; newClassName = "34";
                } else if (tests[i].equals("testIllegalTypeName4")) {
                    className = "A"; newClassName = "this";
                } else if (tests[i].equals("testIllegalTypeName5")) {
                    className = "A"; newClassName = "fred";
                } else if (tests[i].equals("testIllegalTypeName6")){
                    className = "A"; newClassName = "class";
                } else if (tests[i].equals("testIllegalTypeName7")){
                    className = "A"; newClassName = "A.B";
                } else if (tests[i].equals("testIllegalTypeName8")){
                    className = "A"; newClassName = "A$B";
                } else if (tests[i].equals("testIllegalTypeName9")){
                    className = "A"; newClassName = "aux";
                } else if (tests[i].equals("testNoOp")){
                    className = "A"; newClassName = "A";
                } else if (tests[i].equals("testWrongArg1")){
                    className = "A"; newClassName = "";
                } else if (tests[i].equals("testNoOp")){
                    className = "A"; newClassName = "A";
                } else if (tests[i].equals("testSimilarElements00")){
                    className = "SomeClass"; newClassName = "SomeClass2";
                } else if (tests[i].equals("testSimilarElements01")){
                    className = "SomeClass"; newClassName = "SomeClass";
                } else if (tests[i].equals("testSimilarElements02")){
                    className = "SomeClass"; newClassName = "SomeDifferentClass";
                } else if (tests[i].equals("testSimilarElements03")){
                    className = "SomeClass"; newClassName = "SomeClass2";
                } else if (tests[i].equals("testSimilarElements04")){
                    className = "SomeClass"; newClassName = "SomeClass2";
                } else if (tests[i].equals("testSimilarElements05")){
                    className = "SomeClass"; newClassName = "SomeDifferentClass";
                } else if (tests[i].equals("testSimilarElements06")){
                    className = "SomeClass"; newClassName = "SomeOtherClass";
                } else if (tests[i].equals("testSimilarElements07")){
                    className = "SomeClass"; newClassName = "SomeDiffClass";
                } else if (tests[i].equals("testSimilarElements08")){
                    className = "ISomeIf"; newClassName = "ISomeIf2";
                } else if (tests[i].equals("testSimilarElements09")){
                    className = "SomeClass.SomeInnerClass"; newClassName = "SomeNewInnerClass";
                } else if (tests[i].equals("testSimilarElements10")){
                    className = "SomeClass"; newClassName = "SomeClass2";
                } else if (tests[i].equals("testSimilarElements11")){
                    className = "SomeClass"; newClassName = "SomeNewClass";
                } else if (tests[i].equals("testSimilarElements12")){
                    className = "SomeFieldClass"; newClassName = "SomeOtherFieldClass";
                } else if (tests[i].equals("testSimilarElements13")){
                    className = "SomeClass"; newClassName = "SomeOtherClass";
                } else if (tests[i].equals("testSimilarElements14")){
                    className = "SomeClass2"; newClassName = "SomeOtherClass2";
                } else if (tests[i].equals("testSimilarElements15")){
                    className = "SomeClass3"; newClassName = "SomeOtherClass3";
                } else if (tests[i].equals("testSimilarElements16")){
                    className = "SomeClass4"; newClassName = "SomeOtherClass4";
                } else if (tests[i].equals("testSimilarElements17")){
                    className = "SomeClass6"; newClassName = "SomeOtherClass6";
                } else if (tests[i].equals("testSimilarElements18")){
                    className = "SomeClass"; newClassName = "SomeOtherClass";
                } else if (tests[i].equals("testSimilarElements19")){
                    className = "ThreeHunkClass"; newClassName = "TwoHunk";
                } else if (tests[i].equals("testSimilarElements20")){
                    className = "OverriddenMethodClass"; newClassName = "ThirdClass";
                } else if (tests[i].equals("testSimilarElements21")){
                    className = "SomeClass"; newClassName = "SomeNewClass";
                } else if (tests[i].equals("testSimilarElements22")){
                    className = "SomeClass"; newClassName = "SomeNewClass";
                } else if (tests[i].equals("testSimilarElements23")){
                    className = "SomeClass"; newClassName = "SomeNewClass";
                } else if (tests[i].equals("testSimilarElements24")){
                    className = "SomeClass"; newClassName = "SomeNewClass";
                } else if (tests[i].equals("testSimilarElements25") || tests[i].equals("testSimilarElements26") || tests[i].equals("testSimilarElements27") || tests[i].equals("testSimilarElements28") || tests[i].equals("testSimilarElements29")){
                    className = "ScrewUp"; newClassName = "ScrewDown";
                } else if (tests[i].equals("testSimilarElements30")){
                    className = "SomeClass"; newClassName = "SomeNewClass";
                } else if (tests[i].equals("testSimilarElements31")){
                    className = "SomeClass"; newClassName = "SomeDiffClass";
                } else if (tests[i].equals("testSimilarElements32")){
                    className = "SomeClass"; newClassName = "SomeDifferentClass";
                } else if (tests[i].equals("testSimilarElements33")){
                    className = "Why"; newClassName = "WhyNot";
                } else if (tests[i].equals("testFail11")){
                    className = "B.A"; newClassName = "B";
                } else if (tests[i].equals("testFail16") || tests[i].equals("testFail17")){
                    className = "Outer.A"; newClassName = "B";
                } else if (tests[i].equals("testFail18") || tests[i].equals("testFail19")){
                    className = "Outer.Inner.A"; newClassName = "B";
                } else if (tests[i].equals("testFailRegression1GCRKMQ")){
                    className = "Blinky"; newClassName = "B";
                } else if (tests[i].equals("testIllegalInnerClass")){
                    className = "B.A"; newClassName = "B";
                } else if (tests[i].equals("testQualifiedName2")){
                    className = "Transient"; newClassName = "TransientEquipment";
                } else {
                    className = "A"; newClassName = "B";
                }    
                               
                renameClass(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + className, newClassName, true, false);                                          
                                            
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
