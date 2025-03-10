package refactoring.test.junit;

public class JunitTest {
	// Test Case : Test methodToMove
	public void testMethodToMove() {
		SourceClass sourceClass = new SourceClass();
		sourceClass.methodToMove();
	}
}

class SourceClass {
	public TargetClass target;

	// move method "methodToMove" to "TargetClass"
	public void methodToMove() {
		test();
	}

	public void test() {
	}

}

// Target class
class TargetClass {
	public void doSomething() {
		// Method logic
	}
}
