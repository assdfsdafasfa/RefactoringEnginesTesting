package log;

public class TestRef {

}

class A {
	// Original method m() in Class A
	B b;

	void m() {
		System.out.println("Method m() in Class A");
	}
}

class B extends A {
	// Override method m() from Class A
	@Override
	void m() {
		System.out.println("Method m() in Class B");
	}

	void callSuperM() {
		// Calls the overridden method in Class A
		super.m();
	}
}
