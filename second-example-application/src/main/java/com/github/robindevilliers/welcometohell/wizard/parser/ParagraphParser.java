package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ElementParser;
import com.github.robindevilliers.welcometohell.wizard.ParserUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.Paragraph;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;

import java.util.Stack;

@Component
public class ParagraphParser implements ElementParser {

    @Override
    public boolean accepts(String name) {
        return "paragraph".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Paragraph element = new Paragraph();

        ParserUtilities.assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
