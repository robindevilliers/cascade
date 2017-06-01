package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ParserUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.Options;
import com.github.robindevilliers.welcometohell.wizard.type.Type;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

import static java.util.Optional.ofNullable;

@Component
public class OptionsParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "options".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Options element = new Options();

        element.setData(attributes.getValue("data"));
        element.setType(ofNullable(attributes.getValue("type")).map(Type::valueOf).orElse(null));

        ParserUtilities.assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
