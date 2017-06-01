package com.github.robindevilliers.welcometohell.wizard.parser;

import com.github.robindevilliers.welcometohell.wizard.domain.Question;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import com.github.robindevilliers.welcometohell.wizard.ElementParser;

import java.util.Stack;

import static com.github.robindevilliers.welcometohell.wizard.ParserUtilities.assignAttributes;

@Component
public class QuestionParser implements ElementParser {
    @Override
    public boolean accepts(String name) {
        return "question".equals(name);
    }

    @Override
    public Object parse(Stack<Object> elements, Attributes attributes) {

        Question element = new Question();

        assignAttributes(element, attributes);

        linkToParent(elements.peek(), element);
        return element;
    }
}
