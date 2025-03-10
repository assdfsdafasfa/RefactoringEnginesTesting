/*
 * ClassE.java
 *
 * Created on August 10, 2005, 9:28 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package packageB;

/**
 *
 * @author jp159440
 */
public class ClassE{
    
    public static final int publicStaticFinalField=1;
    static final int defaultStaticFinalField=2;
    protected static final int protectedStaticFinalField=3;
    private  static final int privateStaticFinalField=4;
    
    public final int publicFinalInt=5;
       
    public int publicField;
    int defaultField;
    protected int protectedField;
    private  int privateField;
    
    
    /** Creates a new instance of ClassB */
    public ClassE() {
    }
    
    public int method1(int x,double y) {
        return 0;
    }
    
    public void method2() {        
    }
    
    private void privateMethod() {        
    }
    
    protected void protectedMethod() {       
    }
    
    void defaultMethod() {       
    }
    
    
    
}
