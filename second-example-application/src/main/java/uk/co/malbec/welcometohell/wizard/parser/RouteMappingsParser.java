package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.RouteMappings;
import uk.co.malbec.welcometohell.wizard.domain.View;

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
