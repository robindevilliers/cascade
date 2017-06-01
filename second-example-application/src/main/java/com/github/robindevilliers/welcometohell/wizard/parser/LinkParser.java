package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.domain.Link;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

import static com.github.robindevilliers.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class LinkParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "link".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Link element = new Link();

        element.setView(attributes.getValue("view"));
        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
