/*
 * Test.java
 *
 * Created on May 27, 2004, 4:25 PM
 */

package org.netbeans.tests.examples;

/**
 *
 * @author  eh103527
 */
public class Test {
    
    static Test running=null;
    
    /**
     * Name of test - String.
     */
    String name;
    
    /** Creates a new instance of Test */
    public Test(String name) {
        this.name=name;
    }
    
    public void runTest() {
        if (running == null) {
            System.out.println("runTest "+name);
            running=this;
            try {
                Thread.sleep(20000);
            } catch (Exception ex) {
            }
            running=null;
        }
    }
    
    public static Test getRunning() {
        return running;
    }
    
    public static void makeTest() {
        Test t=new Test("empty");
        t.runTest();
    }
    
    public void test1() {
        int a=0;
        for (int i=0;i < 10;i++) {
            a+=i;
        }
        System.out.println(a);
    }
}
