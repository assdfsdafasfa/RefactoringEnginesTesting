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
package org.netbeans.test.refactoring;

import java.io.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.Type;
import org.netbeans.jmi.javamodel.TypeClass;
import org.netbeans.jmi.javamodel.TypeParameter;
import org.netbeans.jmi.javamodel.UnresolvedClass;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.classpath.RefactoringClassPathImplementation;



public class Utility {
    
    public static void beginTrans(boolean writeAccess) {
        getDefaultRepository().beginTrans(writeAccess);
    }
    
    public static void endTrans(boolean rollback) {
        getDefaultRepository().endTrans(rollback);
    }
    
    public static void endTrans() {
        getDefaultRepository().endTrans();
    }
    
    public static MDRepository getDefaultRepository() {
        return JavaMetamodel.getDefaultRepository();
    }
    
    public static void prepareTest() {
        beginTrans(true);
        //class path
        JavaMetamodel.getManager().setClassPath(RefactoringClassPathImplementation.getDefault());
    }
    
    public static void finishTest() {
        endTrans(false);
    }
    
    public static File getFile(File classPathDataDir, String fileName) {
        return new File(classPathDataDir, fileName);
    }
    
    /** 
     * must be called in a transaction
     */
    public static JavaClass findClass(String s) {
        JavaClass result;
        int i = 20;
        do {
            result = (JavaClass) JavaMetamodel.getManager().getDefaultExtent().getType().resolve(s);
            if (result instanceof UnresolvedClass) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    endTrans();
                    e.printStackTrace();
                    return null;
                }
            }
            i--;
        } while ((result instanceof UnresolvedClass) && i > 0);
        if (result instanceof UnresolvedClass) {
            throw new IllegalStateException("Class " + s + " not found.");
        }
        return result;
    }

    /** 
     * must be called in a transaction
     */    
    public static Type findType(String s, TypeClass typeProxy, JavaClass jc) {
        Type result;
        int i = 20;
        do {
            result = typeProxy.resolve(s);
            if (result instanceof UnresolvedClass) {
                List l=jc.getTypeParameters();
                for (Iterator it=l.iterator();it.hasNext();) {
                    TypeParameter tp=(TypeParameter)(it.next());
                    if (tp.getName().equals(s)) {
                        return tp;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    endTrans();
                    e.printStackTrace();
                    return null;
                }
            }
            i--;
        } while ((result instanceof UnresolvedClass) && i > 0);
        if (result instanceof UnresolvedClass) {
            throw new IllegalStateException("Type " + s + " not found.");
        }
        return result;
    }
    
    public static void copyFile(File src, File trg) throws Exception {
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(trg));
        byte[] buffer=new byte[1024];
        int length=1;
        while (length > 0) {
            length=bis.read(buffer);
            if (length <= 0) {
                break;
            }
            bos.write(buffer, 0, length);
        }
        bis.close();
        bos.close();
    }
}
