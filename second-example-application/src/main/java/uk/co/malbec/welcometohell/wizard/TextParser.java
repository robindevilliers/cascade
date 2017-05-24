package uk.co.malbec.welcometohell.wizard;

public interface TextParser {

    boolean accepts(Object element);

    void handle(Object element, String value);
}
