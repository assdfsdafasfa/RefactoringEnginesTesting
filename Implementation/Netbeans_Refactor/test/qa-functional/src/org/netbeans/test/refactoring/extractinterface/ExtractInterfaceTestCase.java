/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.test.refactoring.extractinterface;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.jmi.javamodel.Field;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.JavaDoc;
import org.netbeans.jmi.javamodel.JavaModelPackage;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.jmi.javamodel.MultipartId;
import org.netbeans.jmi.javamodel.NamedElement;
import org.netbeans.jmi.javamodel.TypeClass;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.ExtractInterfaceRefactoring;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;

/**
 *
 * @author jp159440
 */
public class ExtractInterfaceTestCase extends RefactoringTestCase{
    
    /** Creates a new instance of ExtractInterfaceTestCase */
    public ExtractInterfaceTestCase(String name) {
        super(name);
    }
    
    protected void extractInterface(String javaClass,String name,MembersToExtract members,boolean undo) {
        setJavaClass(javaClass);
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Extract interface");
        boolean finish = false;
        ArrayList featuresList = new ArrayList();
        String ifaceFullName = "";
        try {
            ref("Extract new interface "+name+" from "+javaClass);
            Utility.prepareTest();
            jc.getContents();
            ExtractInterfaceRefactoring refactoring = new ExtractInterfaceRefactoring(jc);
            if(refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            refactoring.setIfcName(name);
            List list = new LinkedList();
            if(members!=null) {
                Object[] objs = members.resolveClass(jc).toArray();
                if(objs.length != 0) {
                    NamedElement named[] = new NamedElement[objs.length];
                    for (int i = 0; i < objs.length; i++) {
                        named[i] = (NamedElement) objs[i];
                    }
                    refactoring.setMembers(named);
                }
            }
            if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
            files=getResources(result);
            if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            ifaceFullName = javaClass.substring(0,javaClass.lastIndexOf('.')+1)+name;
            JavaClass jc = Utility.findClass(ifaceFullName);
            refJavaDoc(jc.getResource().getSourceText());
            finish = true;
        } catch (Throwable t) {
            if(t.getMessage()==null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace();
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
                    boolean deleted = true;
                    try {
                        JavaClass jc = Utility.findClass(ifaceFullName);
                        deleted = false;                        
                    } catch (IllegalStateException ise) {
                        deleted = true;
                    }
                    if(!deleted) ref("Iface "+ ifaceFullName+ " was not deleted");
                }
                compareResources(files, result, javaClass, name);
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }
    
    private void refJavaDoc(String source) {
        int pos = source.indexOf("@author");
        String res;
        if(pos>-1) {
            int posEnd = source.indexOf("*/",pos);
            if(posEnd==-1) posEnd = pos;
            res = source.substring(0,pos)+"@author XXXX \n"+source.substring(posEnd);            
        } else res = source;
        ref(res);
    }
    
    protected  class MembersToExtract {
        List methods;
        List fields;
        List classes;
        List ifaces;
        
        public MembersToExtract() {
            methods = new LinkedList();
            fields = new LinkedList();
            classes = new LinkedList();
            ifaces =  new LinkedList();
        }
        
        public void addField(String name) {
            fields.add(name);
        }
        
        public void addClass(String name) {
            classes.add(name);
        }
        
        public void addIface(String name) {
            ifaces.add(name);
        }
        
        public void addMethod(String name,String[] args) {
            methods.add(new Object[]{name,args});
        }
        
        public List resolveClass(JavaClass jc) {
            TypeClass typeProxy = ((JavaModelPackage) jc.refOutermostPackage()).getType();
            LinkedList result = new LinkedList();
            for(Iterator it = fields.iterator();it.hasNext();) {
                String name = (String) it.next();
                Field f  = jc.getField(name,false);
                assertNotNull("Field "+ name + " not found",f);
                result.add(f);
            }
            for(Iterator it = classes.iterator();it.hasNext();) {
                String name = (String) it.next();
                JavaClass ijc  = jc.getInnerClass(name,false);
                assertNotNull("Inner class "+ name + " not found",ijc);
                result.add(ijc);
            }
            for(Iterator it = ifaces.iterator();it.hasNext();) {
                String name = (String) it.next();
                List multiIds = jc.getInterfaceNames();
                for(Iterator i2 = multiIds.iterator();i2.hasNext();) {
                    MultipartId id = (MultipartId) i2.next();
                    if(id.getName().equals(name)) result.add(id);
                }
            }
            for(Iterator it = methods.iterator();it.hasNext();) {
                Object[] met = (Object[]) it.next();
                String name = (String) met[0];
                String[] args = (String[]) met[1];
                ArrayList argsList = new ArrayList();
                if(args!=null) for (int i = 0; i < args.length; i++) {
                    argsList.add(Utility.findType(args[i], typeProxy, jc));
                }
                Method method = jc.getMethod(name,argsList,false);
                assertNotNull("Method "+name+" is not found",method);
                result.add(method);
            }
            return result;
        }
        
        
        
    }
}


