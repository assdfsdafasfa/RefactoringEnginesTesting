package extensiblevisitor;

/** Interfaces for definition of expressions.
 */
public interface Exp {
    public void accept(Visitor visitor);
    
    
    public interface Lit extends Exp {
        public int getValue();
    }
    
    public interface Add extends Exp {
        public Exp getLeft();
        public Exp getRight();
    }
    
    /** This interface is added in later, not the first version */
    public interface Neg extends Exp {
        public Exp getExp();
    }
}
