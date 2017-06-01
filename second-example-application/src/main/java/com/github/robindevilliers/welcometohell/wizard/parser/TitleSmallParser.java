package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ParserUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.TitleSmall;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

@Component
public class TitleSmallParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "title-small".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        TitleSmall element = new TitleSmall();

        ParserUtilities.assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
