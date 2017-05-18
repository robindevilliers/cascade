package uk.co.malbec.welcometohell.wizard.domain.types;

import uk.co.malbec.welcometohell.FontWeight;
import uk.co.malbec.welcometohell.wizard.FontStyle;
import uk.co.malbec.welcometohell.wizard.TextDecoration;

public interface TextStyleSupport {

    String getRem();

    String getEm();

    void setRem(String rem);

    void setEm(String em);

    TextDecoration getTextDecoration();

    void setTextDecoration(TextDecoration textDecoration);

    FontWeight getFontWeight();

    void setFontWeight(FontWeight fontWeight);

    FontStyle getFontStyle();

    void setFontStyle(FontStyle fontStyle);
}
