/*
 * ChangeSignatureTestCase.java
 *
 * Created on August 23, 2004, 3:53 PM
 */

package org.netbeans.test.refactoring.changesignature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.netbeans.jmi.javamodel.CallableFeature;
import org.netbeans.jmi.javamodel.Parameter;
import org.netbeans.jmi.javamodel.Type;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.ChangeParametersRefactoring;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.ChangeParametersRefactoring.ParameterInfo;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;


/**
 *
 * @author  eh103527
 */
public class ChangeSignatureTestCase extends RefactoringTestCase {
    
    /** Creates a new instance of ChangeSignatureTestCase */
    public ChangeSignatureTestCase(String name) {
        super(name);
    }
    
    protected void changeSignature(String className, String name, String[] arguments, String[][] newArguments, int access, boolean undo) {
        setJavaClass(className);
        Utility.prepareTest();
        //System.out.println("after setjavaClass");
        String args="";
        ArrayList list=new ArrayList();
        boolean finish=false;
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Change method signature of " + name);
        try {
            if (arguments != null) {
                for (int i=0;i < arguments.length;i++) {
                    list.add(Utility.findType(arguments[i], typeProxy, jc));
                    args+=arguments[i];
                    if (i < arguments.length-1) {
                        args+=", ";
                    }
                }
            }
            //System.out.println(">>>>>> 1");    
            //get the method
            CallableFeature method;
            if (name == null) //constructor
                method=jc.getConstructor(list, false);
            else
                method=jc.getMethod(name, list, false);
            
            ref("Change signature of "+((name==null)?"constructor ":"method ")+
                    getModifier(method.getModifiers())+" "+
                    ((method.getName() == null)?"":method.getName())+"("+args+") of class "+className);
            ref(((name==null)?"constructor ":"method ")+getModifier(access)+" "+
                    ((method.getName() == null)?"":method.getName()));
            ref("Arguments: ");
            
            //System.out.println(">>>>>> 2");    
            for (int i=0;i < newArguments.length;i++) {
                if (newArguments[i][1] == null) {
                    ref(newArguments[i][0]);
                } else {
                    ref(newArguments[i][1]+" "+newArguments[i][0]+" = "+newArguments[i][2]);
                }
            }
            
            //System.out.println(">>>>>> 3");    
            //prepare arguments array
            HashMap indicies=new HashMap();
            List origArguments = method.getParameters();
            for (int i=0;i < origArguments.size();i++) {
                Parameter par=(Parameter)(origArguments.get(i));
                for (int j=0;j < newArguments.length;j++) {
                    if (newArguments[j][0].equals(par.getName())) {
                        indicies.put(par.getName(), new Integer(i));
                    }
                }
            }
            
            //System.out.println(">>>>>> 4");    
            ParameterInfo[] argTable = new ParameterInfo[newArguments.length];
            for (int i=0;i < argTable.length;i++) {
                Type type=null;
                if (newArguments[i][1] != null) {
                    type=typeProxy.resolve(newArguments[i][1]);
                    argTable[i]=new ParameterInfo(-1, newArguments[i][0], type, newArguments[i][2]);
                } else {
                    argTable[i]=new ParameterInfo(((Integer)(indicies.get(newArguments[i][0]))).intValue());
                }
            }
            
            //make refactoring sequence
            System.out.println(">>>>>> 5");
            Problem p = null;
            ChangeParametersRefactoring refactoring = new ChangeParametersRefactoring(method);
            p = refactoring.preCheck();
            //if (refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                
            }
            refactoring.setParameterInfo(argTable);
            refactoring.setModifiers(access);
            //System.out.println("before checkParameters");
            p = refactoring.checkParameters();
            //if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                                
            }
            //System.out.println("after checkParameters");
            p = refactoring.prepare(result);
            //if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
            if (refProblems(p)){
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                                               
            }
            files=getResources(result);
            //if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            p = result.doRefactoring(true);
            //if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");                                                               
            }
            finish=true;
            System.out.println("finish: "+ finish);
            if (finish) {
                numTotalRefactoredTests++; // Kely add this line to increment counter
                statusOfTests[currentNumOfTest] = true;
            }            
        } catch (Throwable t) {
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                //assertTrue(t.getMessage(), false);  // Kely commented this line
            }
        } finally {
            Utility.finishTest();
        }
        if (finish) {
            try {
                if (undo) {
                    JavaMetamodel.getUndoManager().undo();
                }
                compareResources(files, result, className, className);
            } catch (Exception ex) {
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }
}
