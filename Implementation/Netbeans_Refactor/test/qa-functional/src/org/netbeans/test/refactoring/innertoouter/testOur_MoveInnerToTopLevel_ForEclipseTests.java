package org.netbeans.test.refactoring.innertoouter;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import org.netbeans.junit.NbTestSuite;

public class testOur_MoveInnerToTopLevel_ForEclipseTests extends InnerToOuterTestCase {
    
    public testOur_MoveInnerToTopLevel_ForEclipseTests(String name) throws Exception {
        super(name);        
        setArgsFromFile(testOur_MoveInnerToTopLevel_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_MoveInnerToTopLevel_ForEclipseTests.class);
    }
    
    public void testAll() throws Exception {        
        for (int i = 0; i < tests.length; i++) {
            try {
                String outerClass = "A";
                String innerClass = "Inner";                
                String newFieldName = "a";
                System.out.println("running test# "+ i + " correspond to: "+ tests[i]);
                currentNumOfTest = i;// setting the currentNumOfTest                                    
                
                if (tests[i].equals("test34")) {
                   outerClass = "A"; innerClass = "SomeClass"; newFieldName = null;
                } else if (tests[i].equals("test36")) {
                   outerClass = "A.SomeInner";  innerClass = "Inner"; newFieldName = null;
                } else if (tests[i].equals("test37")) {
                   outerClass = "A";  innerClass = "SomeInner"; newFieldName = null;
                } else if (tests[i].equals("testFail_nonstatic_3")) {
                   outerClass = "A.1Local";  innerClass = "NestedLocal"; newFieldName = "a";
                } else if (tests[i].equals("test_nonstatic_33")) {
                   outerClass = "A";  innerClass = "I"; newFieldName = "a";
                } else if (tests[i].equals("test_nonstatic_43")) {
                   outerClass = "A.Inner";  innerClass = "MoreInner"; newFieldName = "inner";
                } else if (tests[i].equals("test_nonstatic_44")) {
                   outerClass = "A.Inner";  innerClass = "MoreInner"; newFieldName = "p";
                } else if (tests[i].indexOf("_") > 0){
                  outerClass = "A"; innerClass = "Inner"; newFieldName = "a"; 
                } else {
                  outerClass = "A"; innerClass = "Inner"; newFieldName = null;   
                }             
                // These tests are not includes in Eclipse: test27, test28 and test29                
                    innerToOuter(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + outerClass + "." + innerClass, innerClass, newFieldName, false);                         
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
