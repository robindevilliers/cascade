package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.ElementComposer;
import com.github.robindevilliers.welcometohell.wizard.TextParser;
import com.github.robindevilliers.welcometohell.wizard.domain.Text;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.wizard.domain.types.Element;

@Component
public class ElementComposerParser implements TextParser {
    @Override
    public boolean accepts(Object element) {
        return element instanceof ElementComposer;
    }

    @Override
    public void handle(Object element, String value) {
        ElementComposer elementComposer = (ElementComposer) element;
        if (elementComposer.getElements().isEmpty()) {
            elementComposer.getElements().add(new Text(value));
        } else {
            Element last = elementComposer.getElements().get(elementComposer.getElements().size() - 1);
            if (last instanceof Text) {
                elementComposer.getElements().remove(elementComposer.getElements().size() - 1);
                elementComposer.getElements().add(new Text(((Text) last).text + value));
            } else {
                elementComposer.getElements().add(new Text(value));
            }
        }
    }
}
