package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.Div;

import java.util.Stack;

import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

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
