package uk.co.malbec.welcometohell.wizard.domain;

import uk.co.malbec.welcometohell.wizard.domain.types.Element;

public class Text implements Element {

    public String text;

    public Text(String text) {
        this.text = text;
    }
}
