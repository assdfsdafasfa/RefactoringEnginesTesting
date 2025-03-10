/*
 * Association.java
 *
 * Created on May 20, 2004, 1:49 PM
 */

package org.netbeans.tests.examples.packa;

import java.util.Date;
import org.netbeans.tests.examples.packc.AbstractBean;

/**
 *
 * @author  eh103527
 */
public class Association extends AbstractBean {
    
    protected Bean other;
    
    protected String name;
    
    /** Creates a new instance of Association */
    public Association(String name, Bean other) {
        this.name=name;
        this.other=other;
    }
    
    public void methodBean() {
        rootField=name+(new Date().toString());
        System.out.println(rootField);
    }
    
}
