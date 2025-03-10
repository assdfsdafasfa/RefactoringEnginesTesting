/*
 * ClassC.java
 *
 * Created on August 22, 2005, 1:44 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package packageD;

/**
 *
 * @author jp159440
 */
public class ClassC {
           
    private int field;
    private void method() {
        
    }
    
    private class Inner {
        
    }
    
    
    public void user() {
        field=1;
        method();
        Inner i = new Inner();
    }
    
}
