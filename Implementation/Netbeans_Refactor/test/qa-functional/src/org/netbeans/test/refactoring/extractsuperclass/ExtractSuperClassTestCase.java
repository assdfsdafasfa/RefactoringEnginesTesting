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

package org.netbeans.test.refactoring.extractsuperclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.jmi.javamodel.Field;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.JavaModelPackage;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.jmi.javamodel.MultipartId;
import org.netbeans.jmi.javamodel.NamedElement;
import org.netbeans.jmi.javamodel.TypeClass;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.ExtractInterfaceRefactoring;
import org.netbeans.modules.refactoring.api.ExtractSuperClassRefactoring;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;

/**
 *
 * @author jp159440
 */
public class ExtractSuperClassTestCase extends RefactoringTestCase {
    
    /** Creates a new instance of ExtractSuperClassTestCase */
    public ExtractSuperClassTestCase(String name) {
        super(name);
    }
    
    protected void extractSuperClass(String javaClass, String name, ExtractSuperClassRefactoring.MemberInfo[] members, boolean undo) {
        setJavaClass(javaClass);
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Extract superclass");
        boolean finish = false;
        ArrayList featuresList = new ArrayList();
        String superFullName = "";
        try {
            ref("Extract super class "+name+" from "+javaClass);
            Utility.prepareTest();
            ExtractSuperClassRefactoring refactoring = new ExtractSuperClassRefactoring(jc);
            if(refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            refactoring.setSuperClassName(name);
            if(members!=null) refactoring.setMembers(members);
            if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
            files=getResources(result);
            if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            superFullName = javaClass.substring(0,javaClass.lastIndexOf('.')+1)+name;
            JavaClass jc = Utility.findClass(superFullName);
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
                        JavaClass jc = Utility.findClass(superFullName);
                        deleted = false;                        
                    } catch (IllegalStateException ise) {
                        deleted = true;
                    }
                    if(!deleted) ref("Class "+ superFullName+ " was not deleted");
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
    
    public Method getMethod(String javaClass,String name, String[] args) {
        JavaClass jc = Utility.findClass(javaClass);
        TypeClass typeProxy = ((JavaModelPackage) jc.refOutermostPackage()).getType();
        List argsList = new ArrayList();
        if(args!=null) for (int i = 0; i < args.length; i++) {
            argsList.add(Utility.findType(args[i],typeProxy,jc));
        }
        return jc.getMethod(name,argsList,false);
        
    }
    
    public Field getField(String javaClass,String name) {
        JavaClass jc = Utility.findClass(javaClass);
        return jc.getField(name,false);
    }
    
    public JavaClass getInnerClass(String javaClass, String name) {
        JavaClass jc = Utility.findClass(javaClass);
        return jc.getInnerClass(name,false);
    }
    
    public MultipartId getIface(String javaClass, String name) {
        JavaClass jc = Utility.findClass(javaClass);
        NamedElement namedE = null;
        for(Iterator it = jc.getInterfaceNames().iterator();it.hasNext();) {
            MultipartId multiID = (MultipartId) it.next();
            if(multiID.getName().equals(name)) return multiID;
            
        }
        return null;
    }
}
