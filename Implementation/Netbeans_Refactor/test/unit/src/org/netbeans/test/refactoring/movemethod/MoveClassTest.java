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
package org.netbeans.test.refactoring.moveclass;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import junit.textui.TestRunner;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.modules.javacore.api.JavaModel;
import org.netbeans.modules.refactoring.api.MoveClassRefactoring;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.jmi.javamodel.*;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.test.refactoring.Utility;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;

/**
 * @author  Daniel Prusa
 */
public class MoveClassTest extends NbTestCase {
    
    private static String[] resultFiles = {
        "abc/test_folder/BaseClass",
        "abc/test_folder/ClassOne",
        "abc/test_folder/ExtendedMoveMe",
        "abc/AClass",
        "abc/BClass",
        "abc/CClass",
        "abc/MainClass",
        "folder/sub_folder/sub_folder/MoveMe"
    };
    
    private String JAVA_EXT = "java";
    private String PASS_EXT = "pass";
    private String PATH_PREFIX = "org/netbeans/test/moveclass/";
    
    /** Need to be defined because of JUnit */
    public MoveClassTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new MoveClassTest("testMoveClass"));
        return suite;
    }
    
    FileObject targetCPRoot;
    String targetName;
    Resource resource;
    
    protected void setUp() throws MalformedURLException, FileStateInvalidException {
        JavaClass jc = Utility.findClass("org.netbeans.test.moveclass.abc.test_folder.MoveMe");
        resource = (Resource) jc.refImmediateComposite();
        FileObject fo = JavaModel.getFileObject(resource);
        targetCPRoot = ClassPath.getClassPath(fo, ClassPath.SOURCE).findOwnerRoot(fo);
        targetName = "org.netbeans.test.moveclass.folder.sub_folder.sub_folder";
    }
    
    public void testMoveClass() {        
        Collection list = new ArrayList(1);
        list.add(resource);
        MoveClassRefactoring refactoring = new MoveClassRefactoring(list);
        refactoring.preCheck();
        refactoring.setTargetClassPathRoot(targetCPRoot);
        refactoring.setTargetPackageName(targetName);
        refactoring.checkParameters();
        RefactoringSession result = RefactoringSession.create("move class");
        refactoring.prepare(result);
        assertNull(result.doRefactoring(true));
        try {
            for (int x = 0; x < resultFiles.length; x++) {
                String fileName = PATH_PREFIX + resultFiles[x] + '.' + JAVA_EXT;
                int index = resultFiles[x].lastIndexOf('/') + 1;
                String passName = resultFiles[x].substring(index) + '.' + PASS_EXT;
                assertFile(Utility.getFile(getDataDir(), fileName), getGoldenFile(passName), getWorkDir());
            }
        } catch (FileStateInvalidException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
