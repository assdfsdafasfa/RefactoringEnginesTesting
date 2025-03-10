/*
 * PushDownTestCase.java
 *
 * Created on March 16, 2007, 12:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.netbeans.test.refactoring.pushdown;

import java.util.Collections;
import java.util.HashMap;
import javax.jmi.reflect.RefObject;
import org.netbeans.jmi.javamodel.Field;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.PushDownRefactoring;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;

/**
 *
 * @author Kely
 */
public class PushDownTestCase extends RefactoringTestCase {
    
    /** Creates a new instance of RenameTestCase */
    public PushDownTestCase(String name) {
        super(name);
    }
    
    protected void pushDownField(String className, String fieldName, boolean renameInComments, boolean undo) {        
        setJavaClass(className);
        ref("PushDown field "+ fieldName);        
        pushDown(jc, className, fieldName, renameInComments, undo);
        
    }    
    
    protected void pushDown(JavaClass object, String name, String fieldName, boolean renameInComments, boolean undo) {
        Utility.prepareTest();
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("PushDown");
        boolean finish=false;
        try {            
            Problem p = null;
            PushDownRefactoring refactoring = new PushDownRefactoring(object);              
            p = refactoring.preCheck();
            //if (refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                
            }
            //Method m = baseClass.getMethod("newMethod", Collections.EMPTY_LIST, false);
            Field f = object.getField(fieldName, false);
            PushDownRefactoring.MemberInfo[] members = new PushDownRefactoring.MemberInfo[] {new PushDownRefactoring.MemberInfo(f)};
            
            refactoring.setMembers(members);
            p = refactoring.checkParameters();
            //if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                               
            }
            p = refactoring.prepare(result);
            //if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                                               
            }
            files=getResources(result);
            p = result.doRefactoring(true);
            //if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                                                               
            }
            finish=true;
            if (finish) {
                numTotalRefactoredTests++; // Kely add this line to increment counter
                statusOfTests[currentNumOfTest] = true;
            }
            System.out.println("finish: "+ finish);// Kely add this line
        } catch (Throwable t) {
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                assertTrue(t.getMessage(), false);
            }
        } finally {            
            Utility.finishTest();
        }
        if (finish) {
            try {
                if (undo) {
                    JavaMetamodel.getUndoManager().undo();
                }
                compareResources(files, result, name, name);
            } catch (Exception ex) {
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }    
}
