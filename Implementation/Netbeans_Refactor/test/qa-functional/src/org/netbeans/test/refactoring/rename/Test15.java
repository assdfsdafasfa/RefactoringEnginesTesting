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

import java.io.File;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * RENAME refactoring tests fro JDK 1.5 features
 * @author  Emanuel Hucka
 */
public class Test15 extends RenameTestCase {

    private static final String CLASS_NAME = "abc.A";
    private static final String CLASS_NAME2 = "abc.def.D";
    private static final String CLASS_NAME3 = "abc.def.ghi.H";
    private static final String ENUMERATION_NAME = "abc.En";
    private static final String ANNOTATION_TYPE_NAME = "abc.def.DAnnotType";
    
    /** Creates a new instance of Signature1Test */
    public Test15(String name) {
        super(name);
    }
    
    public void prepareProject() {
        ProjectSupport.openProject(new File(getDataDir(), "projects/default2"));
        classPathWorkDir=new File(getDataDir(), "projects.default2.src".replace('.', File.separatorChar));
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new Test15("testClass1"));
        suite.addTest(new Test15("testClass2"));
        suite.addTest(new Test15("testClass3"));
        suite.addTest(new Test15("testEnumeration"));
        suite.addTest(new Test15("testAnnotationType"));
        suite.addTest(new Test15("testField1"));
        suite.addTest(new Test15("testField2"));
        suite.addTest(new Test15("testField3"));
        suite.addTest(new Test15("testField4"));
        suite.addTest(new Test15("testField5"));
        suite.addTest(new Test15("testField6"));
        suite.addTest(new Test15("testMethod1"));
        suite.addTest(new Test15("testMethod2"));
        return suite;
    }
    
    public void testClass1() {
        renameClass(CLASS_NAME, "TestClassName", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass2() {
        renameClass(CLASS_NAME2, "TestClassDName", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClass3() {
        renameClass(CLASS_NAME3, "TestClassHName", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testEnumeration() {
        renameClass(ENUMERATION_NAME, "TestEnumeration", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testAnnotationType() {
        renameClass(ANNOTATION_TYPE_NAME, "TestAT", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField1() {
        renameField(CLASS_NAME, "list", "testFieldName", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField2() {
        renameField(CLASS_NAME, "data", "testFieldListName", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField3() {
        renameField(CLASS_NAME3, "DATA1", "DDDATA1", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testField4() {
        renameField(ENUMERATION_NAME, "X", "XX", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    //rename of annotation type member is not implemented yet
    public void testField5() {
        renameAnnotationMember(ANNOTATION_TYPE_NAME, "id", "idren", true, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testField6() {
        renameAnnotationMember(ANNOTATION_TYPE_NAME, "coord", "dimension", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testMethod1() {
        renameMethod(CLASS_NAME2, "methodD", new String[] {ENUMERATION_NAME}, "methodDEn", true, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testMethod2() {
        renameMethod(CLASS_NAME, "methodA", new String[] {"T", "java.lang.String"}, "methodDEn", true, false);
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
