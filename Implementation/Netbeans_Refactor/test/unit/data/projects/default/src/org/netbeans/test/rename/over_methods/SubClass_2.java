/*
 * SubClass_2.java
 *
 * Created on 12 February 2004, 11:49
 */

package org.netbeans.test.rename.over_methods;

/**
 *
 * @author  Dan
 */
public class SubClass_2 extends Extension_2 {
    
    /** Creates a new instance of SubClass_2 */
    public SubClass_2() {
    }
    
    public int generateNumber(int xx, int yy) {
        return super.generateNumber(xx, yy) + sumNumbers(xx, yy);
    }
    
    public void go () {
        System.out.println(generateNumber(20, sumNumbers(2,3)));
    }
    
}
