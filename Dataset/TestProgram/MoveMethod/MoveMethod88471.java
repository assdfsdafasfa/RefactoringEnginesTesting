interface MyInterface {
}

class MyClass implements MyInterface {
}

class MoveInstanceBug1 {
    private MyInterface myInterface;

    public void doSomething() {
    }
    
    public void callDoSomething() {
        doSomething();
    }
}