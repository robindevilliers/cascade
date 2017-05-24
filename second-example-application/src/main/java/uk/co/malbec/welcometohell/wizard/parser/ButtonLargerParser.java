package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.ButtonLarge;

import java.util.Stack;

import static uk.co.malbec.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class ButtonLargerParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "button-large".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        ButtonLarge element = new ButtonLarge();

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
