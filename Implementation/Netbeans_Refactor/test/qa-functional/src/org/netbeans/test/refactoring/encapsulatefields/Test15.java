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

import java.io.File;
import java.lang.reflect.Modifier;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * Encapsulate fields refactoring tests for JDK 1.5 features
 * @author  Emanuel Hucka
 */
public class Test15 extends EncapsulateTestCase {

    private static final String CLASS_NAME = "abc.A";
    private static final String CLASS_NAME2 = "abc.def.D";
    
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
        suite.addTest(new Test15("testClassField1"));
        suite.addTest(new Test15("testClassField2"));
        suite.addTest(new Test15("testClassField3"));
        suite.addTest(new Test15("testClassField4"));
        return suite;
    }
    
    public void testClassField1() {
        encapsulateField(CLASS_NAME, "list", null, null, Modifier.PUBLIC, Modifier.PRIVATE, false, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testClassField2() {
        encapsulateField(CLASS_NAME2, "en", null, null, Modifier.PUBLIC, Modifier.PROTECTED, false, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testClassField3() {
        encapsulateField(CLASS_NAME2, "a", null, null, Modifier.PUBLIC, Modifier.PROTECTED, false, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassField4() {
        encapsulateField(CLASS_NAME, "data", null, null, Modifier.PUBLIC, Modifier.PRIVATE, false, false);
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
