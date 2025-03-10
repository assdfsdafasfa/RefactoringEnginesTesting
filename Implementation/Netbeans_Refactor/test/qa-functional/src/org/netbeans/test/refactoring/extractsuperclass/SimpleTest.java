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

package org.netbeans.test.refactoring.extractsuperclass;

import java.io.File;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import org.netbeans.modules.refactoring.api.ExtractSuperClassRefactoring;

/**
 *
 * @author jp159440
 */
public class SimpleTest extends ExtractSuperClassTestCase{

    /** Creates a new instance of SimpleTest */
    public SimpleTest(String name) {
        super(name);
    }

    public void prepareProject() {
        ProjectSupport.openProject(new File(getDataDir(), "projects/Refactoring"));
        classPathWorkDir=new File(getDataDir(), "projects.Refactoring.src".replace('.', File.separatorChar));
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new SimpleTest("testExtractSuperClass1"));
        suite.addTest(new SimpleTest("testExtractSuperClass2"));
        suite.addTest(new SimpleTest("testExtractSuperClass3"));
        suite.addTest(new SimpleTest("testExtractSuperClass4"));
        suite.addTest(new SimpleTest("testExtractSuperClass5"));
        suite.addTest(new SimpleTest("testExtractSuperClass6"));
        suite.addTest(new SimpleTest("testExtractSuperClass7"));
        suite.addTest(new SimpleTest("testExtractSuperClass8"));
        suite.addTest(new SimpleTest("testExtractSuperClass9"));        
        suite.addTest(new SimpleTest("testUndoExtractSuperClass10"));        
        suite.addTest(new SimpleTest("testUndoExtractSuperClass11"));        
        return suite;
    }
    
    public void testExtractSuperClass1() {     
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getField("packageC.ClassA","field1"));
        ExtractSuperClassRefactoring.MemberInfo m2 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.ClassA","method1",null),false);
        ExtractSuperClassRefactoring.MemberInfo m3 = new ExtractSuperClassRefactoring.MemberInfo(getIface("packageC.ClassA","Inter"));
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1,m2,m3};
        extractSuperClass("packageC.ClassA","SuperA",elems,false);
    }
    
    public void testExtractSuperClass2() {
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.ClassB","method",null),true);
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.ClassB","SuperB",elems,false);
    }
    
    public void testExtractSuperClass3() {
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.TopLevelElement","method2",null),false);
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.TopLevelElement","SuperC",elems,false);
    }
    
    public void testExtractSuperClass4() {
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.Interf","method",null),false);
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.Interf","SuperD",elems,false);
    }
    
    public void testExtractSuperClass5() {
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.myEnum","translate",null),false);
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.myEnum","SuperE",elems,false);
    }
    
    public void testExtractSuperClass6() {
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.myAnnot","method",null),false);
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.myAnnot","SuperF",elems,false);
    }
    
    public void testExtractSuperClass7() {
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.ClassE","method",new String[]{"T"}),false);
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.ClassE","SuperG",elems,false);
    }
    
    public void testExtractSuperClass8() {
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.ClassF","method",null),false);
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.ClassF","SuperH",elems,true);
    }
            
    
    public void testExtractSuperClass9() {        
        extractSuperClass("packageC.ClassB","Inter",null,false);
    }
    
    //-----------------UNDOs----------------------
    public void testUndoExtractSuperClass10() {     
        ExtractSuperClassRefactoring.MemberInfo m1 = new ExtractSuperClassRefactoring.MemberInfo(getField("packageC.ClassI","field1"));        
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m1};
        extractSuperClass("packageC.ClassI","SuperI",elems,true);
    }
    
    public void testUndoExtractSuperClass11() {           
        ExtractSuperClassRefactoring.MemberInfo m2 = new ExtractSuperClassRefactoring.MemberInfo(getMethod("packageC.ClassJ","method1",null),false);
        ExtractSuperClassRefactoring.MemberInfo m3 = new ExtractSuperClassRefactoring.MemberInfo(getIface("packageC.ClassJ","Inter"));
        ExtractSuperClassRefactoring.MemberInfo[] elems = {m2,m3};
        extractSuperClass("packageC.ClassJ","SuperJ",elems,true);
    }
    
    
}
