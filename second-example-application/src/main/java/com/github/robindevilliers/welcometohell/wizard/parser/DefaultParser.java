package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.domain.Default;
import com.github.robindevilliers.welcometohell.wizard.domain.RouteMappings;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

@Component
public class DefaultParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "default".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Default def = new Default();

        def.setView(attributes.getValue("view"));

        Object parent = elements.peek();
        if (parent instanceof RouteMappings) {
            RouteMappings mappings = (RouteMappings) parent;
            mappings.setDefault(def);
        } else {
            throw new RuntimeException("default must be the child of route-mappings");
        }
        return def;
    }
}
