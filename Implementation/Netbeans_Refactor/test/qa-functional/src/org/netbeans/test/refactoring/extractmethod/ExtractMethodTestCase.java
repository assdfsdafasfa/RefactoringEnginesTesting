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

package org.netbeans.test.refactoring.extractmethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.netbeans.jmi.javamodel.Resource;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.ExtractMethodRefactoring;
import org.netbeans.modules.refactoring.api.ExtractMethodRefactoring.ParameterInfo;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;

/**
 *
 * @author jp159440
 */
public class ExtractMethodTestCase extends RefactoringTestCase{
    
    /** Creates a new instance of ExtractMethodTestCase */
    public ExtractMethodTestCase(String name) {
        super(name);
    }
    
    protected void extractMethod(String javaClass, int startPos, int endPos, String name, int mod, boolean staticMod, boolean undo) {
        setJavaClass(javaClass);
        Resource resource = jc.getResource();
        String extractFrom = resource.getSourceText().substring(startPos,endPos);
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Extract method");
        boolean finish = false;
        try {
            ref("Extract method from "+extractFrom+" in class "+javaClass);
            ref("name of new method "+name);
            Utility.prepareTest();
            ExtractMethodRefactoring refactoring = new ExtractMethodRefactoring(resource,startPos,endPos);
            if(refProblems(refactoring.preCheck())) throw new Exception("Fatal problem");
            refactoring.setName(name);
            refactoring.setStaticMod(staticMod);
            refactoring.setModifier(mod);
            if(refactoring.getParamTable()!=null) {
                List paramInfo = Arrays.asList(refactoring.getParamTable());
                Collections.sort(paramInfo,new ParamInfoComparator());
                ParameterInfo[] sorted = new ParameterInfo[paramInfo.size()];
                for (int i = 0; i < paramInfo.size(); i++) {
                    sorted[i] = (ParameterInfo) paramInfo.get(i);
                }
                refactoring.setParamTable(sorted);
            }
            if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems(refactoring.prepare(result))) throw new Exception("Fatal problem");
            files=getResources(result);
            if (refProblems(result.doRefactoring(true))) throw new Exception("Fatal problem");
            finish = true;
        } catch (Throwable t) {
            if(t.getMessage()==null || !t.getMessage().equals("Fatal problem")) {
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
    
    protected void extractMethod(String javaClass, String startMark, String endMark, String name, int mod, boolean staticMod, boolean undo) {
        setJavaClass(javaClass);
        Resource resource = jc.getResource();
        String text  =resource.getSourceText();
        int startPos = text.indexOf(startMark);
        assertTrue("Mark "+ startMark+" not found",startPos>-1);
        startPos = startPos+startMark.length()+1;
        int endPos = text.indexOf(endMark);
        assertTrue("Mark "+ endMark+" not found",endPos>-1);
        endPos = endPos -1;
        this.extractMethod(javaClass,startPos,endPos,name,mod,staticMod,undo);
        
    }
    
    private class ParamInfoComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            ExtractMethodRefactoring.ParameterInfo i1 = (ParameterInfo) o1;
            ExtractMethodRefactoring.ParameterInfo i2 = (ParameterInfo) o2;
            return i1.getName().compareTo(i2.getName());
        }
        
    }
}
