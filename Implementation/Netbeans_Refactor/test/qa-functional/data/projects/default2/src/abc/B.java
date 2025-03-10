/*
 * B.java
 *
 * Created on May 24, 2004, 1:55 PM
 */

package abc;

import java.util.ArrayList;

/**
 *
 * @author  mg105252
 */
public class B<T> extends A<T> {
    
    private int number = 0;
    public String text = "text";
    protected boolean test = false;
    
    
    /** Creates a new instance of B */
    public B() {
        list = new ArrayList<String>();
    }

    public T methodA() {
        return null;
    }

    private void methodA(int i) {        
        number = i;
        
    }

    protected void methodA(int i, String text) {
        methodA(null,"hello", true);
    }

    
}
