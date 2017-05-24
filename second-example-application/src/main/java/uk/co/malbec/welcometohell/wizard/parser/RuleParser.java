package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.RouteMappings;
import uk.co.malbec.welcometohell.wizard.domain.Rule;

import java.util.Stack;

@Component
public class RuleParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "rule".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Rule rule = new Rule();

        rule.setView(attributes.getValue("view"));
        rule.setCondition(attributes.getValue("condition"));

        Object parent = elements.peek();
        if (parent instanceof RouteMappings) {
            RouteMappings mappings = (RouteMappings) parent;
            mappings.getRules().add(rule);
        } else {
            throw new RuntimeException("default must be the child of route-mappings");
        }
        return rule;
    }
}
