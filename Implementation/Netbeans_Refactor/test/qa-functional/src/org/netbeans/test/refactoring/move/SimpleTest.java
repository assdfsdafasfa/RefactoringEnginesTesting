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

package org.netbeans.test.refactoring.move;

import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * MOVE refactoring simple tests
 * @author  Emanuel Hucka
 */
public class SimpleTest extends MoveTestCase {

    private static final String CLASS_NAME = "org.netbeans.tests.examples.Test";
    private static final String CLASS_NAME_1 = "org.netbeans.tests.examples.packa.Bean";
    private static final String CLASS_NAME_2 = "org.netbeans.tests.examples.packb.BeanA";
    private static final String CLASS_NAME_3 = "org.netbeans.tests.examples.packb.BeanB";
    private static final String CLASS_NAME_4 = "org.netbeans.tests.examples.packb.BeanD";
    private static final String CLASS_NAME_5 = "org.netbeans.tests.examples.packb.BeansD";
    private static final String CLASS_NAME_6 = "Main";
    private static final String CLASS_NAME_7 = "org.netbeans.tests.examples.packc.BPanel";
    
    private static final String IFACE_NAME_1 = "org.netbeans.tests.examples.packc.Makable";
    private static final String IFACE_NAME_2 = "org.netbeans.tests.examples.packa.Testable";
    
    private static final String PACKAGE_NAME_1 ="org.netbeans.tests.examples.packa";
    private static final String PACKAGE_NAME_2 ="org.netbeans.tests.examples.packb";
    private static final String PACKAGE_NAME_3 ="org.netbeans.tests.examples.packc";
    
    /** Creates a new instance of Signature1Test */
    public SimpleTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        
        suite.addTest(new SimpleTest("testClass1"));
        suite.addTest(new SimpleTest("testClass2"));
        suite.addTest(new SimpleTest("testClass3"));
        suite.addTest(new SimpleTest("testClass4"));
        suite.addTest(new SimpleTest("testClass5"));
        suite.addTest(new SimpleTest("testClass6"));
        suite.addTest(new SimpleTest("testClass7"));
        suite.addTest(new SimpleTest("testInterface1"));
        suite.addTest(new SimpleTest("testInterface2"));
        
        //undo
        suite.addTest(new SimpleTest("testUndoMoveClass1"));
        suite.addTest(new SimpleTest("testUndoMoveClass2"));
        suite.addTest(new SimpleTest("testUndoMoveClass3"));
        suite.addTest(new SimpleTest("testUndoMoveClass4"));
        suite.addTest(new SimpleTest("testUndoMoveClass5"));
        suite.addTest(new SimpleTest("testUndoMoveClass6"));
        suite.addTest(new SimpleTest("testUndoMoveInterface1"));
        
        
        return suite;
    }
    
    public void testUndoMoveClass1() {
        moveClass(CLASS_NAME_1, PACKAGE_NAME_2, true);
    }
    
    public void testUndoMoveClass2() {
        moveClass(CLASS_NAME_2, PACKAGE_NAME_1, true);
    }
    
    public void testUndoMoveClass3() {
        moveClass(CLASS_NAME_3, "", true);
    }
    
    public void testUndoMoveClass4() {
        moveClass(CLASS_NAME_5, PACKAGE_NAME_1, true);
    }
    
    public void testUndoMoveClass5() {
        moveClass(CLASS_NAME_6, PACKAGE_NAME_3, true);
    }
    
    public void testUndoMoveClass6() {
        moveClass(CLASS_NAME_7, PACKAGE_NAME_1, true);
    }
    
    public void testUndoMoveInterface1() {
        moveClass(IFACE_NAME_1, PACKAGE_NAME_1, true);
    }
    
    //**************************************************
    public void testClass1() {
        log("Same package");
        moveClass(CLASS_NAME_1, PACKAGE_NAME_1, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass2() {
        moveClass(CLASS_NAME_1, PACKAGE_NAME_2, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass3() {
        log("Move frequently used class, annonymous inner class, retyping, constructor, static field");
        moveClass(CLASS_NAME_2, PACKAGE_NAME_1, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass4() {
        log("Move frequently used class, annonymous inner class, retyping, constructor, static field - to default package");
        moveClass(CLASS_NAME_3, "", false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass5() {
        log("Move a class from default package.");
        moveClass(CLASS_NAME_6, PACKAGE_NAME_3, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass6() {
        log("Move a class used with a guarded block.");
        moveClass(CLASS_NAME_7, PACKAGE_NAME_1, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass7() {
        log("move a file with many toplevel classes");
        moveClass(CLASS_NAME_5, PACKAGE_NAME_3, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    //bud implementation of MCR API
    public void testClass8() {
        log("Move second top level class - non-public");
        moveClass(CLASS_NAME_4, PACKAGE_NAME_1, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testInterface1() {
        moveClass(IFACE_NAME_1, PACKAGE_NAME_1, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testInterface2() {
        moveClass(IFACE_NAME_2, PACKAGE_NAME_3, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    /**
     * Used for running test from inside the IDE by internal execution.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
}
