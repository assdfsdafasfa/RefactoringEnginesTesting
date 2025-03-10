/*
 * CClass.java
 *
 * Created on 03 February 2004, 23:12
 */

package org.netbeans.test.moveclass.abc;

/**
 *
 * @author  Dan
 */
public class CClass {
    
    private org.netbeans.test.moveclass.abc.test_folder.MoveMe.Inner[] array_1;
    private org.netbeans.test.moveclass.abc.test_folder.MoveMe.Inner.Nested[] array_2;
    
    /** Creates a new instance of CClass */
    public CClass() {
    }
    
    public void go(org.netbeans.test.moveclass.abc.test_folder.MoveMe.Inner param) {
    }
    
    private class InnerClass {
        
        public int computeNumber(org.netbeans.test.moveclass.abc.test_folder.MoveMe.Inner param) {
            return param != null ? 1 : 0;
        }
        
    }
    
}
