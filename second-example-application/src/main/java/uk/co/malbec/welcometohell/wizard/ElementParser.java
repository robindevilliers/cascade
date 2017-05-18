package uk.co.malbec.welcometohell.wizard;

import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.wizard.domain.types.Element;

import java.util.Stack;

public interface ElementParser {

    boolean accepts(String name);

    Object parse(Stack<Object> elements, Attributes attributes);

    default void linkToParent(Object parent, Element element) {
        if (parent instanceof ElementComposer) {
            ElementComposer elementComposer = (ElementComposer) parent;
            elementComposer.getElements().add(element);
        } else {
            throw new RuntimeException(element.getClass().getSimpleName() + " must be the child of a element composer");
        }
    }
}
