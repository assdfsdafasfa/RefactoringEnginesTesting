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

package org.netbeans.test.refactoring.safedelete;

import java.io.File;
import java.util.Collections;
import java.util.List;
import junit.textui.TestRunner;
import org.netbeans.jmi.javamodel.ClassMember;
import org.netbeans.jmi.javamodel.Element;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.Resource;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.junit.ide.ProjectSupport;
import org.netbeans.test.refactoring.Utility;

/**
 *
 * @author jp159440
 */
public class SimpleTest extends SafeDeleteTestCase{

    public SimpleTest(String name) {
        super(name);
    }

    public void prepareProject() {
        ProjectSupport.openProject(new File(getDataDir(), "projects/Refactoring"));
        classPathWorkDir=new File(getDataDir(), "projects.Refactoring.src".replace('.', File.separatorChar));
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new SimpleTest("testSafeDelete1"));
        suite.addTest(new SimpleTest("testSafeDelete2"));
        suite.addTest(new SimpleTest("testSafeDelete3"));
        suite.addTest(new SimpleTest("testSafeDelete4"));
        suite.addTest(new SimpleTest("testSafeDelete5"));
        suite.addTest(new SimpleTest("testSafeDelete6"));
        suite.addTest(new SimpleTest("testSafeDelete7"));
        suite.addTest(new SimpleTest("testSafeDelete8"));
        suite.addTest(new SimpleTest("testSafeDelete9"));
        suite.addTest(new SimpleTest("testSafeDelete10"));
        suite.addTest(new SimpleTest("testSafeDelete11"));
        suite.addTest(new SimpleTest("testSafeDelete12"));
        suite.addTest(new SimpleTest("testSafeDelete13"));
        suite.addTest(new SimpleTest("testSafeDelete14"));
        suite.addTest(new SimpleTest("testSafeDelete15"));
        suite.addTest(new SimpleTest("testSafeDelete16"));
        suite.addTest(new SimpleTest("testSafeDelete17"));
        suite.addTest(new SimpleTest("testSafeDelete18"));
        suite.addTest(new SimpleTest("testSafeDelete19"));
        suite.addTest(new SimpleTest("testSafeDelete20"));
        suite.addTest(new SimpleTest("testUndoSafeDelete21"));
        suite.addTest(new SimpleTest("testUndoSafeDelete22"));
        suite.addTest(new SimpleTest("testUndoSafeDelete23"));
        suite.addTest(new SimpleTest("testUndoSafeDelete24"));
        suite.addTest(new SimpleTest("testUndoSafeDelete25"));
        suite.addTest(new SimpleTest("testUndoSafeDelete26"));
        suite.addTest(new SimpleTest("testUndoSafeDelete27"));
        return suite;
    }
    
    public void testSafeDelete1() {
        final String className = "packageD.ClassA";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = Utility.findClass(className).getField("i",false);                
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        System.out.println("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete2() {
        final String className = "packageD.ClassD";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = Utility.findClass(className).getField("i",false);                        
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},true,false);
    }
    
    public void testSafeDelete3() {
        final String className = "packageD.ClassE";
        Resource resource = Utility.findClass(className).getResource();        
        Element elem = Utility.findClass(className).getField("i",false);                
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete4() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = (Element) resource.getImports().get(0);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("import")+2);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete5() {
        final String className = "packageD.ClassH";
        Resource resource = Utility.findClass(className).getResource();
        
        Element elem = resource.getElementByOffset(0);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete6() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = Utility.findClass(className).getMethod("method",Collections.EMPTY_LIST,false);
        System.out.println(elem);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("method")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete7() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = Utility.findClass(className).getField("field1",false);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("field1")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete8() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        List contents = Utility.findClass(className).getContents();        
        Element elem = (Element) contents.get(3);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("Inner")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete9() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(5);
        /*for(int i =0 ; i<childs.size();i++) {
            Element e = (Element) childs.get(i);
            System.out.println(i+" "+e);
        }
        */
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelClass")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete10() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(6);
        
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelInt")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete11() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(7);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelEnum")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete12() {
        final String className = "packageD.ClassB";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(8);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelAnnot")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete13() {        
        final String className = "packageD.ClassF";
        Resource resource = Utility.findClass(className).getResource();        
        Element elem = Utility.findClass(className).getMethod("method",Collections.EMPTY_LIST,false);        
        System.out.println(elem);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("method")+1);        
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));        
        safeDelete(className,new Element[]{elem},false,false);        
    }
    
    public void testSafeDelete14() {
        final String className = "packageD.ClassF";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = Utility.findClass(className).getField("field1",false);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("field1")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete15() {
        final String className = "packageD.ClassF";
        Resource resource = Utility.findClass(className).getResource();
        System.out.println(resource.getSourceText());
        List contents = Utility.findClass(className).getContents();        
        int i;
        for(i = 0 ; i<contents.size();i++) {
            ClassMember cm = (ClassMember) contents.get(i);
            if("class packageD.ClassF.Inner".equals(cm.toString())) break;            
        }
        assertFalse("Element not found",i==contents.size());                
        Element elem = (Element) contents.get(i);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("Inner")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete16() {
        final String className = "packageD.ClassF";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        int i;
        for(i = 0 ; i<childs.size();i++) {
            Element e = (Element) childs.get(i);
            if("class packageD.TopLevelClass2".equals(e.toString())) break;
        }
        assertFalse("Element not found",i==childs.size());                
        Element elem = (Element) childs.get(i);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelClass2")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete17() {
        final String className = "packageD.ClassF";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        int i;
        for(i = 0 ; i<childs.size();i++) {
            Element e = (Element) childs.get(i);
            if("class packageD.TopLevelInt2".equals(e.toString())) break;
        }                       
        assertFalse("Element not found",i==childs.size());                
        Element elem = (Element) childs.get(i);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelInt2")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete18() {
        final String className = "packageD.ClassF";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        int i;
        for(i = 0 ; i<childs.size();i++) {
            Element e = (Element) childs.get(i);
            System.out.println(e);
            if("enum packageD.TopLevelEnum2".equals(e.toString())) break;
        }                       
        assertFalse("Element not found",i==childs.size());                
        Element elem = (Element) childs.get(i);        
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelEnum2")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete19() {
        final String className = "packageD.ClassJ";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        int i;
        for(i = 0 ; i<childs.size();i++) {
            Element e = (Element) childs.get(i);
            System.out.println(e);
            if("class packageD.TopLevelAnnot4".equals(e.toString())) break;
        }                       
        assertFalse("Element not found",i==childs.size());                
        Element elem = (Element) childs.get(i);                
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelAnnot4")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
    public void testSafeDelete20() {
        final String className = "packageD.ClassG";
        Resource resource = Utility.findClass(className).getResource();
        
        Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("ClassG")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,false);
    }
    
     public void testUndoSafeDelete21() {
        final String className = "packageD.ClassI";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = Utility.findClass(className).getMethod("method",Collections.EMPTY_LIST,false);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("method")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,true);
    }
    
    public void testUndoSafeDelete22() {
        final String className = "packageD.ClassI";
        Resource resource = Utility.findClass(className).getResource();
        Element elem = Utility.findClass(className).getField("field1",false);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("field1")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,true);
    }
    
    public void testUndoSafeDelete23() {
        final String className = "packageD.ClassI";
        Resource resource = Utility.findClass(className).getResource();
        List contents = Utility.findClass(className).getContents();        
        Element elem = (Element) contents.get(3);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("Inner")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,true);
    }
    
    public void testUndoSafeDelete24() {
        final String className = "packageD.ClassI";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(5);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelClass3")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,true);
    }
    
    public void testUndoSafeDelete25() {
        final String className = "packageD.ClassI";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(6);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelInt3")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,true);
    }
    
    public void testUndoSafeDelete26() {
        final String className = "packageD.ClassI";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(7);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelEnum3")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,true);
    }
    
    public void testUndoSafeDelete27() {
        final String className = "packageD.ClassI";
        Resource resource = Utility.findClass(className).getResource();
        List childs = resource.getChildren();
        Element elem = (Element) childs.get(8);
        //Element elem = resource.getElementByOffset(resource.getSourceText().indexOf("TopLevelAnnot3")+1);
        ref("Selected element:"+ resource.getSourceText().subSequence(elem.getStartOffset(),elem.getEndOffset()));
        safeDelete(className,new Element[]{elem},false,true);
    }
    
     public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    
}
