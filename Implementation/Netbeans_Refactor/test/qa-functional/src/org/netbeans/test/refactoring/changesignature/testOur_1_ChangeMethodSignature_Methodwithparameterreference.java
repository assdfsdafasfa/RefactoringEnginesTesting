package org.netbeans.test.refactoring.changesignature;

import java.lang.reflect.Modifier;
import org.netbeans.junit.NbTestSuite;
import junit.framework.AssertionFailedError;

public class testOur_1_ChangeMethodSignature_Methodwithparameterreference extends ChangeSignatureTestCase {

    public testOur_1_ChangeMethodSignature_Methodwithparameterreference(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_1_ChangeMethodSignature_Methodwithparameterreference.class.getSimpleName(), "generatorTest");
    }

    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_1_ChangeMethodSignature_Methodwithparameterreference.class);
    }

    public void testAll() throws Exception {
        for (int i = 0; i < tests.length; i++) {
            try {
                System.out.println("running test# "+ i + " correspond to: "+ tests[i]);
                currentNumOfTest = i;// setting the currentNumOfTest
                // Check method and newMethod!!!
                // "java.lang.Integer" , "java.lang.Boolean"
                changeSignature(refactoringName + "." + subDirectoryName + "." + tests[i] + ".in." + "A", "m", new String[] {"java.lang.Object"},
                new String[][] {}, Modifier.PUBLIC, false);
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
