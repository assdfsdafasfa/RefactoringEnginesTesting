package org.netbeans.test.refactoring.pushdown;

import org.netbeans.junit.NbTestSuite;
import junit.framework.AssertionFailedError;

/**
 *
 * @author Kely  March 16
 */
public class testOur_1_PushDown_DualClassFieldReference extends PushDownTestCase {
    
    /** Creates a new instance of testOur_1_PushDown_DualClassFieldReference */
    public testOur_1_PushDown_DualClassFieldReference(String name) throws Exception {
        super(name);        
        System.out.println("setArgsFromFile: "+ testOur_1_PushDown_DualClassFieldReference.class.getSimpleName());
        setArgsFromFile(testOur_1_PushDown_DualClassFieldReference.class.getSimpleName(), "generatorTest");        
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_PushDown_DualClassFieldReference.class);
    }
    
    public void testAll() throws Exception {
        System.out.println("total # of tests: "+ tests.length);
        for (int i = 0; i < tests.length; i++) {                
            try {                
                //System.out.println("Class: "+ refactoringName + "." + subDirectoryName + "." + tests[i] + ".A");
                currentNumOfTest = i;// setting the currentNumOfTest
                pushDownField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "f", true, false);
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
