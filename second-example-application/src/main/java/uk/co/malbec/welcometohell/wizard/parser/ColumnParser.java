package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.Column;

import java.util.Stack;

import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class ColumnParser implements ElementParser {

    @Override
    public boolean accepts(String name) {
        return "column".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Column element = new Column();

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}