package com.github.robindevilliers.welcometohell.wizard.domain;

import com.github.robindevilliers.welcometohell.wizard.domain.types.Element;

public class Text implements Element {

    public String text;

    public Text(String text) {
        this.text = text;
    }
}
