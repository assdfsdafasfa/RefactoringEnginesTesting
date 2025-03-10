/*
 * Makable.java
 *
 * Created on May 21, 2004, 1:52 PM
 */

package org.netbeans.tests.examples.packc;
import org.netbeans.tests.examples.packa.Bean;

/**
 *
 * @author  eh103527
 */
public interface Makable {
    
    public static final int CODE=1;
    
    public void make(int index, String s, Bean bean);
    public Makable prepare();
    public Object[] finish();
}
