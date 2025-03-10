package org.netbeans.test.refactoring.innertoouter;

import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import org.netbeans.junit.NbTestSuite;

public class testOur_1_InnerToOuter_Classrelationships extends InnerToOuterTestCase {
    
    public testOur_1_InnerToOuter_Classrelationships(String name) throws Exception {
        super(name);        
        setArgsFromFile(testOur_1_InnerToOuter_Classrelationships.class.getSimpleName(), "generatorTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_InnerToOuter_Classrelationships.class);
    }
    
    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {
            try {
                System.out.println("running test# "+ i + " correspond to: "+ tests[i]);
                currentNumOfTest = i;// setting the currentNumOfTest                                    
                // Check method and newMethod!!!         
                //System.out.println(refactoringName + "." + subDirectoryName + "." + tests[i] + ".A.B");
                innerToOuter(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A.B", "B", null, false);                
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
