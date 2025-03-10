/*
 * BeanA.java
 *
 * Created on May 20, 2004, 2:12 PM
 */

package org.netbeans.tests.examples.packb;

import org.netbeans.tests.examples.packa.Bean;

/**
 *
 * @author  eh103527
 */
public class BeanA extends Bean implements org.netbeans.tests.examples.packa.Testable {
    
    String privateProperty;
    
    protected String protectedProperty;
    
    public String publicProperty;
    
    /** Creates a new instance of BeanA */
    public BeanA() {
        refID=2345;
        System.out.println("Construct");
    }
    
    /**
     * Getter for property privateProperty.
     * @return Value of property privateProperty.
     */
    public java.lang.String getPrivateProperty() {
        return privateProperty;
    }
    
    public int getRefID() {
        return refID+10;
    }
    /**
     * Setter for property privateProperty.
     * @param privateProperty New value of property privateProperty.
     */
    public void setPrivateProperty(java.lang.String privateProperty) {
        this.privateProperty = privateProperty;
    }
    
    /**
     * Getter for property protectedProperty.
     * @return Value of property protectedProperty.
     */
    public java.lang.String getProtectedProperty() {
        return protectedProperty;
    }
    
    /**
     * Setter for property protectedProperty.
     * @param protectedProperty New value of property protectedProperty.
     */
    public void setProtectedProperty(java.lang.String protectedProperty) {
        this.protectedProperty = protectedProperty;
    }
    
    /**
     * Getter for property publicProperty.
     * @return Value of property publicProperty.
     */
    public java.lang.String getPublicProperty() {
        return publicProperty;
    }
    
    /**
     * Setter for property publicProperty.
     * @param publicProperty New value of property publicProperty.
     */
    public void setPublicProperty(java.lang.String publicProperty) {
        this.publicProperty = publicProperty;
    }
    
    public org.netbeans.tests.examples.packa.Testable test() {
        privateProperty=publicProperty+protectedProperty;
        return this;
    }
    
    public org.netbeans.tests.examples.packa.Testable[] usages(java.util.List list) {
        return null;
    }
    
    public void prepare(Object[] data) {
        
    }
}
