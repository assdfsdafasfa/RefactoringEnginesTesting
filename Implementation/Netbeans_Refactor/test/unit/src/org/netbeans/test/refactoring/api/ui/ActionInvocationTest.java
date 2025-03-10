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

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.netbeans.jmi.javamodel.Field;
import org.netbeans.jmi.javamodel.JavaClass;
import org.netbeans.jmi.javamodel.Method;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.java.ui.nodes.SourceNodeFactory;
import org.netbeans.modules.java.ui.nodes.SourceNodes;
import org.netbeans.modules.refactoring.api.ChangeParametersRefactoring;
import org.netbeans.modules.refactoring.api.EncapsulateFieldsRefactoring;
import org.netbeans.modules.refactoring.api.MoveClassRefactoring;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.modules.refactoring.api.ui.RefactoringActionsFactory;
import org.netbeans.modules.refactoring.spi.ui.ParametersPanel;
import org.netbeans.modules.refactoring.spi.ui.RefactoringUI;
import org.netbeans.test.refactoring.Utility;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;


/**
 *
 * @author Jan Becicka
 */
public class ActionInvocationTest extends NbTestCase {
        
    private static SourceNodeFactory factory;
            
    /** Creates a new instance of ActionInstantiationTest */
    public ActionInvocationTest(String name) {
        super(name);
    }
    
    public static NbTestSuite suite() {
        
        NbTestSuite suite = new NbTestSuite(ActionInvocationTest.class);        
        
        factory = SourceNodes.getExplorerFactory();

        return suite;
    }
    
    protected void setUp() {
       assertEquals(DD.class, Lookup.getDefault().lookup(DialogDisplayer.class).getClass());
    }
    
    public void testRenameAction() throws InterruptedException, InvocationTargetException {
        final JavaClass clazz = (JavaClass) Utility.findClass("org.netbeans.test.encapsulate.Encapsulate");
        Node node = factory.createClassNode(clazz);
        InstanceContent ic = new InstanceContent();
        Lookup lookup = new AbstractLookup(ic);
        ic.add(node);
        final Action rename = RefactoringActionsFactory.renameAction().createContextAwareInstance(lookup);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                if (rename.isEnabled()) {
                    rename.actionPerformed(RefactoringActionsFactory.DEFAULT_EVENT);
                    if (!((RenameRefactoring) DD.rui.getRefactoring()).getRefactoredObject().equals(clazz))
                        fail("Rename dialog was opened with wrong data");
                } else {
                    fail("Action is not enabled.");
                }
            }
        });
    }
    
    public void testEncapsulateFieldsAction() throws InterruptedException, InvocationTargetException {
        JavaClass clazz = (JavaClass) Utility.findClass("org.netbeans.test.encapsulate.Encapsulate");
        final Field a = clazz.getField("a", false);
        Node node = factory.createFieldNode(a);
        InstanceContent ic = new InstanceContent();
        Lookup lookup = new AbstractLookup(ic);
        ic.add(node);
        final Action encapsulate = RefactoringActionsFactory.encapsulateFieldsAction().createContextAwareInstance(lookup);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                if (encapsulate.isEnabled()) {
                    encapsulate.actionPerformed(RefactoringActionsFactory.DEFAULT_EVENT);
                    if (!((EncapsulateFieldsRefactoring) DD.rui.getRefactoring()).getSelectedObjects()[0].equals(a))
                        fail("EncapsulateField was opened with wrong data");
                    
                } else {
                    fail("Action is not enabled.");
                }
            }
        });
    }
    
    public void testChangeParametersAction() throws InterruptedException, InvocationTargetException {
        JavaClass clazz = (JavaClass) Utility.findClass("org.netbeans.test.encapsulate.Encapsulate");
        final Method m = clazz.getMethod("xxx", Collections.EMPTY_LIST, false);
        Node node = factory.createMethodNode(m);
        InstanceContent ic = new InstanceContent();
        Lookup lookup = new AbstractLookup(ic);
        ic.add(node);
        final Action change = RefactoringActionsFactory.changeParametersAction().createContextAwareInstance(lookup);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                if (change.isEnabled()) {
                    change.actionPerformed(RefactoringActionsFactory.DEFAULT_EVENT);
                    if (!((ChangeParametersRefactoring) DD.rui.getRefactoring()).getRefactoredObject().equals(m))
                        fail("ChangeParameters was opened with wrong data");
                } else {
                    fail("Action is not enabled.");
                }
            }
        });
    }

    public void testMoveClassAction() throws InterruptedException, InvocationTargetException {
        final JavaClass clazz = (JavaClass) Utility.findClass("org.netbeans.test.encapsulate.Encapsulate");
        Node node = factory.createClassNode(clazz);
        InstanceContent ic = new InstanceContent();
        Lookup lookup = new AbstractLookup(ic);
        ic.add(node);
        final Action move = RefactoringActionsFactory.moveClassAction().createContextAwareInstance(lookup);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                if (move.isEnabled()) {
                    move.actionPerformed(RefactoringActionsFactory.DEFAULT_EVENT);
                    if (!((MoveClassRefactoring) DD.rui.getRefactoring()).getResources().toArray()[0].equals(clazz.getResource()))
                        fail("MoveClass was opened with wrong data");
                } else {
                    fail("Action is not enabled.");
                }
            }
        });
    }
    
    public static final class Lkp extends org.openide.util.lookup.AbstractLookup {
        public Lkp () {
            this (new org.openide.util.lookup.InstanceContent ());
        }
        
        private Lkp (org.openide.util.lookup.InstanceContent ic) {
            super (ic);
            ic.add (new DD ());
        }
    }

    /** Our own dialog displayer.
     */
    public static final class DD extends org.openide.DialogDisplayer {
        public static Object[] options;
        public static RefactoringUI rui;
        private Object toReturn;
        
        public java.awt.Dialog createDialog(org.openide.DialogDescriptor descriptor) {
            JDialog dialog = new JDialog() {
                public void setVisible(boolean visible) {
                }
                
                public void show() {
                }
            };
            toReturn = descriptor.getMessage();
            if (toReturn instanceof Component)
                dialog.getContentPane().add((Component) toReturn);
            
            if (toReturn instanceof ParametersPanel) {
                try {
                    java.lang.reflect.Field f = toReturn.getClass().getDeclaredField("rui");
                    f.setAccessible(true);
                    rui = (RefactoringUI) f.get(toReturn);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            
            return dialog;
        }
        
        public Object notify(org.openide.NotifyDescriptor descriptor) {
            assertNull (options);
            assertNotNull(toReturn);
            options = descriptor.getOptions();
            Object r = toReturn;
            toReturn = null;
            return r;
        }
        
    } // end of DD
    
}
