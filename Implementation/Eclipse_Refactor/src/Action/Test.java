package Action;

public class Test {
    void k(int primitive) {}
    void k(Integer reference) {}
    
    void use() {
        k(12);
        k(13);
    }
}