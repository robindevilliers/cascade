package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ParserUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.Well;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

@Component
public class WellParser  implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "well".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Well element = new Well();

        ParserUtilities.assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
