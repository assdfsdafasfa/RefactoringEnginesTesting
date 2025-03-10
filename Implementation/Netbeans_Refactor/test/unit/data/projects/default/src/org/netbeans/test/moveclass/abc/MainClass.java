/*
 * MainClass.java
 *
 * Created on 03 February 2004, 22:23
 */

package org.netbeans.test.moveclass.abc;

/**
 *
 * @author  Dan
 */
public class MainClass {
    
    /** Creates a new instance of MainClass */
    public MainClass() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        org.netbeans.test.moveclass.abc.test_folder.MoveMe moveMe = null;
    }
    
    public void go(org.netbeans.test.moveclass.abc.test_folder.MoveMe param_1, org.netbeans.test.moveclass.abc.test_folder.MoveMe param_2) {
        System.out.println(param_1 == param_2);
    }
    
}
