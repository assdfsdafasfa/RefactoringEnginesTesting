/*
 * Bean.java
 *
 * Created on May 20, 2004, 1:46 PM
 */

package org.netbeans.tests.examples.packa;

import java.lang.reflect.Method;
import java.util.HashMap;
import org.netbeans.tests.examples.packb.BeanA;
import org.netbeans.tests.examples.packb.BeansD;

/**
 *
 * @author  eh103527
 */
public class Bean {
        
    HashMap properties;
    HashMap associations;
    
    /**
     * Reference ID - refID.
     */
    protected int refID=1234;
    
    /** Creates a new instance of Bean */
    protected Bean() {
        properties=new HashMap();
        associations=new HashMap();
    }
   
    public Property[] getProperties() {
        return (Property[])(properties.keySet().toArray(new Property[properties.size()]));
    }
    
    public Bean abc() {
        BeanA a=new BeansD.BeanDA("text");
        setA((BeansD.BeanDA)a);
        Bean b=new org.netbeans.tests.examples.packb.BeanB();
        return a;
    }
    
    public void setA(BeansD.BeanDA aa) {
        System.out.println("bye");
    }
    
    /**
     * Returns refID.
     * @return refID
     */
    public int getRefID() {
        return refID;
    }
}
