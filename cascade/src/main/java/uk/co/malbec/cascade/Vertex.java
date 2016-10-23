package uk.co.malbec.cascade;

import uk.co.malbec.cascade.annotations.Page;
import uk.co.malbec.cascade.annotations.ReEntrantTerminator;
import uk.co.malbec.cascade.annotations.Terminator;

public class Vertex extends Thig {

    private boolean isTerminator;

    private boolean isReEntrantTerminator;

    private int reEntrantCount = 0;

    private Class[] precedingEdges;

    public Vertex(Class cls) {
        super(cls);

        precedingEdges = findAnnotation(Page.class, cls).value();

        isTerminator = findAnnotation(Terminator.class, cls) != null;

        ReEntrantTerminator reEntrantTerminator = findAnnotation(ReEntrantTerminator.class, cls);
        if (reEntrantTerminator != null) {
            isReEntrantTerminator = true;
            reEntrantCount = reEntrantTerminator.value();
        } else {
            isReEntrantTerminator = false;
        }

    }

    public boolean isTerminator() {
        return isTerminator;
    }

    public boolean isReEntrantTerminator() {
        return isReEntrantTerminator;
    }

    public long getReEntrantCount(){
        return reEntrantCount;
    }

    public Class[] getPrecedingEdges(){
        return precedingEdges;
    }

}
