 class A {
    private Integer i = 0;

    void foo() {
        i.toString();
    }
}

 class B extends A {
    void bar() {
        foo(); // inline foo
    }
}