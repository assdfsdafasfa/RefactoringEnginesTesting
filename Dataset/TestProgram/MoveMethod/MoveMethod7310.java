class OriginalClass {
TargetClass c;
protected class ProtectedInnerClass {
void innerMethod() {
// Some logic here
}
}

public void myMethod() {
    ProtectedInnerClass inner = new ProtectedInnerClass();
    inner.innerMethod();
}
}
// TargetClass.java
class TargetClass {
}