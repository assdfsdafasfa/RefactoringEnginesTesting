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
package org.netbeans.test.refactoring.undomanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import junit.textui.TestRunner;
import org.netbeans.jmi.javamodel.Assignment;
import org.netbeans.jmi.javamodel.ElementReference;
import org.netbeans.jmi.javamodel.ExpressionStatement;
import org.netbeans.modules.refactoring.spi.RefactoringElementsBag;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.JavaModelPackage;
import org.netbeans.jmi.javamodel.JavaPackage;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.jmi.javamodel.MultipartId;
import org.netbeans.jmi.javamodel.NamedElement;
import org.netbeans.jmi.javamodel.Resource;
import org.netbeans.jmi.javamodel.StatementBlock;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.MoveClassRefactoring;
import org.netbeans.test.refactoring.Utility;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.URLMapper;


/**
 * @author  Pavel Flaska
 */
public class UndoMoveClassTest extends NbTestCase {

    private FileObject targetCPRoot;
    private String targetName;
    
    public UndoMoveClassTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite(UndoMoveClassTest.class);
        return suite;
    }
    
    protected void setUp() {
        targetName = "org.netbeans.test.undomanager";
        targetCPRoot = FileUtil.toFileObject(getDataDir());
    }
    
    
    public void testUndoMove() {
        JavaClass clazz = (JavaClass) Utility.findClass("org.netbeans.test.undomanager.testpackage2.RootClass");
        JavaPackage javaPackage = ((JavaModelPackage) clazz.refOutermostPackage()).getJavaPackage().resolvePackage("org.netbeans.test.undomanager");
        Resource resource = (Resource) clazz.getResource();
        MoveClassRefactoring refactoring = new MoveClassRefactoring(Collections.singletonList(resource));
        refactoring.preCheck();
        refactoring.setTargetClassPathRoot(targetCPRoot);
        refactoring.setTargetPackageName(targetName);
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("hejbni tridou");
        refactoring.prepare(result);
        assertNull(result.doRefactoring(false));
        try { Thread.sleep(100); } catch (InterruptedException ie) {};
        JavaMetamodel.getUndoManager().undo();
        Utility.beginTrans(false);
        boolean fail = true;
        try {
            JavaClass ext = (JavaClass) Utility.findClass("org.netbeans.test.undomanager.testpackage2.Extender1");
            Method m = (Method) ext.getContents().get(1);
            StatementBlock sb = m.getBody();
            ExpressionStatement es = (ExpressionStatement) sb.getStatements().get(0);
            Assignment assign = (Assignment) es.getExpression();
            ElementReference id = (ElementReference) assign.getLeftSide();
            NamedElement ne = id.getElement();
            if (id instanceof MultipartId) {
                getLog().println("Failed!");
                fail = true;
            } else {
                fail = false;
            }
            getLog().println("Name = " + ne.getName() + "; Class = " + ne.getClass());
        } finally {
            Utility.endTrans();
        }
        assertFalse(fail);
    }
    
}
