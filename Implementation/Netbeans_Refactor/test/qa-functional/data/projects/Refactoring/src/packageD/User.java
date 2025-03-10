/*
 * Toucher.java
 *
 * Created on August 22, 2005, 11:27 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package packageD;

import packageD.ClassB.Inner;
import tempPack.ImportedClass;
import tempPack.StaticImportedClass;

/**
 *
 * @author jp159440
 */
@TopLevelAnnot
public class User implements TopLevelInt {
    
    /** Creates a new instance of Toucher */
    public User() {
        TopLevelEnum enumeration;
        ImportedClass ic = new ImportedClass();
        int i = StaticImportedClass.addInts(StaticImportedClass.CONST,1);
        ClassB b = new ClassB();
        b.field1 = 1;
        b.method();
        ClassB.Inner inner;
        TopLevelClass tlc = new TopLevelClass();               
    }
    
}
