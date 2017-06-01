package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ElementParser;
import com.github.robindevilliers.welcometohell.wizard.ParserUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.TitleLarge;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;

import java.util.Stack;

@Component
public class TitleLargeParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "title-large".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        TitleLarge element = new TitleLarge();

        ParserUtilities.assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
