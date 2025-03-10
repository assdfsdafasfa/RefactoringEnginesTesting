/*
 * BClass.java
 *
 * Created on 03 February 2004, 22:51
 */

package org.netbeans.test.moveclass.abc;

import org.netbeans.test.moveclass.abc.test_folder.MoveMe;

/**
 *
 * @author  Dan
 */
public class BClass {
    
    /** Creates a new instance of BClass */
    public BClass() {
    }
    
    public void go(MoveMe moveMe) {
        MoveMe localMoveMe = moveMe;
    }
    
}
