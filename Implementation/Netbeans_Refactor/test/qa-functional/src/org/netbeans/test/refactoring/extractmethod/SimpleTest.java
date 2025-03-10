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

package org.netbeans.test.refactoring.extractmethod;

import java.io.File;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import java.lang.reflect.Modifier;
/**
 *
 * @author jp159440
 */
public class SimpleTest extends ExtractMethodTestCase{



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
        suite.addTest(new SimpleTest("testExtractMethod01"));
        suite.addTest(new SimpleTest("testExtractMethod02"));
        suite.addTest(new SimpleTest("testExtractMethod03"));
        suite.addTest(new SimpleTest("testExtractMethod04"));
        suite.addTest(new SimpleTest("testExtractMethod05"));
        suite.addTest(new SimpleTest("testExtractMethod06"));
        suite.addTest(new SimpleTest("testExtractMethod07"));
        suite.addTest(new SimpleTest("testExtractMethod08"));
        suite.addTest(new SimpleTest("testExtractMethod09"));
        suite.addTest(new SimpleTest("testExtractMethod10"));
        suite.addTest(new SimpleTest("testExtractMethod11"));
        suite.addTest(new SimpleTest("testExtractMethod12"));
        suite.addTest(new SimpleTest("testExtractMethod13"));
        suite.addTest(new SimpleTest("testUndoExtractMethod14"));
        suite.addTest(new SimpleTest("testUndoExtractMethod15"));
        suite.addTest(new SimpleTest("testUndoExtractMethod16"));
        suite.addTest(new SimpleTest("testUndoExtractMethod17"));
        suite.addTest(new SimpleTest("testUndoExtractMethod18"));
        return suite;
    }
    
    public void testExtractMethod01() { 
        extractMethod("packageA.ClassA","//1","//2","first",Modifier.PUBLIC,false,false);        
    }
    
    public void testExtractMethod02() { 
        extractMethod("packageA.ClassB","//3","//4","second",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod03() { 
        extractMethod("packageA.ClassC","//4","//5","third",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod04() {
        extractMethod("packageA.ClassD","//7","//8","fourth",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod05() {
        extractMethod("packageA.ClassE",600,700,"fifth",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod06() {
        extractMethod("packageA.ClassF","//0","//1","sixth",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod07() {
        extractMethod("packageA.ClassG","//6","//7","seventh",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod08() {
        extractMethod("packageA.ClassH","//4","//5","existing",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod09() {
        extractMethod("packageA.ClassI","//4","//5","existing2",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod10() {
        extractMethod("packageA.ClassJ","//9","//10","eight",Modifier.PUBLIC,false,false);
    }
    
    public void testExtractMethod11() {
        extractMethod("packageA.ClassK","//9","//10","ninth",Modifier.PROTECTED,true,false);    
    }
                                
    public void testExtractMethod12() {
        extractMethod("packageA.ClassL","//11","//12","ninth",Modifier.PUBLIC,false,false);    
    }
    
    public void testExtractMethod13() {
        extractMethod("packageA.ClassM","//7","//8","nin.th",Modifier.PUBLIC,false,false);    
    }
    //----------------------UNDOs--------------------------------------------------------
    public void testUndoExtractMethod14() {
        extractMethod("packageA.ClassN","//1","//2","first",Modifier.PUBLIC,false,true);    
    }
    
    public void testUndoExtractMethod15() { 
        extractMethod("packageA.ClassO","//3","//4","second",Modifier.PUBLIC,false,true);
    }
    
    public void testUndoExtractMethod16() { 
        extractMethod("packageA.ClassP","//4","//5","third",Modifier.PUBLIC,false,true);
    }
    
    public void testUndoExtractMethod17() {
        extractMethod("packageA.ClassQ","//7","//8","fourth",Modifier.PUBLIC,false,true);
    }
    
    public void testUndoExtractMethod18() {
        extractMethod("packageA.ClassR","//9","//10","eight",Modifier.PUBLIC,false,true);
    }
    
    
    
}
