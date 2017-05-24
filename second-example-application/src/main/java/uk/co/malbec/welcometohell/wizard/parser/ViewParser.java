package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.ElementParser;
import uk.co.malbec.welcometohell.wizard.domain.View;
import uk.co.malbec.welcometohell.wizard.domain.Wizard;

import java.util.Stack;

@Component
public class ViewParser implements ElementParser {

    @Override
    public boolean accepts(String name) {
        return "view".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {
        View view = new View();

        view.setId(attributes.getValue("id"));
        view.setTitle(attributes.getValue("title"));
        view.setRem(attributes.getValue("rem"));
        view.setEm(attributes.getValue("em"));

        view.setExternalName(attributes.getValue("externalName"));

        if (elements.peek() instanceof Wizard) {
            Wizard wizard = (Wizard) elements.peek();
            wizard.getViews().add(view);
        } else {
            throw new RuntimeException("view must be the child of a wizard");
        }
        return view;
    }
}
