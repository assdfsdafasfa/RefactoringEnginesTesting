/*
 * C.java
 *
 * Created on May 24, 2004, 1:55 PM
 */

package abc;

import abc.def.DAnnotType;

/**
 *
 * @author  mg105252
 */
public class C extends B implements abc.def.ghi.I {
    
    private int number = 0;
    public String text = "text";
    protected boolean test = false;
    
    
    /** Creates a new instance of C */
    public C() {
        
    }

    public C(abc.def.ghi.I i) {
        test = true;
    }

    public String getState() {
        return "idle";
    }

    @DAnnotType(id=1, value="type")
    public Object methodA() {
        return null;
    }

    @DAnnotType(id=3)
    private void methodA(int i) {
        
    }

    protected void methodA(int i, String text) {
        
    }

    
}
