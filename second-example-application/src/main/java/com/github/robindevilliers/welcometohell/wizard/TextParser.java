package com.github.robindevilliers.welcometohell.wizard;

public interface TextParser {

    boolean accepts(Object element);

    void handle(Object element, String value);
}
