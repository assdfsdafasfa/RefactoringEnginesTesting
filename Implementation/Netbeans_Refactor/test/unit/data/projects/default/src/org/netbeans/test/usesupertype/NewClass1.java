package org.netbeans.test.refactoring.usesupertype;



/*
 * NewClass1.java
 *
 * @author Bharath Ravi Kumar
 */
class NewClassSuper {
    
    /** Creates a new instance of NewClass1 */
    public NewClassSuper() {
    }
    
}

class NewClassMid extends NewClassSuper{
    
    int someField;
    /** Creates a new instance of NewClass1 */
    public NewClassMid() {
    }
    
    public void doNothing(){
        
    }

}

class NewClass1 extends NewClassMid{
    
    /** Creates a new instance of NewClass1 */
    public NewClass1() {
    }
    
    public void doNothing(){
        
    }

    public void doSomething(){
        
    }

}

