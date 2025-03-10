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

import java.io.FileNotFoundException;
import java.io.IOException;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.JavaModelPackage;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.test.refactoring.Utility;

/**
 * Test simple rename type parameter.
 *
 * @author Pavel Flaska
 */
public class RenameTypeParameterTest extends NbTestCase {
    
    JavaClass typeparClass;
    JavaModelPackage pkg;
    
    /** Creates a new instance of RenameAnnotationTest */
    public RenameTypeParameterTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite(RenameTypeParameterTest.class);
        return suite;
    }
    
    protected void setUp() {
        typeparClass = (JavaClass) JavaMetamodel.getManager().getDefaultExtent().getType().resolve("org.netbeans.test.rename.typepar.TypeParameterClass");
        pkg = (JavaModelPackage) typeparClass.refOutermostPackage();
    }
    
    public void testRenameTypeParameter() throws FileNotFoundException, IOException {
        RenameRefactoring refactoring = new RenameRefactoring(typeparClass.getTypeParameters().get(0));
        refactoring.setNewName("Typed");
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("rename type parameter");
        refactoring.prepare(result);
        getLog().println("Elements: " + result.getRefactoringElements().size());
        assertNull(result.doRefactoring(true));
        
        String fileName = "org/netbeans/test/rename/typepar/TypeParameterClass.java";
        String passName = "TypeParameterClass.pass";
        assertFile(Utility.getFile(getDataDir(), fileName), getGoldenFile(passName), getWorkDir());
    }
 }
