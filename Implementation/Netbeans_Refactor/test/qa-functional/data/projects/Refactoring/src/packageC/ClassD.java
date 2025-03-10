/*
 * ClassD.java
 *
 * Created on August 22, 2005, 10:12 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package packageC;

/**
 *
 * @author jp159440
 */
public class ClassD {
    
    /** Creates a new instance of ClassD */
    public ClassD() {
    }
    
}

interface Interf {
    public void method();
}

enum myEnum {
    A,B,C;
    
    public void translate() {
        
    }
}

@interface myAnnot {
    String field = "abc";
    String method();
}
