/*
 * ClassG.java
 *
 * Created on May 18, 2006, 3:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package packageD;

/**
 *
 * @author jp159440
 */
public class ClassG {
    private ClassG instance = null;
    
    public ClassG getDefault() {
        if(instance==null) {
            instance = new ClassG();
        }
        return instance;
    }
    
}
