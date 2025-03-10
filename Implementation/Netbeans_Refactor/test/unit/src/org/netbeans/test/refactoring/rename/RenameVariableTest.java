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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.text.StyledDocument;
import junit.textui.TestRunner;
import org.netbeans.modules.refactoring.spi.RefactoringElementsBag;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.jmi.javamodel.*;
import org.netbeans.junit.AssertionFailedErrorException;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.test.refactoring.Utility;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;

 
/**
 *
 * @author  Daniel Prusa
 */
public class RenameVariableTest extends NbTestCase {
        
    private static final String CLASS_NAME = "org.netbeans.test.rename.ThirdClass";
    
    private static String[] resultFiles = {
        "ThirdClass"        
    };
       
    private String JAVA_EXT = "java";
    private String PASS_EXT = "pass";
    private String PATH_PREFIX = "org/netbeans/test/rename/";
    
    private static TypeClass typeProxy;
    private static JavaClass jc;

    
    /** Creates a new instance of Signature1Test */
    public RenameVariableTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new RenameVariableTest("testRename"));

        jc = (JavaClass) Utility.findClass(CLASS_NAME);
        typeProxy = ((JavaModelPackage) jc.refOutermostPackage()).getType();

        return suite;
    }
    
    public void testRename() throws FileStateInvalidException, IOException {
        Method method = (Method)jc.getFeatures().get(1);
        Parameter par=(Parameter)method.getParameters().get(0);
        StatementBlock block=method.getBody();
        List statements=block.getStatements();
        LocalVarDeclaration lvdG=(LocalVarDeclaration)statements.get(0);
        LocalVariable locVarG=(LocalVariable)lvdG.getVariables().get(0);
        LocalVarDeclaration lvdH=(LocalVarDeclaration)statements.get(1);
        LocalVariable locVarH=(LocalVariable)lvdH.getVariables().get(0);
        RenameRefactoring refactoring = new RenameRefactoring(par);
        refactoring.setNewName("f");
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("rename field");
        refactoring.prepare(result);
        result.doRefactoring(true);
        
        refactoring = new RenameRefactoring(locVarG);
        refactoring.setNewName("aa");
        refactoring.checkParameters();
        result = RefactoringSession.create("rename field");
        refactoring.prepare(result);
        result.doRefactoring(true);

        refactoring = new RenameRefactoring(locVarH);
        refactoring.setNewName("bb");
        refactoring.checkParameters();
        result = RefactoringSession.create("rename field");
        refactoring.prepare(result);
        result.doRefactoring(true);

        // check modified files
        try {
            for (int x = 0; x < resultFiles.length; x++) {
                String fileName = PATH_PREFIX + resultFiles[x] + '.' + JAVA_EXT;
                String passName = resultFiles[x] + '.' + PASS_EXT;
                assertFile(Utility.getFile(getDataDir(), fileName), getGoldenFile(passName), getWorkDir());
            }
        } catch (FileStateInvalidException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
        
    public static String getAsString(String file) {
        String result;
        try {
            FileObject testFile = Repository.getDefault().findResource(file);
            DataObject dob = DataObject.find(testFile);
            
            EditorCookie ec = (EditorCookie) dob.getCookie(EditorCookie.class);
            StyledDocument doc = ec.openDocument();
            result = doc.getText(0, doc.getLength());
        } 
        catch (Exception e) {
            throw new AssertionFailedErrorException(e);
        }
        return result;
    }
    
}
