class A {
void m(Object m){
System.out.println("A");
}
}
class B extends A{
// rename k to m
void k(String m){
System.out.println("B");
}
void f(){
m("1");
}
}