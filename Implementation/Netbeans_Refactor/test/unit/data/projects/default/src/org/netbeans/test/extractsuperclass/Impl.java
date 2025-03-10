package org.netbeans.test.extractsuperclass;

public class Impl {
    
    public Impl() {
    }
    
    public int generateNumber(int x, int y) {
        return x * y % 100;
    }
    
    public int sumNumbers(int x, int y) {
        return x + y;
    }
    
    private void waitAMinute() {
        generateNumber(9,9);
        sumNumbers(2,2);
    }
    
    protected final void jump() {
    }
    
     public void beep() {
    }
    
}
