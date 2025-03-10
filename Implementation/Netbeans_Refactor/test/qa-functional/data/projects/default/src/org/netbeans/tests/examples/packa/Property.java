/*
 * Property.java
 *
 * Created on May 20, 2004, 1:48 PM
 */

package org.netbeans.tests.examples.packa;

import java.util.Date;
import org.netbeans.tests.examples.packc.AbstractBean;

/**
 *
 * @author  eh103527
 */
public class Property extends AbstractBean {
    
    static String[] types={"String","int","boolean","char","long","short","float","double"};
    
    String name;
    
    String value;
    
    /** Creates a new instance of Property */
    public Property(String name, String value) {
        this.name=name;
        this.value=value;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public java.lang.String getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(java.lang.String value) {
        this.value = value;
        if (Bean.class == value.getClass()) {
            System.out.println("good");
        }
    }
    
    public void methodBean() {
        rootField=name+" "+value+" "+(new Date().toString());
        System.out.println(rootField);        
    }
    
}
