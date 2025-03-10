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

import java.io.File;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;



/**
 * Find Usages tests for JDK 1.5 features
 * @author  Emanuel Hucka
 */
public class Test15 extends WhereUsedTestCase {

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
        suite.addTest(new Test15("testClass"));
        suite.addTest(new Test15("testClassAllSubTypes"));
        suite.addTest(new Test15("testEnumeration"));
        suite.addTest(new Test15("testAnnotationType"));
        suite.addTest(new Test15("testField1"));
        suite.addTest(new Test15("testField2"));
        suite.addTest(new Test15("testField3"));
        suite.addTest(new Test15("testField4"));
        suite.addTest(new Test15("testMethod1"));
        return suite;
    }
    
    public void testClass() {
        findClass(CLASS_NAME);
        findClass("java.lang.String");
        findClass(CLASS_NAME2);
        findClass(CLASS_NAME3);
    }
    
    public void testClassAllSubTypes() {
        findClassParams(CLASS_NAME, WhereUsedTestCase.CLASS_SUBTYPES);
    }
    
    public void testEnumeration() {
        findClass(ENUMERATION_NAME);
    }
    
    public void testAnnotationType() {
        findClass(ANNOTATION_TYPE_NAME);
    }
    
    public void testField1() {
        findField(CLASS_NAME, "list");
    }
    
    public void testField2() {
        findField(ENUMERATION_NAME, "X");
    }
    
    public void testField3() {
        findField(CLASS_NAME3, "DATA1");
    }
    
    //find usages of annotation type's member is not implemented yet
    public void testField4() {
        findAnnotationTypeMember(ANNOTATION_TYPE_NAME, "id");
        findAnnotationTypeMember(ANNOTATION_TYPE_NAME, "value");
    }
    
    public void testMethod1() {
        findMethod(CLASS_NAME, "methodA", new String[] {"T", "java.lang.String"});
        findMethod(CLASS_NAME2, "methodD", new String[] {ENUMERATION_NAME});
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
