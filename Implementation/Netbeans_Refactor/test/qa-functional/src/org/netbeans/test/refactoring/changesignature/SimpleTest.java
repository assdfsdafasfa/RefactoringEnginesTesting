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

package org.netbeans.test.refactoring.changesignature;

import java.lang.reflect.Modifier;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * Change method signature refactoring simple tests
 * @author  Emanuel Hucka
 */
public class SimpleTest extends ChangeSignatureTestCase {

    private static final String CLASS_BEAN = "org.netbeans.tests.examples.packa.Bean";
    private static final String CLASS_BEANC = "org.netbeans.tests.examples.packb.BeanC";
    private static final String CLASS_TEST = "org.netbeans.tests.examples.Test";
    private static final String CLASS_ABSTRACTBEAN = "org.netbeans.tests.examples.packc.AbstractBean";
    private static final String CLASS_AFRAME = "org.netbeans.tests.examples.packc.AFrame";
    private static final String CLASS_BEANSDBEANDA = "org.netbeans.tests.examples.packb.BeansD.BeanDA";
    private static final String CLASS_BEANA = "org.netbeans.tests.examples.packb.BeanA";
    private static final String CLASS_BEANB = "org.netbeans.tests.examples.packb.BeanB";
    
    private static final String IFACE_MAKABLE = "org.netbeans.tests.examples.packc.Makable";
    private static final String IFACE_TESTABLE = "org.netbeans.tests.examples.packa.Testable";
    
    
    /** Creates a new instance of Signature1Test */
    public SimpleTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        //undo
        suite.addTest(new SimpleTest("testUndoChangeParametersClassMethod1"));
        suite.addTest(new SimpleTest("testUndoChangeParametersClassMethod2"));
        suite.addTest(new SimpleTest("testUndoChangeParametersClassMethod3"));
        suite.addTest(new SimpleTest("testUndoChangeParametersClassMethod4"));
        suite.addTest(new SimpleTest("testUndoChangeParametersClassMethod5"));
        suite.addTest(new SimpleTest("testUndoChangeParametersClassMethod6"));
        suite.addTest(new SimpleTest("testUndoChangeParametersInterfaceMethod1"));
        suite.addTest(new SimpleTest("testUndoChangeParametersInterfaceMethod2"));
        
        suite.addTest(new SimpleTest("testClassMethod1"));
        suite.addTest(new SimpleTest("testClassMethod2"));
        suite.addTest(new SimpleTest("testClassMethod3"));
        suite.addTest(new SimpleTest("testClassMethod4"));
        suite.addTest(new SimpleTest("testInterfaceMethod1"));
        suite.addTest(new SimpleTest("testInterfaceMethod2"));
        suite.addTest(new SimpleTest("testInterfaceMethod3"));
        suite.addTest(new SimpleTest("testInterfaceMethod4"));        
        suite.addTest(new SimpleTest("testClassConstructor1"));
        suite.addTest(new SimpleTest("testClassConstructor2"));
        suite.addTest(new SimpleTest("testClassConstructor3"));
        return suite;
    }
    
    public void testUndoChangeParametersClassMethod1() {
        changeSignature(CLASS_BEAN, "abc", new String[0], new String[][] {{"ab", "java.lang.String", "\"\""}}, Modifier.PUBLIC, true);
    }
    
    public void testUndoChangeParametersClassMethod2() {
        log("static method");
        changeSignature(CLASS_TEST, "makeTest", new String[0],
        new String[][] {{"publish", "boolean", "false"}}, Modifier.PUBLIC, true);
    }
    
    public void testUndoChangeParametersClassMethod3() {//change accesor which cannot be changed
        log("bad accessor - method is called as public from another package");
        changeSignature(CLASS_TEST, "runTest", new String[0],
        new String[][] {}, Modifier.PROTECTED, true);
    }
    
    public void testUndoChangeParametersClassMethod4() {
        log("method of abstract class");
        changeSignature(CLASS_ABSTRACTBEAN, "methodBean", new String[0],
        new String[][] {{"names","java.lang.String[]","new String[] {}"}, {"data", "Object[][]", "new Object[0][0]"}}, Modifier.PUBLIC, true);
    }
    
    public void testUndoChangeParametersClassMethod5() {//error arguments
        log("same argument's name");
        changeSignature(CLASS_BEANC, "setSign", new String[] {"boolean"},
        new String[][] {{"sign", "int", "0"},{"sign", null, null}}, Modifier.PUBLIC, true);
        log("wrong argument's name - just exists in the method");
        changeSignature(CLASS_TEST, "test1", new String[0],
        new String[][] {{"a", "java.beans.PropertyChangeListener", "null"}}, Modifier.PUBLIC, true);
    }
    
    public void testUndoChangeParametersClassMethod6() {
        log("guarded method");
        changeSignature(CLASS_AFRAME, "buttonActionPerformed", new String[] {"java.awt.event.ActionEvent"},
        new String[][] {{"evt", null, null}, {"action", "int", "0"}}, Modifier.PROTECTED, true);
    }
    
    public void testUndoChangeParametersInterfaceMethod1() {
        changeSignature(IFACE_MAKABLE, "make", new String[] {"int","java.lang.String","org.netbeans.tests.examples.packa.Bean"},
        new String[][] {{"bean", null, null}, {"s", null, null}, {"check","boolean","false"}, {"date","java.util.Date","new Date()"}}, Modifier.PUBLIC, true);
    }
    
    public void testUndoChangeParametersInterfaceMethod2() {
        changeSignature(IFACE_TESTABLE, "test", new String[0],
        new String[][] {{"testName", "java.lang.String", "null"}, {"testFolder", "java.io.File", "new java.io.File(\"/tmp/test\")"}}, Modifier.PUBLIC, true);
    }
    
    
    
    public void testClassMethod1() {
        changeSignature(CLASS_BEAN, "abc", new String[0], new String[][] {{"ab", "java.lang.String", "\"\""}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassMethod2() {
        log("static method");
        changeSignature(CLASS_TEST, "makeTest", new String[0],
        new String[][] {{"publish", "boolean", "false"}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassMethod3() {
        log("method of abstract class");
        changeSignature(CLASS_ABSTRACTBEAN, "methodBean", new String[0],
        new String[][] {{"names","java.lang.String[]","new String[] {}"}, {"data", "Object[][]", "new Object[0][0]"}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassMethod4() {//error arguments
        ref("same argument's name");
        changeSignature(CLASS_BEANC, "setSign", new String[] {"boolean"},
        new String[][] {{"sign", "int", "0"},{"sign", null, null}}, Modifier.PUBLIC, false);
        ref("wrong argument's name - just exists in the method");
        changeSignature(CLASS_TEST, "test1", new String[0],
        new String[][] {{"a", "java.beans.PropertyChangeListener", "null"}}, Modifier.PUBLIC, false);        
        ref("guarded method");
        changeSignature(CLASS_AFRAME, "buttonActionPerformed", new String[] {"java.awt.event.ActionEvent"},
        new String[][] {{"evt", null, null}, {"action", "int", "0"}}, Modifier.PROTECTED, false);        
        ref("bad accessor - method is called as public from another package");
        changeSignature(CLASS_TEST, "runTest", new String[0],
        new String[][] {}, Modifier.PRIVATE, false);        
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testInterfaceMethod1() {
        log("add and change order of  parameters");
        changeSignature(IFACE_MAKABLE, "make", new String[] {"int","java.lang.String","org.netbeans.tests.examples.packa.Bean"},
        new String[][] {{"bean", null, null}, {"s", null, null}, {"index", null, null}, {"check","boolean","false"}, {"date","java.util.Date","new Date()"}}, Modifier.PUBLIC, false);
        //JavaMetamodel.getUndoManager().undo(); // added params used in next test methos
    }
    
    public void testInterfaceMethod2() {
        log("remove  parameters");
        changeSignature(IFACE_MAKABLE, "make", new String[] {"org.netbeans.tests.examples.packa.Bean","java.lang.String","int", "boolean", "java.util.Date"},
        new String[][] {{"bean", null, null}, {"s", null, null}, {"check",null,null}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testInterfaceMethod3() {
        changeSignature(IFACE_TESTABLE, "test", new String[0],
        new String[][] {{"testName", "java.lang.String", "null"}, {"testFolder", "java.io.File", "new java.io.File(\"/tmp/test\")"}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testInterfaceMethod4() {
        log("wrong interface accessor");
        changeSignature(IFACE_MAKABLE, "prepare", new String[0],
        new String[][] {}, Modifier.PROTECTED, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassConstructor1() {
        changeSignature(CLASS_BEANA, null, new String[0],
        new String[][] {{"name", "java.lang.String", "\"\""}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testClassConstructor2() {
        changeSignature(CLASS_BEANB, null, new String[0],
        new String[][] {{"name", "java.lang.String", "\"\""},{"check","boolean","false"}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testClassConstructor3() {
        changeSignature(CLASS_BEAN, null, new String[0],
        new String[][] {{"name", "java.lang.String", "\"\""},{"check","boolean","false"}}, Modifier.PUBLIC, false);
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
