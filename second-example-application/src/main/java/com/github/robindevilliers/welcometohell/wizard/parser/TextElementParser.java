package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.TextElement;
import com.github.robindevilliers.welcometohell.wizard.TextParser;
import org.springframework.stereotype.Component;

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
