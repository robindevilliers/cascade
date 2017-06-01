package com.github.robindevilliers.welcometohell.wizard.domain.types;

import com.github.robindevilliers.welcometohell.wizard.Alignment;
import com.github.robindevilliers.welcometohell.wizard.Justification;

public interface FlexItemSupport {

    Alignment getAlignSelf();

    Justification getJustifySelf();

    String getFlex();

    void setJustifySelf(Justification justifySelf);

    void setAlignSelf(Alignment alignSelf);

    void setFlex(String flex);

}
