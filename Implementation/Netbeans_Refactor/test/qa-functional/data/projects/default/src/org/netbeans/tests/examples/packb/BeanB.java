/*
 * BeanB.java
 *
 * Created on May 20, 2004, 2:14 PM
 */

package org.netbeans.tests.examples.packb;

import java.util.Vector;
import org.netbeans.tests.examples.packa.Bean;
import org.netbeans.tests.examples.packa.Testable;

import org.netbeans.tests.examples.packc.Makable;

/**
 *
 * @author  eh103527
 */
public class BeanB extends Bean implements org.netbeans.tests.examples.packc.Makable {
    
    int index=0;
    
    Vector beansa=new Vector();
    
    /** Creates a new instance of BeanB */
    public BeanB() {
        refID=473542*Makable.CODE;
    }
    
    public void addBeanA(BeanA ba) {
        beansa.add(ba);
        ((Testable)new BeansD.BeanDA("1")).test();
        new BeansD.BeanDA("1").test();
        ba.protectedProperty+=5;
    }
    
    public BeanA getBeanA(int indx) {
        return (BeanA)(beansa.get(indx));
    }
    
    public Bean[] getBeans() {
        return (Bean[])(beansa.toArray(new Bean[beansa.size()]));
    }
    
    /**
     * Getter for property index.
     * @return Value of property index.
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * Setter for property index.
     * @param index New value of property index.
     */
    public void setIndex(int index) {
        this.index = index;
    }
    
    public Object[] finish() {
        return new Object[CODE];
    }
    
    public Makable prepare() {
        System.out.println("do");
        BeanC c=new BeanC();
        c.prepare().make(10, "run", this);
        ((Bean)c).abc();
        return this;
    }
    
    public void make(int index, java.lang.String s, org.netbeans.tests.examples.packa.Bean bean) {
    }
    
}
