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

package org.netbeans.test.refactoring.encapsulatefields;

import java.lang.reflect.Modifier;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * Encapsulate fields refactoring simple tests
 * @author  Emanuel Hucka
 */
public class SimpleTest extends EncapsulateTestCase {
    
    private static final String CLASS_BEAN = "org.netbeans.tests.examples.packa.Bean";
    private static final String CLASS_BEANSD = "org.netbeans.tests.examples.packb.BeansD";
    private static final String CLASS_BEANA = "org.netbeans.tests.examples.packb.BeanA";
    private static final String CLASS_ABSTRACTBEAN = "org.netbeans.tests.examples.packc.AbstractBean";
    private static final String CLASS_AFRAME = "org.netbeans.tests.examples.packc.AFrame";
    
    private static final String IFACE_MAKABLE = "org.netbeans.tests.examples.packc.Makable";
    
    /** Creates a new instance of Signature1Test */
    public SimpleTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        //undo
        suite.addTest(new SimpleTest("testUndoEncapsulateClassField1"));
        suite.addTest(new SimpleTest("testUndoEncapsulateClassField2"));
        suite.addTest(new SimpleTest("testUndoEncapsulateClassField3"));
        suite.addTest(new SimpleTest("testUndoEncapsulateClassField4"));
        suite.addTest(new SimpleTest("testUndoEncapsulateClassField5"));
        suite.addTest(new SimpleTest("testUndoEncapsulateInterfaceField1"));
        
        suite.addTest(new SimpleTest("testClassField1"));
        suite.addTest(new SimpleTest("testClassField2"));
        suite.addTest(new SimpleTest("testClassField3"));
        suite.addTest(new SimpleTest("testClassField4"));
        suite.addTest(new SimpleTest("testClassField5"));
        suite.addTest(new SimpleTest("testClassField6"));
        suite.addTest(new SimpleTest("testClassField7"));
        suite.addTest(new SimpleTest("testInterfaceField1"));
        return suite;
    }
    
    public void testUndoEncapsulateClassField1() {
        encapsulateField(CLASS_BEAN, "refID", "setRefId", null, Modifier.PUBLIC, Modifier.PRIVATE, true, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoEncapsulateClassField2() {
        encapsulateField(CLASS_BEANSD, "bb", "setBeanA", "getBeanA", Modifier.PUBLIC, Modifier.PROTECTED, false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoEncapsulateClassField3() {
        log("field of abstract class");
        encapsulateField(CLASS_ABSTRACTBEAN, "rootField", null, null, Modifier.PUBLIC, Modifier.PROTECTED, false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoEncapsulateClassField4() {
        log("field of a form");
        encapsulateField(CLASS_AFRAME, "counter", null, null, Modifier.PUBLIC, Modifier.PRIVATE, false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoEncapsulateClassField5() {
        log("guarded field");
        encapsulateField(CLASS_AFRAME, "button", null, null, Modifier.PUBLIC, Modifier.PROTECTED, true, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testUndoEncapsulateInterfaceField1() {
        log("interface field");
        encapsulateField(IFACE_MAKABLE, "CODE", "setCode", "getCode", Modifier.PUBLIC, Modifier.PRIVATE, false, true);
        JavaMetamodel.getUndoManager().undo();
    }
    
    
    
    public void testClassField1() {
        encapsulateField(CLASS_BEAN, "refID", "setRID", "getRID", Modifier.PUBLIC, Modifier.PRIVATE, true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassField2() {
        encapsulateField(CLASS_BEANSD, "bb", "setBeanA", "getBeanA", Modifier.PUBLIC, Modifier.PRIVATE, false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassField3() {
        log("existing accessors");
        encapsulateField(CLASS_BEANA, "protectedProperty", null, null, Modifier.PUBLIC, Modifier.PROTECTED, true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassField4() {
        log("same access as new accessors");
        encapsulateField(CLASS_BEANA, "publicProperty", null, null, Modifier.PUBLIC, Modifier.PUBLIC, false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassField5() {
        log("field of abstract class");
        encapsulateField(CLASS_ABSTRACTBEAN, "rootField", null, null, Modifier.PUBLIC, Modifier.PRIVATE, false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassField6() {
        log("field of a form");
        encapsulateField(CLASS_AFRAME, "counter", null, null, Modifier.PUBLIC, Modifier.PRIVATE, false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassField7() {
        log("guarded field");
        encapsulateField(CLASS_AFRAME, "button", null, null, Modifier.PUBLIC, Modifier.PRIVATE, true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testInterfaceField1() {
        log("interface field");
        encapsulateField(IFACE_MAKABLE, "CODE", "setCode", "getCODE", Modifier.PUBLIC, Modifier.PRIVATE, false, false);
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
