package org.netbeans.test.encapsulate;

public class Encapsulate {
    int a;
    int a1;
    float b[]=new float[1];
    float b1;
    Encapsulate x;
    Encapsulate x1;

    void xxx() {
       a=1;
       b[0]=5;
       x=null;
    }

    void yyy() {
        x1=x;
        a1=a;
        b1=b[0];
    }

    Encapsulate  pp() {
        return x;
    }
 
    void gggg() {
        a1=this.a;
        a1=5+this.a-5;
    }

    void zzzz() {
       a1=pp().a;
    }
}

