/*
 * WhereUsedTestCase.java
 *
 * Created on August 23, 2004, 3:33 PM
 */

package org.netbeans.test.refactoring.usages;

import java.util.ArrayList;
import java.util.Iterator;
import org.netbeans.jmi.javamodel.Constructor;
import org.netbeans.jmi.javamodel.Feature;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.WhereUsedQuery;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;

/**
 *
 * @author  eh103527
 */
public class WhereUsedTestCase extends RefactoringTestCase {
    
    public static final int CLASS_USAGES=0;
    public static final int CLASS_SUBTYPES=1;
    public static final int CLASS_DIRECTSUBTYPES=2;
    
    /** Creates a new instance of WhereUsedTestCase */
    public WhereUsedTestCase(String name) {
        super(name);
    }
    
    protected void findClass(String className) {
        setJavaClass(className);
        Utility.beginTrans(false);
        try {
            ref("Find usages of: "+className);
            WhereUsedQuery wu=new WhereUsedQuery(jc);
            wu.setSearchInComments(true);
            RefactoringSession result = RefactoringSession.create(null);
            refProblems(wu.prepare(result));
            refUsages(result);
        } finally {
            Utility.endTrans();
        }
        ref("");
    }
    
    protected void findClassParams(String className, int type) {
        setJavaClass(className);
        Utility.beginTrans(false);
        try {
            ref("Find usages of: "+className);
            WhereUsedQuery wu=new WhereUsedQuery(jc);
            switch (type) {
                case CLASS_USAGES:
                    wu.setFindUsages(true);
                    break;
                case CLASS_SUBTYPES:
                    wu.setFindSubclasses(true);
                    break;
                case CLASS_DIRECTSUBTYPES:
                    wu.setFindDirectSubclassesOnly(true);
                    break;
            }
            wu.setSearchInComments(true);
            if (refProblems(wu.checkParameters())) throw new Exception("Fatal problem");
            RefactoringSession result = RefactoringSession.create(null);
            refProblems(wu.prepare(result));
            refUsages(result);
        } catch (Throwable t) {
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                assertTrue(t.getMessage(), false);
            }            
        } finally {
            Utility.endTrans();
        }
        ref("");
    }
    
    protected void findField(String className, String fieldName) {
        setJavaClass(className);
        Utility.beginTrans(false);
        try {
            org.netbeans.jmi.javamodel.Field field = jc.getField(fieldName, false);
            assertNotNull("Field "+fieldName+" of "+className+" is null", field);
            ref("Find usages of: "+fieldName);
            WhereUsedQuery wu=new WhereUsedQuery(field);
            wu.setSearchInComments(true);
            RefactoringSession result = RefactoringSession.create(null);
            refProblems(wu.prepare(result));
            refUsages(result);
        } finally {
            Utility.endTrans();
        }
        ref("");
    }

    protected void findAnnotationTypeMember(String annotationName, String memberName) {
        setJavaClass(annotationName);
        Utility.beginTrans(false);
        try {
            Feature member = null;
            for (Iterator it=jc.getFeatures().iterator();it.hasNext();) {
                Feature f=(Feature)(it.next());
                if (f.getName().equals(memberName)) {
                    member=f;
                    break;
                }
            }
            assertNotNull("Member "+memberName+" of "+annotationName+" is null", member);
            ref("Find usages of: "+memberName);
            WhereUsedQuery wu=new WhereUsedQuery(member);
            wu.setSearchInComments(true);
            RefactoringSession result = RefactoringSession.create(null);
            refProblems(wu.prepare(result));
            refUsages(result);
        } finally {
            Utility.endTrans();
        }
        ref("");
    }
    
    protected void findConstructor(String className, String[] arguments) {
        setJavaClass(className);
        Utility.beginTrans(false);
        try {
            ArrayList list=new ArrayList();
            String refs="Find usages of "+className+"(";
            if (arguments != null && arguments.length > 0) {
                for (int i=0;i < arguments.length;i++) {
                    list.add(Utility.findType(arguments[i], typeProxy, jc));
                    if (i > 0) {
                        refs+=", ";
                    }
                    refs+=arguments[i];
                }
            }
            refs+=")";
            ref(refs);
            Constructor cons = jc.getConstructor(list, false);
            WhereUsedQuery wu=new WhereUsedQuery(cons);
            wu.setSearchInComments(true);
            RefactoringSession result = RefactoringSession.create(null);
            refProblems(wu.prepare(result));
            refUsages(result);
        } finally {
            Utility.endTrans();
        }
        ref("");
    }
    
    protected void findMethod(String className, String methodName, String[] arguments) {
        setJavaClass(className);
        Utility.beginTrans(false);
        try {
            ArrayList list=new ArrayList();
            String refs="Find usages of "+className+"."+methodName+"(";
            if (arguments != null && arguments.length > 0) {
                for (int i=0;i < arguments.length;i++) {
                    list.add(Utility.findType(arguments[i], typeProxy, jc));
                    if (i > 0) {
                        refs+=", ";
                    }
                    refs+=arguments[i];
                }
            }
            refs+=")";
            ref(refs);
            Method method = jc.getMethod(methodName, list, false);
            assertNotNull("Method "+refs+" of "+className+" is null", method);
            WhereUsedQuery wu=new WhereUsedQuery(method);
            wu.setSearchInComments(true);
            RefactoringSession result = RefactoringSession.create(null);
            refProblems(wu.prepare(result));
            refUsages(result);
        } finally {
            Utility.endTrans();
        }
        ref("");
    }
    
    protected void findMethodParams(String className, String methodName, String[] arguments, boolean fromBaseClass, boolean methodOverriders, boolean findUsages) {
        setJavaClass(className);
        Utility.beginTrans(false);
        try {
            ArrayList list=new ArrayList();
            String refs="Find usages of "+className+"."+methodName+"(";
            if (arguments != null && arguments.length > 0) {
                for (int i=0;i < arguments.length;i++) {
                    list.add(typeProxy.resolve(arguments[i]));
                    if (i > 0) {
                        refs+=", ";
                    }
                    refs+=arguments[i];
                }
            }
            refs+=")";
            ref(refs);
            Method method = jc.getMethod(methodName, list, false);
            assertNotNull("Method "+refs+" of "+className+" is null", method);
            WhereUsedQuery wu=new WhereUsedQuery(method);
            wu.setSearchFromBaseClass(fromBaseClass);
            wu.setFindOverridingMethods(methodOverriders);
            wu.setFindUsages(findUsages);
            wu.setSearchInComments(true);
            if (refProblems(wu.checkParameters())) throw new Exception("Fatal problem");
            RefactoringSession result = RefactoringSession.create(null);
            refProblems(wu.prepare(result));
            refUsages(result);
        } catch (Throwable t) {
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                assertTrue(t.getMessage(), false);
            }            
        } finally {
            Utility.endTrans();
        }
        ref("");
    }
    
}
