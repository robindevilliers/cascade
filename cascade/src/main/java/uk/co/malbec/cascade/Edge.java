package uk.co.malbec.cascade;

import uk.co.malbec.cascade.annotations.*;

public class Edge extends Thig {

    private Class[] precedingVertices;

    public Edge(Class<?> cls) {
        super(cls);
        precedingVertices = findAnnotation(Step.class, cls).value();
    }

    public Class[] getPrecedingVertices(){
        return precedingVertices;
    }
}
