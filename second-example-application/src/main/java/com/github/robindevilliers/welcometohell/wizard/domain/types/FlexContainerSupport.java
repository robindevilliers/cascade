package com.github.robindevilliers.welcometohell.wizard.domain.types;

import com.github.robindevilliers.welcometohell.wizard.Alignment;
import com.github.robindevilliers.welcometohell.wizard.Orientation;
import com.github.robindevilliers.welcometohell.wizard.Wrap;
import com.github.robindevilliers.welcometohell.wizard.Justification;

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
