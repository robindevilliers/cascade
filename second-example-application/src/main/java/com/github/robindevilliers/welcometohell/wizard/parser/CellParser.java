package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ParserUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.Cell;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

@Component
public class CellParser  implements ElementParser {

    @Override
    public boolean accepts(String name) {
        return "cell".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Cell element = new Cell();

        ParserUtilities.assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}