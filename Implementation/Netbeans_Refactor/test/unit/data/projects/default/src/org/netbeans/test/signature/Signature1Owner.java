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
 * Signature1Owner.java
 *
 * Created on January 15, 2004, 10:55 AM
 */

package org.netbeans.test.signature;

import java.lang.IndexOutOfBoundsException;
import java.util.List;

/**
 *
 * @author  Pavel Flaska
 */
public class Signature1Owner {

    /** Creates a new instance of Signature1Owner */
    public Signature1Owner() {
    }

    protected String signatureMethod(List l, Object o, int i) throws IndexOutOfBoundsException {
        o = l.get(i);
        return "Assigned " + i + "th element to second parameter.";
    }

}
