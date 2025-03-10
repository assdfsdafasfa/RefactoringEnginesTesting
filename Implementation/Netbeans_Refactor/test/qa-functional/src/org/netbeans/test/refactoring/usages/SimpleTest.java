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

package org.netbeans.test.refactoring.usages;

import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;



/**
 * Find Usages simple tests
 * @author  Emanuel Hucka
 */
public class SimpleTest extends WhereUsedTestCase {

    private static final String CLASS_NAME = "org.netbeans.tests.examples.packa.Bean";
    private static final String CLASS_NAME_2 = "org.netbeans.tests.examples.packb.BeanA";
    private static final String CLASS_NAME_3 = "org.netbeans.tests.examples.packb.BeanB";
    private static final String CLASS_NAME_4 = "org.netbeans.tests.examples.packb.BeanD";
    private static final String CLASS_NAME_5 = "org.netbeans.tests.examples.packb.BeansD.BeanDA";
    private static final String CLASS_NAME_6 = "org.netbeans.tests.examples.packc.BPanel";
    
    private static final String IFACE_NAME_1 = "org.netbeans.tests.examples.packc.Makable";
    private static final String IFACE_NAME_2 = "org.netbeans.tests.examples.packa.Testable";
    
    /** Creates a new instance of Signature1Test */
    public SimpleTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new SimpleTest("testClass"));
        suite.addTest(new SimpleTest("testClassAllSubTypes"));
        suite.addTest(new SimpleTest("testClassDirectSubTypes"));
        suite.addTest(new SimpleTest("testInterface"));
        suite.addTest(new SimpleTest("testInterfaceAllSubTypes"));
        suite.addTest(new SimpleTest("testInterfaceDirectSubTypes"));
        suite.addTest(new SimpleTest("testConstructor"));
        suite.addTest(new SimpleTest("testMethodUsages"));
        suite.addTest(new SimpleTest("testMethodUsagesOverriding"));
        suite.addTest(new SimpleTest("testMethodOverriding"));
        suite.addTest(new SimpleTest("testMethodOverridingBase"));
        suite.addTest(new SimpleTest("testMethodUsagesBase"));
        suite.addTest(new SimpleTest("testMethodUsagesBaseOverriding"));
        suite.addTest(new SimpleTest("testField"));
        return suite;
    }
    
    public void testClass() {
        findClass(CLASS_NAME);
        findClass(CLASS_NAME_2);
        findClass(CLASS_NAME_3);
        findClass(CLASS_NAME_4);
        findClass(CLASS_NAME_5);
        findClass("java.lang.String");
    }
    
    public void testClassAllSubTypes() {
        findClassParams(CLASS_NAME, WhereUsedTestCase.CLASS_SUBTYPES);
        findClassParams(CLASS_NAME_2, WhereUsedTestCase.CLASS_SUBTYPES);
        findClassParams(CLASS_NAME_3, WhereUsedTestCase.CLASS_SUBTYPES);
        findClassParams(CLASS_NAME_4, WhereUsedTestCase.CLASS_SUBTYPES);
        findClassParams(CLASS_NAME_5, WhereUsedTestCase.CLASS_SUBTYPES);
        findClassParams("java.awt.Container", WhereUsedTestCase.CLASS_SUBTYPES);
    }
    
    public void testClassDirectSubTypes() {
        findClassParams(CLASS_NAME, WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
        findClassParams(CLASS_NAME_2, WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
        findClassParams(CLASS_NAME_3, WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
        findClassParams(CLASS_NAME_4, WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
        findClassParams(CLASS_NAME_5, WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
        findClassParams("javax.swing.JFrame", WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
    }
    
    public void testInterface() {
        findClass(IFACE_NAME_1);
        findClass(IFACE_NAME_2);
    }
    
    public void testInterfaceAllSubTypes() {
        findClassParams(IFACE_NAME_1, WhereUsedTestCase.CLASS_SUBTYPES);
        findClassParams(IFACE_NAME_2, WhereUsedTestCase.CLASS_SUBTYPES);
        findClassParams("javax.accessibility.Accessible", WhereUsedTestCase.CLASS_SUBTYPES);
    }
    
    public void testInterfaceDirectSubTypes() {
        findClassParams(IFACE_NAME_1, WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
        findClassParams(IFACE_NAME_2, WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
        findClassParams("java.awt.event.ActionListener", WhereUsedTestCase.CLASS_DIRECTSUBTYPES);
    }
    
    public void testField() {
        findField(CLASS_NAME, "refID");
        findField(CLASS_NAME_2, "protectedProperty");
        findField(IFACE_NAME_1, "CODE");
    }
    
    public void testConstructor() {
        findConstructor(CLASS_NAME_2, new String[0]);
        findConstructor(CLASS_NAME_3, new String[0]);
        findConstructor(CLASS_NAME_5, new String[] {"java.lang.String"});
    }
    
    public void testMethodUsages() {
        findMethodParams(IFACE_NAME_1, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, false, false, true);
        findMethodParams(IFACE_NAME_2, "test", null, false, false, true);
        findMethodParams(CLASS_NAME, "abc", null, false, false, true);
        findMethodParams(CLASS_NAME_2, "getProtectedProperty", null, false, false, true);
        findMethodParams("java.io.PrintStream", "println", new String[] {"java.lang.String"}, false, false, true);
    }
    
    public void testMethodUsagesOverriding() {
        findMethodParams(IFACE_NAME_1, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, false, true, true);
        findMethodParams(IFACE_NAME_2, "test", null, false, true, true);
        findMethodParams(CLASS_NAME, "abc", null, false, true, true);
        findMethodParams(CLASS_NAME_2, "getProtectedProperty", null, false, true, true);
        findMethodParams("java.awt.event.ActionListener", "actionPerformed", new String[] {"java.awt.event.ActionEvent"}, false, true, true);
    }
    
    public void testMethodOverriding() {
        findMethodParams(IFACE_NAME_1, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, false, true, false);
        findMethodParams(IFACE_NAME_2, "test", null, false, true, false);
        findMethodParams(CLASS_NAME, "abc", null, false, true, false);
        findMethodParams(CLASS_NAME_2, "getProtectedProperty", null, false, true, false);
        findMethodParams("java.awt.event.ActionListener", "actionPerformed", new String[] {"java.awt.event.ActionEvent"}, false, true, false);
    }
    
    public void testMethodOverridingBase() {
        findMethodParams(CLASS_NAME_3, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, true, true, false);
        findMethodParams(CLASS_NAME_2, "test", null, true, true, false);
        findMethodParams(CLASS_NAME_2, "getRefID", null, true, true, false);
        findMethodParams(CLASS_NAME_4, "getProtectedProperty", null, true, true, false);
        findMethodParams(CLASS_NAME_6, "actionPerformed", new String[] {"java.awt.event.ActionEvent"}, true, false, true);
    }
    
    public void testMethodUsagesBase() {
        findMethodParams(CLASS_NAME_3, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, true, true, false);
        findMethodParams(CLASS_NAME_2, "test", null, true, true, false);
        findMethodParams(CLASS_NAME_2, "getRefID", null, true, true, false);
        findMethodParams(CLASS_NAME_4, "getProtectedProperty", null, true, true, false);
        findMethodParams(CLASS_NAME_6, "actionPerformed", new String[] {"java.awt.event.ActionEvent"}, true, false, true);
    }
    
    public void testMethodUsagesBaseOverriding() {
        findMethodParams(CLASS_NAME_3, "make", new String[] {"int", "java.lang.String", "org.netbeans.tests.examples.packa.Bean"}, true, true, false);
        findMethodParams(CLASS_NAME_2, "test", null, true, true, false);
        findMethodParams(CLASS_NAME_2, "getRefID", null, true, true, false);
        findMethodParams(CLASS_NAME_4, "getProtectedProperty", null, true, true, false);
        findMethodParams(CLASS_NAME_6, "actionPerformed", new String[] {"java.awt.event.ActionEvent"}, true, false, true);
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
