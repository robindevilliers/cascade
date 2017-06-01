package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.domain.Input;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;
import com.github.robindevilliers.welcometohell.wizard.type.Type;

import java.util.Stack;

import static java.util.Optional.ofNullable;
import static com.github.robindevilliers.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class InputParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "input".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Input element = new Input();

        assignAttributes(element, attributes);

        element.setData(attributes.getValue("data"));
        element.setType(ofNullable(attributes.getValue("type")).map(Type::valueOf).orElse(null));

        linkToParent(elements.peek(), element);
        return element;
    }
}
