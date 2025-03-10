/*
 * D.java
 *
 * Created on May 21, 2004, 5:34 PM
 */

package abc.def;

import abc.En;

/**
 * class is offspring of class A
 * @author  mg105252
 */
public class D extends abc.A {
    
    @DAnnotType(id=1455)
    private abc.A<Integer> a;
    En en;
    
    /** Creates a new instance of D */
    public D() {
        abc.A a;
        a = new abc.A(1, "text");
        a.text = "Hello";
        a.methodA(2,"ciao", false);
    }
    
    public D(En e) {
        en=e;
    }
    /**
     * parameter of the method is enum En
     */
    @DAnnotType(id=7, value="methD", coord=En.Y)
    public void methodD(En e) {
        en=e;
    }
    
    public void methodD(abc.A proma) {
        
    }
    
    public void methodDI(abc.A proma, abc.def.ghi.I promi) {
        
    }
    
    public En getEn() {
        return en;
    }
    
}
