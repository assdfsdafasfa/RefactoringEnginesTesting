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

package org.netbeans.test.refactoring.rename;

import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * RENAME refactoring simple tests
 * @author  Emanuel Hucka
 */
public class SimpleTest extends RenameTestCase {

    private static final String CLASS_NAME = "org.netbeans.tests.examples.Test";
    private static final String CLASS_NAME_1 = "org.netbeans.tests.examples.packa.Bean";
    private static final String CLASS_NAME_2 = "org.netbeans.tests.examples.packb.BeanA";
    private static final String CLASS_NAME_3 = "org.netbeans.tests.examples.packb.BeanB";
    private static final String CLASS_NAME_4 = "org.netbeans.tests.examples.packb.BeanD";
    private static final String CLASS_NAME_5 = "org.netbeans.tests.examples.packb.BeansD.BeanDA";
    private static final String CLASS_NAME_6 = "Main";
    private static final String CLASS_NAME_7 = "org.netbeans.tests.examples.packc.AFrame";
    private static final String CLASS_NAME_8 = "org.netbeans.tests.examples.packc.BPanel";
    private static final String CLASS_NAME_9 = "org.netbeans.tests.examples.packc.BeanE";
    
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
        //undo tests
        suite.addTest(new SimpleTest("testUndoRenameField1"));
        suite.addTest(new SimpleTest("testUndoRenameField2"));
        suite.addTest(new SimpleTest("testUndoRenameField3"));
        suite.addTest(new SimpleTest("testUndoRenameMethod1"));
        suite.addTest(new SimpleTest("testUndoRenameMethod2"));
        suite.addTest(new SimpleTest("testUndoRenameMethod3"));
        suite.addTest(new SimpleTest("testUndoRenameClass1"));
        suite.addTest(new SimpleTest("testUndoRenameClass2"));
        suite.addTest(new SimpleTest("testUndoRenameClass3"));
        suite.addTest(new SimpleTest("testUndoRenameClass4"));
        suite.addTest(new SimpleTest("testUndoRenameClass5"));
        suite.addTest(new SimpleTest("testUndoRenameInterface1"));
        suite.addTest(new SimpleTest("testUndoRenamePackage1"));
        
        suite.addTest(new SimpleTest("testField1"));
        suite.addTest(new SimpleTest("testField2"));
        suite.addTest(new SimpleTest("testField3"));
        suite.addTest(new SimpleTest("testField4"));
        suite.addTest(new SimpleTest("testField5"));
        suite.addTest(new SimpleTest("testLocalField1"));
        suite.addTest(new SimpleTest("testLocalField2"));
        suite.addTest(new SimpleTest("testMethod1"));
        suite.addTest(new SimpleTest("testMethod2"));
        suite.addTest(new SimpleTest("testMethod3"));
        suite.addTest(new SimpleTest("testMethod4"));
        suite.addTest(new SimpleTest("testMethod5"));
        suite.addTest(new SimpleTest("testClass1"));
        suite.addTest(new SimpleTest("testClass2"));
        suite.addTest(new SimpleTest("testClass3"));
        suite.addTest(new SimpleTest("testClass4"));
        suite.addTest(new SimpleTest("testClass5"));
        suite.addTest(new SimpleTest("testClass6"));
        suite.addTest(new SimpleTest("testClass7"));
        suite.addTest(new SimpleTest("testClass8"));
        suite.addTest(new SimpleTest("testInterface1"));
        suite.addTest(new SimpleTest("testInterface2"));
        suite.addTest(new SimpleTest("testPackage1"));
        suite.addTest(new SimpleTest("testPackage2"));
        suite.addTest(new SimpleTest("testPackage3"));
        suite.addTest(new SimpleTest("testPackage4"));
        return suite;
    }
    
    public void testUndoRenameClass1() {
        renameClass(CLASS_NAME_1, "BeanRenamed", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameClass2() {
        log("Rename second top level class");
        renameClass(CLASS_NAME_4, "BeanDRenamed", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameClass3() {
        log("Rename static inner class");
        renameClass(CLASS_NAME_5, "BeanDARenamed", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameClass4() {
        log("Rename a class from default package.");
        renameClass(CLASS_NAME_6, "MainRenamed", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameClass5() {
        log("Rename a form class used in another form's guarded block.");
        renameClass(CLASS_NAME_8, "BeanPanel", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameInterface1() {
        renameClass(IFACE_NAME_1, "MakableRenamed", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameField1() {
        renameField(CLASS_NAME_1, "refID", "refIDRenamed", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameField2() {
        log("Rename static field");
        renameField(IFACE_NAME_1, "CODE", "CODE_RENAMED", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameField3() {
        log("Rename guarded field in form");
        renameField(CLASS_NAME_7, "button", "mainB", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameMethod1() {
        log("rename method of an interface");
        renameMethod(IFACE_NAME_1, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, "makeRenamed", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameMethod2() {
        log("frequently used method of a class");
        renameMethod(CLASS_NAME_1, "abc", new String[] {}, "def", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenameMethod3() {
        log("rename guarded method");
        renameMethod(CLASS_NAME_7, "initComponents", new String[] {}, "createComponents", false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoRenamePackage1() {
        log("simple rename package");
        renamePackage(PACKAGE_NAME_1, PACKAGE_NAME_1+"renamed", CLASS_NAME, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass1() {
        log("Wrong class name");
        renameClass(CLASS_NAME_1, "123BeanRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass2() {
        renameClass(CLASS_NAME_1, "BeanRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass3() {
        log("Rename frequently used class, annonymous inner class, retyping, constructor, static field");
        renameClass(CLASS_NAME_2, "BeanARenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass4() {
        log("Rename frequently used class, annonymous inner class, retyping, constructor, static field");
        renameClass(CLASS_NAME_3, "BeanBRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass5() {
        log("Rename second top level class");
        renameClass(CLASS_NAME_4, "BeanDRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass6() {
        log("Rename static inner class");
        renameClass(CLASS_NAME_5, "BeanDARenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass7() {
        log("Rename a class from default package.");
        renameClass(CLASS_NAME_6, "MainRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
        
    public void testClass8() {
        log("Rename a form class used in another form's guarded block.");
        renameClass(CLASS_NAME_8, "BeanPanel", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testInterface1() {
        renameClass(IFACE_NAME_1, "MakableRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testInterface2() {
        renameClass(IFACE_NAME_2, "TestableRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField1() {
        log("Wrong field name");
        renameField(CLASS_NAME_1, "refID", "22222", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField2() {
        renameField(CLASS_NAME_1, "refID", "refIDRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField3() {
        renameField(CLASS_NAME_2, "protectedProperty", "protectedPropertyRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField4() {
        log("Rename static field");
        renameField(IFACE_NAME_1, "CODE", "CODE_RENAMED", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField5() {
        log("Rename guarded field in form");
        renameField(CLASS_NAME_7, "button", "mainB", false, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testLocalField1() {
        renameMethodParameter(CLASS_NAME_9, "count", new String[] {"double[]", "double[]"}, "v1", "vector1", false, false);
        renameMethodParameter(CLASS_NAME_9, "count", new String[] {"double[]", "double[]"}, "v2", "vector2", false, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testLocalField2() {
        renameLocalVariable(CLASS_NAME_9, "count", new String[] {"double[]", "double[]"}, 1, "len1", "length1", false, false);
        renameLocalVariable(CLASS_NAME_9, "count", new String[] {"double[]", "double[]"}, 1, "len2", "length2", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testMethod1() {
        log("rename method of an interface");
        renameMethod(IFACE_NAME_1, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, "makeRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testMethod2() {
        log("invalid method name");
        renameMethod(CLASS_NAME_1, "abc", new String[] {}, "111", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testMethod3() {
        log("frequently used method of a class");
        renameMethod(CLASS_NAME_1, "abc", new String[] {}, "def", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testMethod4() {
        log("rename frequently used getter");
        renameMethod(CLASS_NAME_2, "getProtectedProperty", new String[] {}, "getProtectedPropertyRenamed", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testMethod5() {
        log("rename guarded method");
        renameMethod(CLASS_NAME_7, "initComponents", new String[] {}, "createComponents", false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testPackage1() {
        log("simple rename package");
        renamePackage(PACKAGE_NAME_1, PACKAGE_NAME_1+"renamed", CLASS_NAME, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testPackage2() {
        log("rename with the same name");
        renamePackage(PACKAGE_NAME_2, PACKAGE_NAME_2, CLASS_NAME, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testPackage3() {
        log("rename to just renamed package");
        renamePackage(PACKAGE_NAME_2, PACKAGE_NAME_1, CLASS_NAME, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testPackage4() {
        log("'system name'");
        renamePackage(PACKAGE_NAME_3, "com"+PACKAGE_NAME_3.substring(PACKAGE_NAME_3.indexOf('.')), CLASS_NAME, false);
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
