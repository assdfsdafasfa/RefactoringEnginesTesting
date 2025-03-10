/*
 * BeanC.java
 *
 * Created on May 20, 2004, 2:17 PM
 */

package org.netbeans.tests.examples.packb;
import org.netbeans.tests.examples.packc.Makable;


/**
 *
 * @author  eh103527
 */
public class BeanC extends BeanA implements Makable {
    
    boolean sign=false;
    
    org.netbeans.tests.examples.packa.Bean b;
    
    /** Creates a new instance of BeanC */
    public BeanC() {
        super();
        init();
    }
    
    public void init() {
        b=new BeanB() {
            public void doit() {
                System.out.println("done "+refID+" "+BeanC.this.protectedProperty);
            }
        };
        ((BeanB)b).abc();
    }
    
    public int getRefID() {
        return refID+100;
    }
    /**
     * Getter for property sign.
     * @return Value of property sign.
     */
    public boolean isSign() {
        return sign;
    }
    
    public org.netbeans.tests.examples.packa.Bean getBean() {
        System.out.println("getBean, property="+protectedProperty);
        return b;
    }
    
    public void setBean(org.netbeans.tests.examples.packa.Bean bean) {
        b=bean;
        protectedProperty="set";
    }
    /**
     * Setter for property sign.
     * @param sign New value of property sign.
     */
    public void setSign(boolean sign) {
        this.sign = sign;
        int d=BeanB.CODE;
        int count=(int)((refID+5)*d+(refID*refID*1.0/Math.tan((double)refID)));
        org.netbeans.tests.examples.packa.Bean bb=new BeanC();
        ((org.netbeans.tests.examples.packc.Makable)bb).prepare().make(refID, "text", this);
        ((BeanB)(((BeanC)bb).getBean())).make(refID+1, ((BeanA)bb).getProtectedProperty(), b);
        int id=bb.getRefID();
        BeanC[] cc=(BeanC[])(((org.netbeans.tests.examples.packc.Makable)bb).finish());
        protectedProperty="set";
        publicProperty="a";
        BeansD.BeanDA da=new BeansD.BeanDA("text");
        new BeanB().abc();
    }
    
    public org.netbeans.tests.examples.packa.Testable test() {
        return super.test();
    }
    
    public java.lang.String getProtectedProperty() {
        java.lang.String retValue;
        
        retValue = super.getProtectedProperty();
        return retValue;
    }
    
    public void setTestable(org.netbeans.tests.examples.packa.Testable t) {
        org.netbeans.tests.examples.packa.Testable te=t;
        te.test();
    }

    public java.lang.Object[] finish() {
        return new Object[] {protectedProperty, publicProperty};
    }

    public void make(int index, java.lang.String s, org.netbeans.tests.examples.packa.Bean bean) {
        protectedProperty+=s+publicProperty;
    }

    public org.netbeans.tests.examples.packc.Makable prepare() {
        return null;
    }
}
