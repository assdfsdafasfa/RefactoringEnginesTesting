class OriginalClass {
public int m() {
return 1;
}
public class C extends OriginalClass {
// move method to class B
public int m(B b) {
return super.m();
}
}
}
class B {
}