package uk.co.malbec.welcometohell.wizard.domain.types;

import uk.co.malbec.welcometohell.wizard.Alignment;
import uk.co.malbec.welcometohell.wizard.Justification;

public interface FlexItemSupport {

    Alignment getAlignSelf();

    Justification getJustifySelf();

    String getFlex();

    void setJustifySelf(Justification justifySelf);

    void setAlignSelf(Alignment alignSelf);

    void setFlex(String flex);

}
