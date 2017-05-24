package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.Option;
import uk.co.malbec.welcometohell.wizard.domain.Options;
import uk.co.malbec.welcometohell.wizard.type.Serialization;

import java.util.Stack;

import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

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

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);

        return element;
    }
}
