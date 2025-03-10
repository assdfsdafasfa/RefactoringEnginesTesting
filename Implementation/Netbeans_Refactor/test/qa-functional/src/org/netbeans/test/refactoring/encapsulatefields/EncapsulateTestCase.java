/*
 * EncapsulateTestCase.java
 *
 * Created on August 23, 2004, 3:51 PM
 */

package org.netbeans.test.refactoring.encapsulatefields;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.netbeans.jmi.javamodel.Field;
import org.netbeans.jmi.javamodel.PrimitiveType;
import org.netbeans.jmi.javamodel.PrimitiveTypeKindEnum;
import org.netbeans.jmi.javamodel.Type;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.EncapsulateFieldRefactoring;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;


/**
 *
 * @author  eh103527
 */
public class EncapsulateTestCase extends RefactoringTestCase {

    /** Creates a new instance of EncapsulateTestCase */
    public EncapsulateTestCase(String name) {
        super(name);
    }

    protected void encapsulateField(String className, String name, String setter, String getter, int methodModifiers, int fieldModifier, boolean alwaysUseAccessors, boolean undo) {
        setJavaClass(className);

        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Encapsulate field " + name);
        Utility.prepareTest();
        boolean finish=false;
        try {
            Field field = jc.getField(name, false);
            assertNotNull("Encapsulated field is null.", field);                        
            ref("Encapsulate field "+getModifier(field.getModifiers())+" "+name+" of class "+className);
            String s,g;
            s=(setter != null) ? setter : computeSetterName(field);
            g=(getter != null) ? getter : computeGetterName(field);
            ref("field changed to "+getModifier(fieldModifier)+", setter: "+getModifier(methodModifiers)+" "+s+"(...), getter: "+getModifier(methodModifiers)+" "+g+"()"+
                    ", use accessors "+((alwaysUseAccessors)?"always":"if necessary"));
            EncapsulateFieldRefactoring refactoring = new EncapsulateFieldRefactoring(field);
            Problem p = null;
            p = refactoring.preCheck();
            //if (refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            if (refProblems(p)) {
                errorMessages[currentNumOfTest]=p.getMessage();                
                throw new Exception("Fatal problem");
            }
            
            refactoring.setGetterName(g);
            refactoring.setSetterName(s);
            refactoring.setFieldModifiers(fieldModifier);
            refactoring.setMethodModifiers(methodModifiers);
            refactoring.setAlwaysUseAccessors(alwaysUseAccessors);
            
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
            System.out.println("finish: "+ finish);
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
                compareResources(files, result, className, className);
            } catch (Exception ex) {
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }

    private static StringBuffer getCapitalizedName(Field field) {
        StringBuffer name=new StringBuffer(field.getName());

        name.setCharAt(0,Character.toUpperCase(name.charAt(0)));
        return name;
    }


    public static String computeSetterName(Field field) {
        StringBuffer name=getCapitalizedName(field);

        name.insert(0,"set"); //NOI18N
        return name.toString();
    }

    public static String computeGetterName(Field field) {
        StringBuffer name=getCapitalizedName(field);
        Type retVal=field.getType();

        if (retVal instanceof PrimitiveType && ((PrimitiveType)retVal).getKind().equals(PrimitiveTypeKindEnum.BOOLEAN))
            name.insert(0,"is"); //NOI18N
        else
            name.insert(0,"get"); //NOI18N
        return name.toString();
    }
}
