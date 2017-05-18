package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.*;
import uk.co.malbec.welcometohell.wizard.domain.TitleLarge;

import java.util.Stack;

import static java.util.Optional.ofNullable;
import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class TitleLargeParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "title-large".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        TitleLarge element = new TitleLarge();

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
