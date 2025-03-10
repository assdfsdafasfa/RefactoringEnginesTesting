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
package org.netbeans.test.refactoring.extractvariable;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.jmi.javamodel.Resource;
import org.netbeans.jmi.javamodel.Statement;
import org.netbeans.jmi.javamodel.StatementBlock;
import org.netbeans.jmi.javamodel.WhileStatement;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.api.JavaModel;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.ExtractMethodRefactoring;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.Utility;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;

/**
 *
 * @author Pavel Flaska
 */
public class ExtractVariable1Test extends NbTestCase {
    
    public ExtractVariable1Test(String name) {
        super(name);
    }

    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new ExtractVariable1Test("testExtractVariable1"));
        suite.addTest(new ExtractVariable1Test("testExtractVariable2"));
        suite.addTest(new ExtractVariable1Test("testExtractVariable3"));
        suite.addTest(new ExtractVariable1Test("testExtractVariable4"));
        suite.addTest(new ExtractVariable1Test("testExtractVariable5"));
        return suite;
    }
    
    FileObject targetCPRoot;
    Resource resource;
    JavaClass clazz;
    
    protected void setUp() throws MalformedURLException, FileStateInvalidException {
        clazz = Utility.findClass("org.netbeans.test.extractvariable.TestClass");
        resource = (Resource) clazz.refImmediateComposite();
        FileObject fo = JavaModel.getFileObject(resource);
        targetCPRoot = ClassPath.getClassPath(fo, ClassPath.SOURCE).findOwnerRoot(fo);
    }

    public void testExtractVariable1() throws FileStateInvalidException, IOException {
        Utility.beginTrans(false);
        try {
            Method m = (Method) clazz.getContents().get(2);
            Statement st1 = (Statement) m.getBody().getStatements().get(0);
            Statement st2 = (Statement) m.getBody().getStatements().get(1);
            int start = JavaMetamodel.getManager().getElementPosition(st1, true).getBegin().getOffset();
            int end = JavaMetamodel.getManager().getElementPosition(st2, true).getEnd().getOffset();
            ExtractVariableRefactoring emr = new ExtractVariableRefactoring(resource, start, end);
            assertNull(emr.preCheck());
            emr.setName("extracted1");
            assertNull(emr.checkParameters());
            RefactoringSession result = RefactoringSession.create("extract variable, #1");
            assertNull(emr.prepare(result));
        assertNull(result.doRefactoring(true));
        } finally {
            Utility.endTrans();
        }
        assertFile("File is not correctly generated.",
            Utility.getFile(getDataDir(), "org/netbeans/test/extractvariable/TestClass.java"),
            getGoldenFile("testExtractVariable1.pass"),
            getWorkDir()
        );
    }
    
    public void testExtractVariable2() throws FileStateInvalidException, IOException {
        Utility.beginTrans(false);
        try {
            Method m = (Method) clazz.getContents().get(4);
            WhileStatement wajl = (WhileStatement) m.getBody().getStatements().get(1);
            Statement systemErr = (Statement) ((StatementBlock) wajl.getBody()).getStatements().get(1);
            int start = JavaMetamodel.getManager().getElementPosition(systemErr, true).getBegin().getOffset();
            int end = JavaMetamodel.getManager().getElementPosition(systemErr, true).getEnd().getOffset();
            ExtractVariableRefactoring emr = new ExtractVariableRefactoring(resource, start, end);
            assertNull(emr.preCheck());
            emr.setModifier(Modifier.PUBLIC);
            emr.setName("extracted2");
            assertNull(emr.checkParameters());
            RefactoringSession result = RefactoringSession.create("extract variable, #2");
            assertNull(emr.prepare(result));
        assertNull(result.doRefactoring(true));
        } finally {
            Utility.endTrans();
        }
        assertFile("File is not correctly generated.",
            Utility.getFile(getDataDir(), "org/netbeans/test/extractvariable/TestClass.java"),
            getGoldenFile("testExtractVariable2.pass"),
            getWorkDir()
        );
    }
    
    public void testExtractVariable3() throws FileStateInvalidException, IOException {
        Utility.beginTrans(false);
        try {
            Method m = (Method) clazz.getContents().get(6);
            Statement stream = (Statement) m.getBody().getStatements().get(0);
            int start = JavaMetamodel.getManager().getElementPosition(stream, true).getBegin().getOffset();
            int end = JavaMetamodel.getManager().getElementPosition(stream, true).getEnd().getOffset();
            ExtractVariableRefactoring emr = new ExtractVariableRefactoring(resource, start, end);
            assertNull(emr.preCheck());
            emr.setName("extracted3");
            assertNull(emr.checkParameters());
            RefactoringSession result = RefactoringSession.create("extract variable, #3");
            assertNull(emr.prepare(result));
        assertNull(result.doRefactoring(true));
        } finally {
            Utility.endTrans();
        }
        assertFile("File is not correctly generated.",
            Utility.getFile(getDataDir(), "org/netbeans/test/extractvariable/TestClass.java"),
            getGoldenFile("testExtractVariable.pass"),
            getWorkDir()
        );
    }
    
    public void testExtractVariable4() throws FileStateInvalidException, IOException {
        Utility.beginTrans(false);
        try {
            Method m = (Method) clazz.getContents().get(8);
            Statement ifstmt = (Statement) m.getBody().getStatements().get(1);
            int start = JavaMetamodel.getManager().getElementPosition(ifstmt, true).getBegin().getOffset();
            int end = JavaMetamodel.getManager().getElementPosition(ifstmt, true).getEnd().getOffset();
            ExtractVariableRefactoring emr = new ExtractVariableRefactoring(resource, start, end);
            assertNull(emr.preCheck());
            emr.setName("extracted4");
            assertNull(emr.checkParameters());
            RefactoringSession result = RefactoringSession.create("extract variable, #4");
            assertNull(emr.prepare(result));
        assertNull(result.doRefactoring(true));
        } finally {
            Utility.endTrans();
        }
        assertFile("File is not correctly generated.",
            Utility.getFile(getDataDir(), "org/netbeans/test/extractvariable/TestClass.java"),
            getGoldenFile("testExtractVariable4.pass"),
            getWorkDir()
        );
    }
    
    public void testExtractMethod5() throws FileStateInvalidException, IOException {
        Utility.beginTrans(false);
        try {
            Method m = (Method) clazz.getContents().get(10);
            Statement s1 = (Statement) m.getBody().getStatements().get(0);
            Statement s2 = (Statement) m.getBody().getStatements().get(1);
            int start = JavaMetamodel.getManager().getElementPosition(s1, true).getBegin().getOffset();
            int end = JavaMetamodel.getManager().getElementPosition(s2, true).getEnd().getOffset();
            ExtractVariableRefactoring emr = new ExtractVariableRefactoring(resource, start, end);
            assertNull(emr.preCheck());
            emr.setName("extracted5");
            assertNull(emr.checkParameters());
            RefactoringSession result = RefactoringSession.create("extract variable, #5");
            assertNull(emr.prepare(result));
        assertNull(result.doRefactoring(true));
        } finally {
            Utility.endTrans();
        }
        assertFile("File is not correctly generated.",
            Utility.getFile(getDataDir(), "org/netbeans/test/extractvariable/TestClass.java"),
            getGoldenFile("testExtractVariable5.pass"),
            getWorkDir()
        );
    }
}
