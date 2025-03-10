/*
 * Testable.java
 *
 * Created on May 21, 2004, 2:17 PM
 */

package org.netbeans.tests.examples.packa;

import org.netbeans.tests.examples.packc.Makable;

/**
 *
 * @author  eh103527
 */
public interface Testable {
    
    public static final int MATCH=Makable.CODE;
    
    public void prepare(Object[] data);    
    public Testable test();
    public Testable[] usages(java.util.List list);
}
