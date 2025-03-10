/*
 * DAnnotType.java
 *
 * Created on January 4, 2005, 11:34 AM
 */

package abc.def;

import abc.En;

/**
 * This is a comment for DAnnotType.
 * @author eh103527
 */
public @interface DAnnotType {
    int id();
    String value() default "<empty>";
    En coord() default En.X;
}
