package org.netbeans.test.refactoring.encapsulatefields;
import java.lang.reflect.Modifier;
import org.netbeans.junit.NbTestSuite;
import junit.framework.AssertionFailedError;

public class testOur_1_Encapsulate_Classwitharrayfield extends EncapsulateTestCase {
        
    public testOur_1_Encapsulate_Classwitharrayfield(String name) throws Exception {
        super(name);        
        System.out.println("setArgsFromFile: "+ testOur_1_Encapsulate_Classwitharrayfield.class.getSimpleName());
        setArgsFromFile(testOur_1_Encapsulate_Classwitharrayfield.class.getSimpleName(), "generatorTest");
    }
        
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_Encapsulate_Classwitharrayfield.class);
    }
     
    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {                
            try {
                currentNumOfTest = i;// setting the currentNumOfTest                
                encapsulateField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "theField", "setTheField", "getTheField", Modifier.PUBLIC, Modifier.PRIVATE, true, false);
                //encapsulateField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".A", "theField", null, null, Modifier.PUBLIC, Modifier.PRIVATE, true, false);
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
