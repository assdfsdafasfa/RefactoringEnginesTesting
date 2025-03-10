@FunctionalInterface
interface I1 {
	int foo(int x);	
}

 class X {
	public static final I1 a= (int x) -> x;
	
	void fun1() {
		int n = a.foo(0); // [1] Inline "a" => AFE	
		
		I1 i= (int x) -> { return x; }; // [2] Inline "i"
		I1 i1= x -> i.foo(x); // => Invalid inlining of "i"
		fun2(i); // => Valid inlining of "i"
	}
	
	void fun2(I1 i) {}
}