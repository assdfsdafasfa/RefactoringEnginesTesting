class Foo {
    static class Bar {
        static class X {
            static void method() {
                var x = new X();
                System.out.println(x);
            }
        }
    }
}