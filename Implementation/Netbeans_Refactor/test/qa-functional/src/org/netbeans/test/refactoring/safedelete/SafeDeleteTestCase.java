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

package org.netbeans.test.refactoring.safedelete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.netbeans.jmi.javamodel.Element;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.SafeDeleteRefactoring;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;
import org.netbeans.modules.refactoring.api.Problem;

/**
 *
 * @author jp159440
 */
public class SafeDeleteTestCase extends RefactoringTestCase {
    
    /** Creates a new instance of SafeDeleteTestCase */
    
    public SafeDeleteTestCase(String name) {
        super(name);
    }
    
    protected void safeDelete(String javaClass,Element[] elements, boolean checkInComments, boolean undo) {
        setJavaClass(javaClass);
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Safe delete");
        boolean finish = false;
        ArrayList featuresList = new ArrayList();
        try {
            ref("Safe delete from class "+javaClass);
            Utility.prepareTest();
            SafeDeleteRefactoring refactoring = new SafeDeleteRefactoring(elements);
            if(refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            refactoring.setCheckInComments(checkInComments);
            if (refProblems(refactoring.checkParameters(),true))  throw new Exception("Fatal problem");
            if (refProblems(refactoring.prepare(result),true)) throw new Exception("Fatal problem");
            files=getResources(result);
            if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
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
                }
                compareResources(files, result, javaClass, javaClass);
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }
    
    public boolean refProblems(Problem problem, boolean fatalReferenes) {
        Problem p=problem;
        boolean ret=false;
        if (p != null) {
            ArrayList list=new ArrayList();
            while (p != null) {
                if (p.isFatal()) {
                    ret=true;
                    list.add("Problem fatal: "+p.getMessage());
                } else {
                    if(p.getMessage().startsWith("References to selected element") && fatalReferenes) {
                        ret=true;
                    }
                    list.add("Problem: "+p.getMessage());
                }
                p=p.getNext();
                
            }
            Collections.sort(list);
            for (int i=0;i < list.size();i++) {
                ref(list.get(i));
            }
        }        
        return ret;
    }
}
