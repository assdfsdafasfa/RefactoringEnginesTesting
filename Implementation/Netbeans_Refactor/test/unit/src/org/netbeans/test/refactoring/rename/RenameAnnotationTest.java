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

import java.io.IOException;
import org.netbeans.jmi.javamodel.AnnotationType;
import org.netbeans.jmi.javamodel.JavaModelPackage;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.spi.RefactoringElementsBag;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.test.refactoring.Utility;
import org.openide.filesystems.FileStateInvalidException;

/**
 * Test simple rename annotation type refactoring. Finds annotation and its
 * usages and renames it.
 *
 * @author Pavel Flaska
 */
public class RenameAnnotationTest extends NbTestCase {
    
    AnnotationType annType;
    JavaModelPackage pkg;
    
    /** Creates a new instance of RenameAnnotationTest */
    public RenameAnnotationTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite(RenameAnnotationTest.class);
        return suite;
    }
    
    protected void setUp() {
        // annType = (AnnotationType) Utility.findClass("org.netbeans.test.rename.anntype.AnnotationTypeToBeRenamed");
        annType = (AnnotationType) JavaMetamodel.getManager().getDefaultExtent().getType().resolve("org.netbeans.test.rename.anntype.AnnTypeToBeRenamed");
        pkg = (JavaModelPackage) annType.refOutermostPackage();
    }
    
    public void testFindUsagesOfAnnType() {
        String[] resultFiles = {
            "NewAnnTypeName",
            "AnnTypeUsages1",
            "AnnTypeUsages2"
        };
        RenameRefactoring refactoring = new RenameRefactoring(annType);
        refactoring.setNewName("NewAnnTypeName");
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("rename annotation type");
        refactoring.prepare(result);
        getLog().println("Elements: " + result.getRefactoringElements().size());
        assertNull(result.doRefactoring(true));
        
        try {
            for (int i = 0; i < resultFiles.length; i++) {
                String fileName = "org/netbeans/test/rename/anntype/" + resultFiles[i] + ".java";
                String passName = resultFiles[i] + ".pass";
                assertFile(Utility.getFile(getDataDir(), fileName), getGoldenFile(passName), getWorkDir());
            }
        } catch (FileStateInvalidException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
 }
