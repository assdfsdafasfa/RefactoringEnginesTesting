/*
 * Extension_2.java
 *
 * Created on 12 February 2004, 11:46
 */

package org.netbeans.test.rename.over_methods;

/**
 *
 * @author  Dan
 */
public class Extension_2 extends Extension_1 implements Interface_2 {
    
    /** Creates a new instance of Extension_2 */
    public Extension_2() {
    }
    
    public String getName() {
        return "this is a name";
    }
    
    public int generateNumber (int x, int y) {
        return x*x + y*y*y - x*y;
    }
    
    protected void waitAMinute() {
    }
    
}
