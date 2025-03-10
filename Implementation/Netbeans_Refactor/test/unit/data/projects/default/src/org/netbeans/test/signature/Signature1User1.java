/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
/*
 * Signature1User1.java
 *
 * Created on January 15, 2004, 10:56 AM
 */

package org.netbeans.test.signature;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  Pavel Flaska
 */
public class Signature1User1 {

    Signature1Owner owner;
    List l;

    /** Creates a new instance of Signature1User1 */
    public Signature1User1() {
        owner = new Signature1Owner();
        l = new ArrayList();
        l.add(new String("Kaja"));
        l.add(new String("Arnost"));
        l.add(new String("Pepa"));
        l.add(new String("Martin"));
        l.add(new String("Tonda"));
        l.add(new String("Jenda"));
        l.add(new String("Peta"));
        l.add(new String("Jarda"));
        l.add(new String("Dan"));
        l.add(new String("Honzik"));
    }
    
    // method usage in method body 
    public void getFifth() {
        Object boy = null;
        String resultText = owner.signatureMethod(l, boy, 5);
    }
    
}
