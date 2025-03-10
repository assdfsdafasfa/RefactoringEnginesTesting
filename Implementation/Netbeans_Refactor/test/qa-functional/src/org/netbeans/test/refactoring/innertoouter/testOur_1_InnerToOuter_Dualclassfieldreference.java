package org.netbeans.test.refactoring.innertoouter;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import org.netbeans.junit.NbTestSuite;

public class testOur_1_InnerToOuter_Dualclassfieldreference extends InnerToOuterTestCase {
    
    public testOur_1_InnerToOuter_Dualclassfieldreference(String name) throws Exception {
        super(name);        
        setArgsFromFile(testOur_1_InnerToOuter_Dualclassfieldreference.class.getSimpleName(), "generatorTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_InnerToOuter_Dualclassfieldreference.class);
    }
    
    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {
            try {
                System.out.println("running test# "+ i + " correspond to: "+ tests[i]);
                currentNumOfTest = i;// setting the currentNumOfTest                                    
                // Check method and newMethod!!!         
                innerToOuter(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A.B", "B", "a", false);                         
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
