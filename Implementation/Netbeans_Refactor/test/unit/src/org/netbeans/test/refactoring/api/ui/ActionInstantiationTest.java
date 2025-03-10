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

package org.netbeans.test.refactoring.api.ui;

import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.refactoring.api.ui.RefactoringActionsFactory;
import org.netbeans.modules.refactoring.ui.AnonymousToInnerAction;
import org.netbeans.modules.refactoring.ui.ChangeParametersAction;
import org.netbeans.modules.refactoring.ui.EncapsulateFieldAction;
import org.netbeans.modules.refactoring.ui.ExtractMethodAction;
import org.netbeans.modules.refactoring.ui.MoveClassAction;
import org.netbeans.modules.refactoring.ui.PullUpAction;
import org.netbeans.modules.refactoring.ui.PushDownAction;
import org.netbeans.modules.refactoring.ui.RenameAction;
import org.netbeans.modules.refactoring.ui.SafeDeleteAction;
import org.netbeans.modules.refactoring.ui.UseSuperTypeAction;

/**
 *
 * @author Jan Becicka
 */
public class ActionInstantiationTest extends NbTestCase {
        
    /** Creates a new instance of ActionInstantiationTest */
    public ActionInstantiationTest(String name) {
        super(name);
    }
    
    public void testRenameAction() {
        assert RefactoringActionsFactory.renameAction() instanceof RenameAction;
        
    }
    
    public void testChangeParametersAction() {
        assert RefactoringActionsFactory.changeParametersAction() instanceof ChangeParametersAction;
    }
    
    public void testMoveClassAction() {
        assert RefactoringActionsFactory.moveClassAction() instanceof MoveClassAction;
    }

    public void testEncapsulateFieldsAction() {
        assert RefactoringActionsFactory.encapsulateFieldsAction() instanceof EncapsulateFieldAction;
    } 
    
    public void testAnonymousToInnerAction() {
        assert RefactoringActionsFactory.anonymousToInnerAction() instanceof AnonymousToInnerAction;
    }
    
    public void testExtractMethodAction() {
        assert RefactoringActionsFactory.extractMethodAction() instanceof ExtractMethodAction;
    }
    
    public void testPullUpAction() {
        assert RefactoringActionsFactory.pullUpAction() instanceof PullUpAction;
    }
    
    public void testPushDownAction() {
        assert RefactoringActionsFactory.pushDownAction() instanceof PushDownAction;
    }
    
    public void testUseSuperTypeAction () {
        assert RefactoringActionsFactory.useSuperTypeAction() instanceof UseSuperTypeAction;
    }

    public void testDEFAULT_EVENT_UnsupportedOperation() {
        try {
            RefactoringActionsFactory.DEFAULT_EVENT.setSource(null);
        } catch (UnsupportedOperationException uoe) {
            return ;
        }
        assert false : "UnsupportedOperationException was not thrown";
    }
    
}
