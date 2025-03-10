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

import java.io.File;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;




/**
 * MOVE refactoring tests for JDK 1.5 features
 * @author  Emanuel Hucka
 */
public class Test15 extends MoveTestCase {

    private static final String CLASS_NAME = "abc.A";
    private static final String ENUMERATION_NAME = "abc.En";
    private static final String ANNOTATION_TYPE_NAME = "abc.def.DAnnotType";
    
    private static final String PACKAGE_NAME = "abc.def";
    
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
        suite.addTest(new Test15("testClass"));
        suite.addTest(new Test15("testEnumeration"));
        suite.addTest(new Test15("testAnnotationType"));
        return suite;
    }
    
    public void testClass() {
        moveClass(CLASS_NAME, PACKAGE_NAME, false);
        JavaMetamodel.getUndoManager().undo();
    }
    
    public void testEnumeration() {
        moveClass(ENUMERATION_NAME, PACKAGE_NAME, false);
        JavaMetamodel.getUndoManager().undo();
    }

    public void testAnnotationType() {
        moveClass(ANNOTATION_TYPE_NAME, "abc", false);
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
