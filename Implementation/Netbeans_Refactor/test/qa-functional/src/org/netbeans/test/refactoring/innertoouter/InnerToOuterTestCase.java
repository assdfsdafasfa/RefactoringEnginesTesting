/*
 * InnerToOuterTestCase.java
 *
 * Created on March 19, 2007, 1:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.netbeans.test.refactoring.innertoouter;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.modules.refactoring.api.InnerToOuterRefactoring;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;

public class InnerToOuterTestCase extends RefactoringTestCase {
    
    public InnerToOuterTestCase(String name) {
        super(name);
    }
    
protected void innerToOuter(String className, String innerClass, String referenceName, boolean undo) {
    Utility.prepareTest();
    try {
        boolean finish=false;
        // initialize refactoring and set parameters
        JavaClass clazz = (JavaClass) Utility.findClass(className);
        //System.out.println("innertotop 1, Class is: "+ clazz);
        InnerToOuterRefactoring refactoring = new InnerToOuterRefactoring(clazz);
        refactoring.setClassName(innerClass);
        refactoring.setReferenceName(referenceName);
        
        Problem p = null;
        //System.out.println("innertotop 2");
        //System.out.println("innerouter before CheckParams");
        // we don't expect any problems from checkParameters
        p = refactoring.checkParameters();
        //System.out.println("innertotop 3");
        //if (refProblems((refactoring.checkParameters()))) throw new Exception("Fatal Problem");
        if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");            
        }
        
        //System.out.println("innerouter after CheckParams");
        RefactoringSession result = RefactoringSession.create("Inner to outer");
        // we don't expect any problems from prepare
        p = refactoring.prepare(result);
        //if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
        if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                        
        }
        //System.out.println("innerouter after prepare");
        // we don't expect any problems during refactoring
        p = result.doRefactoring(true);
        //if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
        if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                                    
        }
        finish = true;
        if (finish) {
                numTotalRefactoredTests++; // Kely add this line to increment counter
                statusOfTests[currentNumOfTest] = true;                        
        }
        //assertNull(p);
    } catch (Throwable t) {
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                assertTrue(t.getMessage(), false);
            }
     } finally {
            Utility.finishTest();
     }
}

}
