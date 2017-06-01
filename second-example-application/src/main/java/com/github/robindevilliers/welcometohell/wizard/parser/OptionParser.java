package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ParserUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.Option;
import com.github.robindevilliers.welcometohell.wizard.domain.Options;
import com.github.robindevilliers.welcometohell.wizard.type.Serialization;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

@Component
public class OptionParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "option".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Option element = new Option();

        Options options = elements
                .stream()
                .filter(e -> e instanceof Options)
                .reduce((lhs, rhs) -> rhs)
                .map(Options.class::cast)
                .orElseThrow(() -> new RuntimeException("Option must be a descendant of Options."));

        element.setData(options.getData());
        element.setType(options.getType());
        element.setValue(Serialization.deserialize(options.getType(), attributes.getValue("value")));

        ParserUtilities.assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);

        return element;
    }
}
