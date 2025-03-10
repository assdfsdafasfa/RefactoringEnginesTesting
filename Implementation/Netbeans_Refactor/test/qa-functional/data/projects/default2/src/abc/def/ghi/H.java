/*
 * H.java
 *
 * Created on May 24, 2004, 1:56 PM
 */

package abc.def.ghi;

import abc.En;
import abc.def.D;
import abc.def.DAnnotType;
import  abc.def.ghi.jkl.J ;

/**
 *
 * @author  mg105252
 */
@DAnnotType(id=5)
public class H implements J {
    
    public static final D DATA1=new D(En.X);
    public static final D DATA2=new D(En.Y);
    public static final D DATA3=new D(En.Z);
    
    /** Creates a new instance of H */
    public H() {
        
        new D().methodD(En.Y);
    }

    public String getState() {
        return "idle";
    }

    public boolean setState(String text) {
        return true;
    }
    
}
