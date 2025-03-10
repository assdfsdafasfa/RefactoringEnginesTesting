/*
 * SubClass_1.java
 *
 * Created on 12 February 2004, 11:48
 */

package org.netbeans.test.rename.over_methods;

/**
 *
 * @author  Dan
 */
public class SubClass_1 extends Extension_2 {
    
    /** Creates a new instance of SubClass_1 */
    public SubClass_1() {
    }
    
    public int generateNumber(int x, int y) {
        return sumNumbers(x, y);
    }
    
    protected void waitAMinute() {
        System.out.println("waiting");
    }
    
    static void countToFour() {
        for (int x = 1; x <= 4; x++)
            System.out.println(x);
    }
    
    public final void run() {
    }
    
}
