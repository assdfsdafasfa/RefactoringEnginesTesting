package org.netbeans.test.refactoring.rename;

import org.netbeans.junit.NbTestSuite;
import junit.framework.AssertionFailedError;

public class testOur_1_RenameMethod_Singleclassmethodreference extends RenameTestCase {
    
    public testOur_1_RenameMethod_Singleclassmethodreference(String name) throws Exception {
        super(name);        
        setArgsFromFile(testOur_1_RenameMethod_Singleclassmethodreference.class.getSimpleName(), "generatorTest");
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_RenameMethod_Singleclassmethodreference.class);
    }
    
    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {        
        //for (int i = 414; i < tests.length; i++) {        
            try {
                currentNumOfTest = i;// setting the currentNumOfTest 
                System.out.println("before renamemethod, class: "+ tests[i]);
                // Check method and newMethod!!!
                renameMethod(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "m", new String[] {}, "n", true, false);                
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
