/*
 * MoveMe.java
 *
 * Created on 03 February 2004, 22:13
 */

package org.netbeans.test.moveclass.abc.test_folder;

/**
 *
 * @author  Dan
 */
public class MoveMe extends RuntimeException {
    
    public static String NAME = "The Name";
    
    static String name = "";
    
    private ClassOne instanceOfClassOne = null;
    
    /** Creates a new instance of MoveMe */
    public MoveMe() {
    }
    
    public org.netbeans.test.moveclass.abc.test_folder.MoveMe go(MoveMe param_1, String s, org.netbeans.test.moveclass.abc.test_folder.MoveMe param_2) throws org.netbeans.test.moveclass.abc.test_folder.MoveMe {
        if (instanceOfClassOne instanceof org.netbeans.test.moveclass.abc.test_folder.ClassOne) {
            System.out.println("It is an instance !");
        }
        instanceOfClassOne.callMe();
        return (org.netbeans.test.moveclass.abc.test_folder.MoveMe) null;
    }
    
    public static class Inner {
        
        public static class Nested {
        }
        
    }
    
}
