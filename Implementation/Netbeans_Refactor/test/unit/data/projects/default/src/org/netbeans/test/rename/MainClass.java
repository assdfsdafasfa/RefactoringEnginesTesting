/*
 * MainClass.java
 *
 * Created on 18 January 2004, 21:15
 */

package org.netbeans.test.rename;

/**
 *
 * @author  Dan
 */
public class MainClass {
    
    public static MainClass lastInstance = null;
    public static int MAX = 20;
    public static int count = 0;
    
    int number = MAX / 2;
    int square = number * (number);
    private String name = "Name";
    public String fullName = "full" + name;
    
    static {
        int count = 5;
        MainClass.count += count;
        System.out.println(MAX);
        System.out.println(lastInstance);
        System.out.println(MainClass.count);
    }
    
    {
        System.out.println(number);
        System.out.println(name);
        System.out.println(fullName);
        System.out.println(MAX);
        System.out.println(count);
        System.out.println(lastInstance);
    }
    
    public MainClass() {
    }
    
    public MainClass(int x, String s) {
        number = x;
        for (; x < MAX; x -= count) {
            System.out.println(lastInstance);
        }
        lastInstance = this;
    }
    
    public int getValue (char c, int n2) {
        int result = number;
        switch (c) {
            case 'a':
            case 'b':
                System.out.println("debug info ...");
                result = (int) Math.sqrt(number) + result;
                break;
            case 'c':
                result = number - 1;
                break;
            default:
                result = result + number + (count + MAX) % 23;
        }
        if (lastInstance instanceof MainClass) {
            System.out.println(lastInstance.getClass());
        } else {
            System.out.println("null");
        }
        new InnerClass("baf").go();
        return result;
    }
    
    public void go() {
        number = getValue('w', 1);
        number = getValue('e', 2);
        System.out.println(getValue('r', getValue('t', 0)));
    }
    
    public static void main (String[] args) {
        org.netbeans.test.rename.MainClass inst = new MainClass ();
        if (MAX > 30)
            inst = new MainClass(2, "ahoy!");
        inst.go();
    }
    
    public class InnerClass extends MainClass {
        
        public int num = MAX * MAX;
        
        public InnerClass(String s) {
            super(0, s);
        }
        
        public void go() {
            MainClass.this.number += num;
            MainClass.this.go();
            getValue('a', getValue('b', 7));
        }
    }
}
