package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.*;
import uk.co.malbec.welcometohell.wizard.type.Type;
import uk.co.malbec.welcometohell.wizard.domain.Options;

import java.util.Stack;

import static java.util.Optional.ofNullable;
import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

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

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
