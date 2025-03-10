/*
 * G.java
 *
 * Created on May 24, 2004, 1:43 PM
 */

package abc.def.ghi;

import abc.A;
import abc.En;
import abc.def.DAnnotType;
import java.util.Vector;

/**
 *
 * @author  mg105252
 */
public class G {
    
    /** Creates a new instance of G */
    public G() {
        abc.A a = new abc.A();
        a.methodA(3, "hi", true);
        
        abc.def.D d = new abc.def.D();
        d.methodD(En.X);
        A<Vector> av=new A();
        av.methodA(new Vector(), "aah");
        Vector v2=new Vector();
        av.methodA(v2, "bbb");
        A<String> as=new A();
        as.methodA("s1", "s2");
        A<Long> al=new A();
        al.methodA(123l, "s2");
        
    }

    public int age = 13;
    
    /**
     * there is annotation DAnnotType
     */
    @DAnnotType(id=2, value="ghi")
    public void testA() {
        
    }
}
