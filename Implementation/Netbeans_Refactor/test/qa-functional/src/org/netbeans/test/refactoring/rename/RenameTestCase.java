/*
 * RenameTestCase.java
 *
 * Created on August 23, 2004, 3:45 PM
 */

package org.netbeans.test.refactoring.rename;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.netbeans.jmi.javamodel.Field;
import org.netbeans.jmi.javamodel.JavaModelPackage;
import org.netbeans.jmi.javamodel.JavaPackage;
import org.netbeans.jmi.javamodel.LocalVarDeclaration;
import org.netbeans.jmi.javamodel.LocalVariable;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.jmi.javamodel.Parameter;
import javax.jmi.reflect.RefObject;
import org.netbeans.jmi.javamodel.Feature;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.MoveClassRefactoring;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;
import org.openide.filesystems.FileObject;


/**
 *
 * @author  eh103527
 */
public class RenameTestCase extends RefactoringTestCase {
    public boolean finish;// Kely add this field and commented the local variable finish
    
    /** Creates a new instance of RenameTestCase */
    public RenameTestCase(String name) {
        super(name);
    }
    
    protected void renameClass(String name, String newName, boolean renameInComments, boolean undo) {
        setJavaClass(name);
        ref("Rename class "+name+" to "+newName);
        rename(jc, name, newName, renameInComments, undo);
    }
    
    protected void renameAnnotationMember(String annotationName, String memberName, String newMemberName, boolean renameInComments, boolean undo) {
        setJavaClass(annotationName);
        ref("Rename annotation member "+memberName+" to "+newMemberName+" of AnnotationType "+annotationName);
        Feature member=null;
        JavaMetamodel.getDefaultRepository().beginTrans(true);
        try {
            for (Iterator it=jc.getFeatures().iterator();it.hasNext();) {
                Feature f=(Feature)(it.next());
                if (f.getName().equals(memberName)) {
                    member=f;
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace(getLogStream());
            assertTrue(t.getMessage(), false);
        } finally {
            JavaMetamodel.getDefaultRepository().endTrans();
        }
        assertNotNull(memberName+" of "+annotationName+" is null.", member);
        rename(member, memberName, newMemberName, renameInComments, undo);
    }
    
    protected void renameField(String className, String fieldName, String newFieldName, boolean renameInComments, boolean undo) {        
        setJavaClass(className);                
        System.out.println("renameField jc: "+ jc);
        ref("Rename field "+fieldName+" to "+newFieldName+" of class "+className);
        Field field=null;
        JavaMetamodel.getDefaultRepository().beginTrans(true);
        try {                        
            field = jc.getField(fieldName, false);            
            assertNotNull("RenameField " + fieldName + " is null", field);                                      
        } catch (Throwable t) {                     
            errorMessages[currentNumOfTest]= "field " + fieldName + " does not exist"; // Kely add this line
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                assertTrue(t.getMessage(), false);
            }            
            //t.printStackTrace(getLogStream());            
            //assertTrue(t.getMessage(), false);  // Kely comment this line
        } finally {            
            JavaMetamodel.getDefaultRepository().endTrans();
        }        
        rename(field, fieldName, newFieldName, renameInComments, undo);        
    }
    
    protected void renameMethodParameter(String className, String methodName, String[] parameters, String fieldName, String newFieldName, boolean renameInComments, boolean undo) {
        setJavaClass(className);
        JavaMetamodel.getDefaultRepository().beginTrans(true);
        Parameter param=null;
        try {
            String args="";
            ArrayList list=new ArrayList();
            Method method = null;
            if (parameters != null) {
                for (int i=0;i < parameters.length;i++) {
                    list.add(Utility.findType(parameters[i], typeProxy, jc));
                    args+=parameters[i];
                    if (i < parameters.length-1) {
                        args+=", ";
                    }
                }
            }
            ref("Rename method's "+methodName+"("+args+") parameter "+fieldName+" to "+newFieldName+" of class "+className);
            method = jc.getMethod(methodName, list, false);
            List params=method.getParameters();
            
            for (Iterator it=params.iterator();it.hasNext();) {
                Parameter par=(Parameter)(it.next());
                if (par.getName().equals(fieldName)) {
                    param=par;
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace(getLogStream());
            assertTrue(t.getMessage(), false);
        } finally {
            JavaMetamodel.getDefaultRepository().endTrans();
        }
        
        rename(param, fieldName, newFieldName, renameInComments, undo);
    }
    
    protected void renameLocalVariable(String className, String methodName, String[] parameters, int declarationStatementIndex, String fieldName, String newFieldName, boolean renameInComments, boolean undo) {
        setJavaClass(className);
        JavaMetamodel.getDefaultRepository().beginTrans(true);
        LocalVariable variable=null;
        try {
            String args="";
            ArrayList list=new ArrayList();
            Method method = null;
            if (parameters != null) {
                for (int i=0;i < parameters.length;i++) {
                    list.add(Utility.findType(parameters[i], typeProxy, jc));
                    args+=parameters[i];
                    if (i < parameters.length-1) {
                        args+=", ";
                    }
                }
            }
            ref("Rename method's "+methodName+"("+args+") local variable "+fieldName+" to "+newFieldName+" of class "+className);
            method = jc.getMethod(methodName, list, false);
            List stmtList=method.getBody().getStatements();
            LocalVarDeclaration locDecl=(LocalVarDeclaration)stmtList.get(declarationStatementIndex);
            List vars=locDecl.getVariables();
            for (Iterator it=vars.iterator();it.hasNext();) {
                LocalVariable par=(LocalVariable)(it.next());
                if (par.getName().equals(fieldName)) {
                    variable=par;
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace(getLogStream());
            assertTrue(t.getMessage(), false);
        } finally {
            JavaMetamodel.getDefaultRepository().endTrans();
        }
        
        rename(variable, fieldName, newFieldName, renameInComments, undo);
    }
    
    protected void renameMethod(String className, String methodName, String[] attributes, String newMethodName, boolean renameInComments, boolean undo) {        
        setJavaClass(className); 
        System.out.println("renamemethod class: "+ jc);
        String args="";
        ArrayList list=new ArrayList();
        Method method = null;
        JavaMetamodel.getDefaultRepository().beginTrans(true);
        try {
            if (attributes != null) {
                for (int i=0;i < attributes.length;i++) {
                    list.add(Utility.findType(attributes[i], typeProxy, jc));
                    args+=attributes[i];
                    if (i < attributes.length-1) {
                        args+=", ";
                    }
                }
            }
            System.out.println("RenameMethod after reading attributes");
            ref("Rename method "+methodName+"("+args+") to "+newMethodName+" of class "+className);
            method = jc.getMethod(methodName, list, false);
            System.out.println("method is: "+ method);
            assertNotNull("RenameMethod method is null.", method); 
        } catch (Throwable t) {
            errorMessages[currentNumOfTest]= "method " + method + " does not exist"; // Kely add this line
            t.printStackTrace(getLogStream());
            assertTrue(t.getMessage(), false);
        } finally {
            JavaMetamodel.getDefaultRepository().endTrans();
        }        
        rename(method, methodName, newMethodName, renameInComments, undo);
    }
    
    //through UI
    protected void renamePackage(String packageName, String newPackageName, String initClass, boolean undo) {
        setJavaClass(initClass);
        Utility.prepareTest();
        ref("Rename package "+packageName+" to "+newPackageName);
        RefactoringSession result = RefactoringSession.create("Rename package");
        //boolean finish=false;
        finish=false;
        HashMap files=null;
        try {
            JavaPackage pack=((JavaModelPackage)(jc.refOutermostPackage())).getJavaPackage().resolvePackage(packageName);
            FileObject sourceFolder = JavaMetamodel.getManager().getClassPath().findResource(pack.getName().replace('.','/'));
            if (sourceFolder == null) {
                ref("Renamed package folder is null.");
                throw new Exception("Fatal problem");
            }
            MoveClassRefactoring refactoring = new MoveClassRefactoring(sourceFolder, true);
            
            if (refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            refactoring.setTargetPackageName(newPackageName);
            if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
            files=getResources(result);
            if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            finish=true;
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
                Thread.sleep(2000);
                if (undo) {
                    JavaMetamodel.getUndoManager().undo();
                    Thread.sleep(2000);
                }
                compareResources(files, result, packageName, newPackageName);
            } catch (Exception ex) {
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }
    
    protected void rename(RefObject object, String name, String newName, boolean renameInComments, boolean undo) {
        //System.out.println("rename >>> 1");
        Utility.prepareTest();
        //System.out.println("rename >>> 2");
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Rename");
        //boolean finish=false;
        finish=false;
        Problem p= null;
        try {
            //System.out.println("rename >>> 3");
            RenameRefactoring refactoring = new RenameRefactoring(object);
            p = refactoring.preCheck();
            //System.out.println("rename >>> 4");
            //if (refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception(p.getMessage());                
            }
            refactoring.setNewName(newName);
            refactoring.setSearchInComments(renameInComments);
            //System.out.println("rename >>> 5");
            
            p = refactoring.checkParameters();
            
            //if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems(p)) {                                
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception(p.getMessage());
            }
            //System.out.println("rename >>> 6");
            p = refactoring.prepare(result);
            //if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
            //System.out.println("rename >>> 7");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();      
                throw new Exception(p.getMessage());
            }
            files=getResources(result);
            //System.out.println("rename >>> 8");
            p = result.doRefactoring(true);
            //if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            //System.out.println("rename >>> 9");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();      
                throw new Exception(p.getMessage());                
            }
            finish=true;
            //System.out.println("rename >>> 10");
            if (finish) {
                numTotalRefactoredTests++; // Kely add this line to increment counter
                statusOfTests[currentNumOfTest] = true;
            }
            System.out.println("finish: "+ finish);// Kely add this line
        } catch (Exception e) {
            System.out.println("Exception e.getMessage(): "+ e.getMessage());
            errorMessages[currentNumOfTest]=e.getMessage();
            if (e.getMessage() == null || !e.getMessage().equals("Fatal problem")) {
                e.printStackTrace(getLogStream());
                assertTrue(e.getMessage(), false);
            }                                
        } catch (Throwable t) {
            System.out.println("Throwable t.getMessage(): " + t.getMessage());
            errorMessages[currentNumOfTest]=t.getMessage();
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                assertTrue(t.getMessage(), false);
            }
        } finally {            
            Utility.finishTest();
        }
        if (finish) {
            String nm=newName;//generate new name
            if (name.indexOf('.') > -1) { //class with package
                nm=name.substring(0, name.lastIndexOf('.'))+"."+newName;
            }
            try {
                if (undo) {
                    JavaMetamodel.getUndoManager().undo();
                }
                compareResources(files, result, name, nm);
            } catch (Exception ex) {
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }
}
