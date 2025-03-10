/*
 * ClassOne.java
 *
 * Created on 03 February 2004, 22:13
 */

package org.netbeans.test.moveclass.abc.test_folder;

/**
 *
 * @author  Dan
 */
public class ClassOne {
    
    /** Creates a new instance of ClassOne */
    public ClassOne() {
    }
    
    public void go() {
        System.out.println(MoveMe.NAME);
        MoveMe.name = MoveMe.NAME;
    }
    
    protected void callMe () {
    }
    
}
