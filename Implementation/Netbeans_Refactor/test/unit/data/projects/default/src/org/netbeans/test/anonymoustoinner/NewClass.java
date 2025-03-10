package org.netbeans.test.anonymoustoinner;

public class NewClass {
    
    /** Creates a new instance of NewClass */
    public NewClass() {
        Runnable run = new Runnable() {
            public void run() {
                System.out.println("foo");
            }
        };
    }
}
