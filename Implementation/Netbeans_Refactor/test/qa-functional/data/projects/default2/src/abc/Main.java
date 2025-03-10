/*
 * Main.java
 *
 * Created on May 20, 2004, 4:28 PM
 */

package abc;

import abc.def.F;



/**
 *
 * @author  mg105252
 */
public class Main {
    
    private C c;
    
    
    /** Creates a new instance of Main */
    public Main() {
        new A<String>();
        c = new C(new abc.def.ghi.I () {
            public String getState() {
                return "idle";
            };     
        });

        F f = new F();       
        F.PublicInnerClass pic = f.new PublicInnerClass();
        F.PublicStaticInnerClass psic = new F.PublicStaticInnerClass();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic herAe
        new Main();
    }    
    
}
