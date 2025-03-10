package org.netbeans.test.refactoring.rename;

import org.netbeans.junit.NbTestSuite;
import junit.framework.AssertionFailedError;

public class testOur_1_RenameClass_AllRelationships extends RenameTestCase {
    
    public testOur_1_RenameClass_AllRelationships(String name) throws Exception {
        super(name);        
        setArgsFromFile(testOur_1_RenameClass_AllRelationships.class.getSimpleName(), "generatorTest");
    }
    
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_RenameClass_AllRelationships.class);
    }
    
    
    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest                  
                renameClass(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "NewA", false, false);
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
