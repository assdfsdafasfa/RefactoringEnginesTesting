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

import java.io.File;
import java.lang.reflect.Modifier;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * Change method signature refactoring tests for JDK 1.5 features
 * @author  Emanuel Hucka
 */
public class Test15 extends ChangeSignatureTestCase {

    private static final String CLASS_A = "abc.A";
    private static final String CLASS_D = "abc.def.D";
    
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
        suite.addTest(new Test15("testClassMethod1"));
        //suite.addTest(new Test15("testClassMethod2"));
        suite.addTest(new Test15("testClassMethod3"));
        return suite;
    }
    
    public void testClassMethod1() {
        changeSignature(CLASS_A, "methodA", new String[] {"T", "java.lang.String"},
                new String[][] {{"i", null, null}, {"abc", "java.lang.String", "\"\""}, {"text", null, null}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    //not implemented in promoE
    public void testClassMethod2() {
        changeSignature(CLASS_A, "methodA", new String[] {"T", "java.lang.String", "boolean", "java.lang.String..."},
                new String[][] {{"ab", "java.lang.String", "\"\""}}, Modifier.PUBLIC, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testClassMethod3() {
        changeSignature(CLASS_D, "methodD", new String[] {"abc.En"},
                new String[][] {{"abc", "java.lang.String", "\"\""}}, Modifier.PUBLIC, false);
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
