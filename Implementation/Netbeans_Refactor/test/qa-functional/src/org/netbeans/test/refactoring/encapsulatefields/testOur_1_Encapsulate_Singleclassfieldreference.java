package org.netbeans.test.refactoring.encapsulatefields;
import java.lang.reflect.Modifier;
import org.netbeans.junit.NbTestSuite;
import junit.framework.AssertionFailedError;

public class testOur_1_Encapsulate_Singleclassfieldreference extends EncapsulateTestCase {

    public testOur_1_Encapsulate_Singleclassfieldreference(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_1_Encapsulate_Singleclassfieldreference.class.getSimpleName(), "generatorTest");
    }


    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_Encapsulate_Singleclassfieldreference.class);
    }


    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {
            try {
                currentNumOfTest = i;// setting the currentNumOfTest
                encapsulateField(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "theField", null, null, Modifier.PUBLIC, Modifier.PRIVATE, true, false);
                //System.out.println("***************************" + i);
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
