package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.Well;

import java.util.Stack;

import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class WellParser  implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "well".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Well element = new Well();

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
