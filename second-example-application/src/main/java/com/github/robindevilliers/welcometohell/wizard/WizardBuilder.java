package com.github.robindevilliers.welcometohell.wizard;

import com.github.robindevilliers.welcometohell.wizard.domain.Wizard;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static java.util.Arrays.copyOfRange;

@Component
public class WizardBuilder extends DefaultHandler implements ApplicationContextAware {

    private Stack<Object> elements = new Stack<>();

    private List<ElementParser> elementParsers;

    private List<TextParser> textParsers;

    private Wizard wizard;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        for (ElementParser elementParser : elementParsers) {
            if (elementParser.accepts(qName)) {
                Object element = elementParser.parse(elements, attributes);
                elements.push(element);

                if ("wizard".equals(qName)) {
                    this.wizard = (Wizard) element;
                }

                return;
            }
        }

        throw new RuntimeException("unhandled tag " + qName);
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elements.pop();
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String value = new String(copyOfRange(ch, start, start + length));

        if (!StringUtils.isBlank(value)) {
            Object element = elements.peek();

            for (TextParser textParser : this.textParsers) {
                if (textParser.accepts(element)) {
                    textParser.handle(element, value);
                    return;
                }
            }
            throw new RuntimeException("text [" + value + "] is in an invalid position");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ElementParser> elementParsers = applicationContext.getBeansOfType(ElementParser.class);
        this.elementParsers = new ArrayList<>(elementParsers.values());

        Map<String, TextParser> textParsers = applicationContext.getBeansOfType(TextParser.class);
        this.textParsers = new ArrayList<>(textParsers.values());
    }

    public Wizard getWizard() {
        return wizard;
    }
}
