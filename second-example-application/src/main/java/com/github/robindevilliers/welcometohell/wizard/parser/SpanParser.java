package com.github.robindevilliers.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;
import com.github.robindevilliers.welcometohell.wizard.domain.Span;

import java.util.Stack;

import static com.github.robindevilliers.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class SpanParser implements ElementParser {

    @Override
    public boolean accepts(String name) {
        return "span".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Span element = new Span();

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
