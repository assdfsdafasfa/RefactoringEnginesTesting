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
import org.netbeans.jmi.javamodel.Attribute;
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
 * Tries to rename annotation attribute <code>attrToBeRenamed</code> and
 * its usages.
 *
 * @author Pavel Flaska
 */
public class RenameAnnAttributeTest extends NbTestCase {
    
    AnnotationType annType;
    JavaModelPackage pkg;
    
    /** Creates a new instance of RenameAnnAttributeTest */
    public RenameAnnAttributeTest() {
        super("RenameAnnAttributeTest");
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite(RenameAnnAttributeTest.class);
        return suite;
    }
    
    protected void setUp() {
        annType = (AnnotationType) JavaMetamodel.getManager().getDefaultExtent().getType().resolve("org.netbeans.test.rename.anntype.AnnTypeAttrToRename");
        pkg = (JavaModelPackage) annType.refOutermostPackage();
    }
    
    public void testRenameAnnAttribute() {
        String[] resultFiles = {
            "AnnTypeAttrToRename",
            "AnnAttrUsages1",
            "AnnAttrUsages2",
            "AnnAttrUsages3",
            "AnnAttrUsages4"
        };
        Attribute attr = (Attribute) annType.getContents().get(0);
        RenameRefactoring refactoring = new RenameRefactoring(attr);
        refactoring.setNewName("attrNewName");
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("rename annotation attribute");
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
