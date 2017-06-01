package com.github.robindevilliers.welcometohell.wizard.parser;


import com.github.robindevilliers.welcometohell.wizard.ElementParser;
import com.github.robindevilliers.welcometohell.wizard.TextParser;
import com.github.robindevilliers.welcometohell.wizard.domain.Default;
import com.github.robindevilliers.welcometohell.wizard.domain.Rule;
import com.github.robindevilliers.welcometohell.wizard.domain.Variable;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;

import java.util.Stack;

@Component
public class VariableParser implements ElementParser, TextParser {
    @Override
    public boolean accepts(String name) {
        return "variable".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        Variable variable = new Variable();
        variable.name = attributes.getValue("name");


        Object parent = elements.peek();
        if (parent instanceof Rule) {
            Rule rule = (Rule) parent;
            rule.getVariables().add(variable);
        } else if (parent instanceof Default) {
            Default rule = (Default) parent;
            rule.getVariables().add(variable);
        }  else {
            throw new RuntimeException("varable must be the child of a rule or default element");
        }
        return variable;
    }

    @Override
    public boolean accepts(Object element) {
        return element instanceof Variable;
    }

    @Override
    public void handle(Object element, String value) {
        Variable variable = (Variable) element;
        if (variable.text == null) {
            variable.text = value;
        } else {
            variable.text = variable.text + value;
        }


    }
}
