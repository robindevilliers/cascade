package com.github.robindevilliers.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;
import com.github.robindevilliers.welcometohell.wizard.domain.Div;

import java.util.Stack;

import static com.github.robindevilliers.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class DivParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "div".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Div element = new Div();

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
