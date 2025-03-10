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
package org.netbeans.test.refactoring.innertoouter;

import java.io.IOException;
import org.netbeans.modules.refactoring.api.InnerToOuterRefactoring;
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
public class InnerToOuterTest extends NbTestCase {

    private JavaClass clazz;
    JavaModelPackage pkg;
    public InnerToOuterTest() {
        super("InnerToOuterTest");
    }
    
    protected void setUp() {
        clazz = (JavaClass) Utility.findClass("org.netbeans.test.innertoouter.NewClass.Inner");
        pkg = (JavaModelPackage) clazz.refOutermostPackage();
    }
    
    // this method has "test" prefix - it describes one test case
    public void testExtractInterface() throws FileStateInvalidException, IOException {
        // initialize refactoring and set parameters
        InnerToOuterRefactoring refactoring = new InnerToOuterRefactoring(clazz);
        refactoring.setClassName("Inner");
        refactoring.setReferenceName("foo");

        // we don't expect any problems from checkParameters
        assertNull(refactoring.checkParameters());
        
        RefactoringSession result = RefactoringSession.create("Inner to outer");
        // we don't expect any problems from prepare
        assertNull(refactoring.prepare(result));
        
        // we don't expect any problems during refactoring
        assertNull(result.doRefactoring(true));

        // let's check if all files were correctly refactored
        assertFiles();
    }
    
    // put your test sources into test project: refactoring/test/unit/data/projects/default/src/..
    // test project including your files will be available for refactoring during test execution
    private static final String FILE1 = "org/netbeans/test/innertoouter/NewClass.java";
    private static final String FILE2 = "org/netbeans/test/innertoouter/Inner.java";
    
    public void assertFiles() throws FileStateInvalidException, IOException {
        assertFile(Utility.getFile(getDataDir(), FILE1), getGoldenFile("NewClass.pass"), getWorkDir());    
        assertFile(Utility.getFile(getDataDir(), FILE2), getGoldenFile("Inner.pass"), getWorkDir());    
    }
    
}
