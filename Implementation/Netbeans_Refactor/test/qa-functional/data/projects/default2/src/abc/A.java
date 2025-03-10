/*
 * A.java
 *
 * Created on May 24, 2004, 9:36 AM
 */

package abc;

import abc.def.DAnnotType;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import static java.lang.String.*;
/**
 * class is annotated by DAnnotType
 * @author  mg105252
 */
@DAnnotType(id=5)
public class A<T> {
    
    private T number;
    public String text = "text";
    protected boolean test = false;
    
    public List<String> list;
    ArrayList<T> data;
    
    /** Creates a new instance of A */
    public A() {
        data=new ArrayList();
        methodA(number, text, false);
    }
    
    /** Creates a new instance of A */
    public A(T i) {
        
    }
    
    /** Creates a new instance of A */
    public A(T i, String text) {
        
    }
    
    public T methodA() {
        return null;
    }
    
    /**
     * There is used enumeration En
     */
    private void methodA(T i) {
        list.get(0);
        
        En a = En.X;
        
        System.err.println("a=" + a);
        for (T t : data) {
            if (t == i) {
                return;
            }
        }
        data.add(i);
    }
    
    public void methodA(T i, String text) {
        methodA(i);
        methodA(i, text, true, "test1", "test2", "test3", "test4");
        methodA(i, text, true, "test1");
        methodA(i, text, true);
        list.add(text);
        Collections.sort(list, CASE_INSENSITIVE_ORDER);
    }
    
    public void methodA(T i, String text, boolean b, String ... var) {
        methodA(i);
    }
    
    
}
