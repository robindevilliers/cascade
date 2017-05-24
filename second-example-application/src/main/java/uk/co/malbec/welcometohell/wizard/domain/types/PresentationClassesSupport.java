package uk.co.malbec.welcometohell.wizard.domain.types;

import uk.co.malbec.welcometohell.wizard.Flavour;

public interface PresentationClassesSupport extends Element {

    Flavour getFlavour();

    void setFlavour(Flavour flavour);
}
