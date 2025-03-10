/*
 * SecondClass.java
 *
 * Created on 18 January 2004, 21:42
 */

package org.netbeans.test.rename;

import java.util.List;
import java.util.Iterator;
import org.netbeans.test.rename.MainClass;
import java.util.Collections;

/**
 *
 * @author  Dan
 */
public class SecondClass {
    
    /** Creates a new instance of SecondClass */
    public SecondClass() {
    }
    
    public void runMe() {
        MainClass inst = new MainClass();
        inst.go();
        if (MainClass.MAX % 2 == MainClass.MAX % 3) {
            System.out.println(inst.getValue('q', inst.getValue('d', 3)));
        } else {
            System.out.println(inst.getValue('f', 7));
        }
        inst.main(null);
    }
    
    public static class InnerClass extends MainClass {
        
        public InnerClass() {
            super(2, "");
        }
        
    }
    
}
