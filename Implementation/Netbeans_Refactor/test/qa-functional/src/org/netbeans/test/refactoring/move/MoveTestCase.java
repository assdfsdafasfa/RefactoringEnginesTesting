/*
 * MoveTestCase.java
 *
 * Created on August 23, 2004, 3:49 PM
 */

package org.netbeans.test.refactoring.move;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.netbeans.jmi.javamodel.Resource;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.modules.refactoring.api.MoveClassRefactoring;
import org.netbeans.modules.refactoring.spi.RefactoringElementsBag;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.test.refactoring.RefactoringTestCase;
import org.netbeans.test.refactoring.Utility;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.URLMapper;


/**
 *
 * @author  eh103527
 */
public class MoveTestCase extends RefactoringTestCase {
    
    /** Creates a new instance of MoveTestCase */
    public MoveTestCase(String name) {
        super(name);
    }
    
    protected void moveClass(String name, String newPackage, boolean undo) {
        setJavaClass(name);
        ref("Move class "+name+" to "+newPackage);
        
        HashMap files=null;
        RefactoringSession result = RefactoringSession.create("Move Class");
        boolean finish=false;
        Utility.prepareTest();
        try {
            Resource resource = (Resource) jc.refImmediateComposite();
            FileObject classPathWorkDirFO = FileUtil.toFileObject(classPathWorkDir);
            Collection list = new ArrayList(1);
            list.add(resource);
            MoveClassRefactoring refactoring = new MoveClassRefactoring(list);
            if (refProblems( refactoring.preCheck() )) throw new Exception("Fatal problem");
            refactoring.setTargetClassPathRoot(classPathWorkDirFO);
            refactoring.setTargetPackageName(newPackage);
            if (refProblems(refactoring.checkParameters()))  throw new Exception("Fatal problem");
            if (refProblems( refactoring.prepare(result) )) throw new Exception("Fatal problem");
            files=getResources(result);
            if (refProblems( result.doRefactoring(true) )) throw new Exception("Fatal problem");
            finish=true;
        } catch (Throwable t) {
            if (t.getMessage() == null || !t.getMessage().equals("Fatal problem")) {
                t.printStackTrace(getLogStream());
                assertTrue(t.getMessage(), false);
            }
        } finally {
            Utility.finishTest();
        }
        if (finish) {
            try {
                Thread.sleep(2000);
                if (undo) {
                    JavaMetamodel.getUndoManager().undo();
                    Thread.sleep(2000);
                }
                compareResources(files, result, name, newPackage+"."+((name.indexOf('.') > -1) ? name.substring(name.lastIndexOf('.')+1):name));
            } catch (Exception ex) {
                ex.printStackTrace(getLogStream());
                assertTrue(ex.getMessage(), false);
            }
        }
    }
}
