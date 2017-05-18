package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.Link;

import java.util.Stack;

import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class LinkParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "link".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Link element = new Link();

        element.setView(attributes.getValue("view"));
        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
