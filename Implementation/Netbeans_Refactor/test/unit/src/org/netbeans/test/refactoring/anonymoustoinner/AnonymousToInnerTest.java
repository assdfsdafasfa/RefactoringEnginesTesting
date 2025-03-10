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
package org.netbeans.test.refactoring.anonymoustoinner;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import org.netbeans.jmi.javamodel.ClassDefinition;
import org.netbeans.jmi.javamodel.Element;
import org.netbeans.modules.refactoring.api.AnonymousToInnerRefactoring;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.JavaModelPackage;
import org.netbeans.junit.NbTestCase;
import org.netbeans.test.refactoring.Utility;
import org.openide.filesystems.FileStateInvalidException;

/**
 *
 * @author Jan Becicka
 */
public class AnonymousToInnerTest extends NbTestCase {
    
    private JavaClass clazz;
    JavaModelPackage pkg;
    public AnonymousToInnerTest() {
        super("AnonymousToInnerTest");
    }
    
    protected void setUp() {
        clazz = (JavaClass) Utility.findClass("org.netbeans.test.anonymoustoinner.NewClass");
        pkg = (JavaModelPackage) clazz.refOutermostPackage();
    }
    
    public void testExtractInterface() throws FileStateInvalidException, IOException {
        // initialize refactoring and set parameters
        Element e = clazz;
        List children = clazz.getChildren();
        Object o = null;
        while (!children.isEmpty()) {
            o = children.get(0);
            if (o instanceof ClassDefinition) {
                break;
            }
            children.remove(0);
            children.addAll(((Element) o).getChildren());
        }
        assertTrue(o instanceof ClassDefinition);
        
        AnonymousToInnerRefactoring refactoring = new AnonymousToInnerRefactoring((ClassDefinition) o);
        refactoring.setModifiers(Modifier.PUBLIC);
        refactoring.setName("MyRunnable");
        AnonymousToInnerRefactoring.ParameterInfo[] pinfo = new AnonymousToInnerRefactoring.ParameterInfo[0];
        
        refactoring.setParamTable(pinfo);

        // we don't expect any problems from checkParameters
        assertNull(refactoring.checkParameters());
        
        RefactoringSession result = RefactoringSession.create("AnonymousToInner");
        // we don't expect any problems from prepare
        assertNull(refactoring.prepare(result));
        
        // we don't expect any problems during refactoring
        assertNull(result.doRefactoring(true));

        // let's check if all files were correctly refactored
        assertFiles();
    }
    
    // put your test sources into test project: refactoring/test/unit/data/projects/default/src/..
    // test project including your files will be available for refactoring during test execution
    private static final String FILE1 = "org/netbeans/test/anonymoustoinner/NewClass.java";
    
    public void assertFiles() throws FileStateInvalidException, IOException {
        assertFile(Utility.getFile(getDataDir(), FILE1), getGoldenFile("NewClass.pass"), getWorkDir());    
    }
    
}
