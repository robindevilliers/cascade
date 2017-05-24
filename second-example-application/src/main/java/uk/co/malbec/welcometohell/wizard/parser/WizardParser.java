package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.Wizard;

import java.util.Stack;

@Component
public class WizardParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "wizard".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Wizard wizard = new Wizard();

        wizard.setId(attributes.getValue("id"));
        wizard.setStart(attributes.getValue("start"));

        wizard.setRem(attributes.getValue("rem"));
        wizard.setEm(attributes.getValue("em"));

        return wizard;
    }


}
