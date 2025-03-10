/*
 * ClassA.java
 *
 * Created on August 22, 2005, 11:19 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package packageD;

import tempPack.ImportedClass;
import static tempPack.StaticImportedClass.CONST;
import static tempPack.StaticImportedClass.addInts;
/**
 *
 * @author jp159440
 */
public class ClassB {
    
    /** Creates a new instance of ClassA */
    public ClassB() {
    }
    
    public void method() {
        
    }
    
    public int field1;
    
    public static class Inner {
        
    }
    
}

class TopLevelClass {    
}

interface TopLevelInt {    
}

enum TopLevelEnum {   
}

@interface TopLevelAnnot {    
}