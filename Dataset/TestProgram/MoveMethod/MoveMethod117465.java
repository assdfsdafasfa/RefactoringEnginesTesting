class Test1 {
        public void methodToMove() {}
        private TestTarget target;
        public TestTarget getTestTarget() { return target; }
}
class TestTarget {}
class Test2 {
        public Test2() {
                new Test1().methodToMove();
}}