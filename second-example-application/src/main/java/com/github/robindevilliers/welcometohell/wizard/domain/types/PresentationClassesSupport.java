package com.github.robindevilliers.welcometohell.wizard.domain.types;

import com.github.robindevilliers.welcometohell.wizard.Flavour;

public interface PresentationClassesSupport extends Element {

    Flavour getFlavour();

    void setFlavour(Flavour flavour);
}
