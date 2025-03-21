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
package org.netbeans.test.extractmethod;

/**
 *
 * @author Pavel Flaska
 */
public class TestClass {

    int field = 0;
    String name;

    void someMethod() {
        int number = 10;
        System.err.println("Number: " + number);
        number = number + 3;
    }

    void anotherMethod() {
        int number = 0;
        while (field < 666) {
            field++;
            System.out.println("number = " + number++ + ", field = " + field);
        }
    }

    public static void exceptionMethod() throws java.io.IOException {
        java.io.FileInputStream fis = new java.io.FileInputStream(new java.io.File("What?"));
    }

    void flowMethod() {
        int number = 0;
        if (field == 111) {
            number = 1;
            System.out.println("number = " + number + ", field = " + field);
        } else {
            System.out.println("number = " + number + ", field = " + field);
        }
    }

    void dupVar() {
        String test = "broken";
        System.out.println(test);
        test = "test";
        System.out.println(test);
    }
}
