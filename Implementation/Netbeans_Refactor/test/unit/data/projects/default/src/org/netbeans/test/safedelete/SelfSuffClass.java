package org.netbeans.test.refactoring.safedelete;

public class SelfSuffClass{
    
    Object refClass = new Object();
    
    public SelfSuffClass() {
    
    }

    public void referringMethod() {
        refClass.hashCode();
    }
    
}
