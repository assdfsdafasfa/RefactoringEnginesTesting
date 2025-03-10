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
import java.util.Collections;
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
public class RenameTest extends NbTestCase {
        
    private static final String CLASS_NAME = "org.netbeans.test.rename.MainClass";
    private static final String CLASS_NAME_2 = "org.netbeans.test.rename.over_methods.Interface";
    
    private static String[] resultFiles = {
        "RenamedClass",
        "SecondClass"        
    };
    
    private static String[] resultFiles_2 = {
        "BaseClass",
        "Extension_1",
        "Extension_2",
        "Interface",
        "Interface_2",
        "SubClass_1",
        "SubClass_2"
    };
    
    private String JAVA_EXT = "java";
    private String PASS_EXT = "pass";
    private String PATH_PREFIX = "org/netbeans/test/rename/";
    
    private static TypeClass typeProxy;
    private static JavaClass jc;

    
    /** Creates a new instance of Signature1Test */
    public RenameTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new RenameTest("testRename"));
        suite.addTest(new RenameTest("testRenameExtensibleVisitor"));
        suite.addTest(new RenameTest("testOverridingMethods"));

        jc = (JavaClass) Utility.findClass(CLASS_NAME);
        typeProxy = ((JavaModelPackage) jc.refOutermostPackage()).getType();

        return suite;
    }
    
    public void testRename() throws FileStateInvalidException, IOException {
        Field field = jc.getField("number", false);
        RenameRefactoring refactoring = new RenameRefactoring(field);
        refactoring.setNewName("renamedField");
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("rename field");
        refactoring.prepare(result);
        result.doRefactoring(true);

        List argTypes = new ArrayList(2);
        argTypes.add(typeProxy.resolve("char"));
        argTypes.add(typeProxy.resolve("int"));
        jc = (JavaClass) typeProxy.resolve(CLASS_NAME);
        Method method = jc.getMethod("getValue", argTypes, false);
        refactoring = new RenameRefactoring(method);
        refactoring.setNewName("renamedMethod");
        refactoring.checkParameters();
        result = RefactoringSession.create("rename method");
        refactoring.prepare(result);
        result.doRefactoring(true);

        jc = (JavaClass) typeProxy.resolve(CLASS_NAME);
        refactoring = new RenameRefactoring(jc);
        refactoring.setNewName("RenamedClass");
        refactoring.checkParameters();
        result = RefactoringSession.create("rename class");
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
    
    
    public void testRenameExtensibleVisitor() throws FileStateInvalidException, IOException {
        jc = (JavaClass) Utility.findClass("extensiblevisitor.Visitor");
        assertNotNull("Visitor found", jc);
    
        JavaClass exp = (JavaClass) Utility.findClass("extensiblevisitor.Exp");
        assertNotNull("Exp", exp);
        JavaClass add = exp.getInnerClass("Add", false);
        assertNotNull("add class found", add);
        
        Method method = jc.getMethod("visitAdd", Collections.nCopies(1, add), false);
        assertNotNull("method found", method);
        
        RenameRefactoring refactoring = new RenameRefactoring(method);
        refactoring.setNewName("dispatchAdd");
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("rename field");
        refactoring.prepare(result);
        result.doRefactoring(true);

        assertFile(Utility.getFile(getDataDir(), "extensiblevisitor/Visitor.java"), getGoldenFile("Visitor.pass"), getWorkDir());
    }
    
    public void testOverridingMethods() throws FileStateInvalidException, IOException {
        jc = (JavaClass) typeProxy.resolve(CLASS_NAME_2);
        List argTypes = new ArrayList(2);
        Type intType = typeProxy.resolve("int");
        argTypes.add(intType);
        argTypes.add(intType);
        Method method = jc.getMethod("generateNumber", argTypes, false);
        RenameRefactoring refactoring = new RenameRefactoring(method);
        refactoring.setNewName("calculateValue");
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("rename method");
        refactoring.prepare(result);
        result.doRefactoring(true);

        // check modified files
        try {
            for (int x = 0; x < resultFiles_2.length; x++) {
                String fileName = PATH_PREFIX + "over_methods/" + resultFiles_2[x] + '.' + JAVA_EXT;
                String passName = resultFiles_2[x] + '.' + PASS_EXT;
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
