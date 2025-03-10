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

package org.netbeans.test.refactoring.extractinterface;

import java.io.File;
import java.util.Collections;
import org.netbeans.jmi.javamodel.Feature;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import org.netbeans.test.refactoring.Utility;
/**
 *
 * @author jp159440
 */
public class SimpleTest extends ExtractInterfaceTestCase{

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
        suite.addTest(new SimpleTest("testExtractInterface1"));
        suite.addTest(new SimpleTest("testExtractInterface2"));
        suite.addTest(new SimpleTest("testExtractInterface3"));
        suite.addTest(new SimpleTest("testExtractInterface4"));
        suite.addTest(new SimpleTest("testExtractInterface5"));
        suite.addTest(new SimpleTest("testExtractInterface6"));
        suite.addTest(new SimpleTest("testExtractInterface7"));
        suite.addTest(new SimpleTest("testExtractInterface8"));
        suite.addTest(new SimpleTest("testUndoExtractInterface9"));        
        return suite;
    }
    
    public void testExtractInterface1() {
        MembersToExtract members = new MembersToExtract();
        members.addMethod("method2",null);
        extractInterface("packageB.ClassA","newIface",members,false);
    }
    
    public void testExtractInterface2() {
        MembersToExtract members = new MembersToExtract();
        members.addMethod("method1",new String[]{"int","int"});
        extractInterface("packageB.InterfaceA","newIfaceFromIface",members,false);
    }
    
    public void testExtractInterface3() {
        MembersToExtract members = new MembersToExtract();
        members.addMethod("translate",new String[]{"packageB.Enumeration"});
        extractInterface("packageB.Enumeration","newIfaceFromEnum",members,false);
    }
    
    public void testExtractInterface4() {
        MembersToExtract members = new MembersToExtract();
        members.addMethod("method",new String[]{"T"});
        extractInterface("packageB.ClassB","newGenericIface",members,false);
    }
    
    public void testExtractInterface5() {
        MembersToExtract members = new MembersToExtract();
        members.addIface("Runnable");
        members.addMethod("run",null);
        extractInterface("packageB.ClassC","newIfaceImplementingIface",members,false);
    }
    
    public void testExtractInterface6() {        
        extractInterface("packageB.ClassC","wrong.name",null,false);
    }
    
    public void testExtractInterface7() {
        MembersToExtract members = new MembersToExtract();        
        extractInterface("packageB.TopLevelClass","newIfaceFromTopClass",null,false);
    }
    
    public void testExtractInterface8() {
        MembersToExtract members = new MembersToExtract();        
        extractInterface("packageB.TopLevelInterface","newIfaceFromTopIface",null,false);
    }
    
    public void testUndoExtractInterface9() {
        MembersToExtract members = new MembersToExtract();        
        extractInterface("packageB.ClassE","undoIface",null,true);
    }
    
    
}
