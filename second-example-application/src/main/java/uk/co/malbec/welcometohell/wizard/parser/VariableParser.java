package uk.co.malbec.welcometohell.wizard.parser;


import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.TextParser;
import uk.co.malbec.welcometohell.wizard.domain.Default;
import uk.co.malbec.welcometohell.wizard.domain.Rule;
import uk.co.malbec.welcometohell.wizard.domain.Variable;

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
