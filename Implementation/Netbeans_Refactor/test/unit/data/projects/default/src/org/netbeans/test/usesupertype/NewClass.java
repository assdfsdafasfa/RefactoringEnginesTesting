package org.netbeans.test.refactoring.usesupertype;

/*
 * NewClass.java
 *
 * @author  Bharath Ravi Kumar
 */
public class NewClass {
    /** Creates a new instance of NewClass */
    NewClass1 nc = new NewClass1();
    NewClassMid ncMid = new NewClassMid();
    public NewClass() {
        nc.toString();
        if(nc instanceof NewClass1)
            ;
    }
    public void constrainingUsage(){
        nc.doNothing();
        ncMid.someField = 24;
    }
}