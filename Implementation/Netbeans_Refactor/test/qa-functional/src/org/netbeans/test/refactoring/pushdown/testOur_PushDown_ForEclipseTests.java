package org.netbeans.test.refactoring.pushdown;

import org.netbeans.junit.NbTestSuite;

/**
 *
 * @author Kely  March 16
 */
public class testOur_PushDown_ForEclipseTests extends PushDownTestCase {
        
    public testOur_PushDown_ForEclipseTests(String name) throws Exception {
        super(name);        
        setArgsFromFile(testOur_PushDown_ForEclipseTests.class.getSimpleName(), "eclipseTest");        
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_PushDown_ForEclipseTests.class);
    }
    
    public void testAll() throws Exception {
        System.out.println("total # of tests: "+ tests.length);
        for (int i = 0; i < tests.length; i++) {                
            try {                
                //System.out.println("Class: "+ refactoringName + "." + subDirectoryName + "." + tests[i] + ".A");
                currentNumOfTest = i;// setting the currentNumOfTest
                pushDownField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "f", true, false);
                System.out.println("***************************" + i);
            } catch (Exception e) {
                System.out.println("fail test ***************************" + i + " error " + e);
            }
        }
    }    
    
}
