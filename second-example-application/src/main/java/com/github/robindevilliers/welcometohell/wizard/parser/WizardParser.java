package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.domain.Wizard;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

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
