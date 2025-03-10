package extensiblevisitor;

/**
 *
 */
public abstract class Visitor {
    public interface E {
        public void visitUnknown(Exp e);
    }
    
    public interface ALE extends E {
        public void visitLit(Exp.Lit l);
        public void visitAdd(Exp.Add a);
    }
    
    /** @since 2.0 */
    public interface NALE extends ALE {
        public void visitNeg(Exp.Neg n);
    }
    
    public static Visitor create(final E e) {
        return new Visitor() {
            public void visitNeg(Exp.Neg n) {
                e.visitUnknown(n);
            }

            public void visitLit(Exp.Lit l) {
                e.visitUnknown(l);
            }

            public void visitAdd(Exp.Add a) {
                e.visitUnknown(a);
            }
        };
    }
    
    public static Visitor create(final ALE e) {
        return new Visitor() {
            public void visitNeg(Exp.Neg n) {
                e.visitUnknown(n);
            }

            public void visitLit(Exp.Lit l) {
                e.visitLit(l);
            }

            public void visitAdd(Exp.Add a) {
                e.visitAdd(a);
            }
        };
    }
    
    public static Visitor create(final NALE e) {
        return new Visitor() {
            public void visitNeg(Exp.Neg n) {
                e.visitNeg(n);
            }

            public void visitLit(Exp.Lit l) {
                e.visitLit(l);
            }

            public void visitAdd(Exp.Add a) {
                e.visitAdd(a);
            }
        };
    }
    
    /** Creates a new instance of Visitor */
    private Visitor() {
    }
    
    public abstract void visitLit(Exp.Lit l);
    public abstract void visitAdd(Exp.Add a);
    
    /** @since 2.0 */
    public abstract void visitNeg(Exp.Neg n);
    
}
