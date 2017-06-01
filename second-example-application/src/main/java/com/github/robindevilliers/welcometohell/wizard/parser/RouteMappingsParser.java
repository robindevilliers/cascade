package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.domain.RouteMappings;
import com.github.robindevilliers.welcometohell.wizard.domain.View;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

@Component
public class RouteMappingsParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "route-mappings".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        RouteMappings routeMappings = new RouteMappings();

        if (elements.peek() instanceof View){
            View view = (View) elements.peek();
            view.setRouteMappings(routeMappings);
        }

        return routeMappings;
    }
}
