package uk.co.malbec.welcometohell.wizard.domain.types;

import uk.co.malbec.welcometohell.wizard.Alignment;
import uk.co.malbec.welcometohell.wizard.Justification;
import uk.co.malbec.welcometohell.wizard.Orientation;
import uk.co.malbec.welcometohell.wizard.Wrap;

public interface FlexContainerSupport {

    Orientation getOrientation();

    void setOrientation(Orientation orientation);

    Justification getJustifyContent();

    void setJustifyContent(Justification justifyContent);

    Alignment getAlignItems();

    void setAlignItems(Alignment alignItems);

    Wrap getWrap();

    void setWrap(Wrap wrap);

    Alignment getAlignContent();

    void setAlignContent(Alignment alignContent);
}
