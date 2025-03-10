package org.netbeans.test.refactoring.rename;

import org.netbeans.junit.NbTestSuite;
import junit.framework.AssertionFailedError;

public class testOur_1_RenameField_Singleclassfieldreference extends RenameTestCase {
    
    public testOur_1_RenameField_Singleclassfieldreference(String name) throws Exception {
        super(name);        
        System.out.println("beginning>>>>>>>>");
        setArgsFromFile(testOur_1_RenameField_Singleclassfieldreference.class.getSimpleName(), "generatorTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_RenameField_Singleclassfieldreference.class);
    }
    
    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest                    
                //renameField(tests[i] + ".A", "theField", "newField", true, false);
                renameField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "theField", "theNewField", true, false);
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
