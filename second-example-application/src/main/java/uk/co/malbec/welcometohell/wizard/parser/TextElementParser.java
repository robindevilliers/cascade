package uk.co.malbec.welcometohell.wizard.parser;

import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.wizard.TextParser;
import uk.co.malbec.welcometohell.wizard.TextElement;

@Component
public class TextElementParser implements TextParser {
    @Override
    public boolean accepts(Object element) {
        return element instanceof TextElement;
    }

    @Override
    public void handle(Object element, String value) {
        TextElement textElement = (TextElement) element;

        textElement.setText(value);
    }
}
