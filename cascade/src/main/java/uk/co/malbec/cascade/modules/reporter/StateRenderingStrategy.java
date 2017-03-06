package uk.co.malbec.cascade.modules.reporter;

public interface StateRenderingStrategy {
    boolean accept(Object value);
    String render(Object value);
}
