package com.github.robindevilliers.welcometohell.wizard.domain.types;

import com.github.robindevilliers.welcometohell.wizard.TextDecoration;
import com.github.robindevilliers.welcometohell.FontWeight;
import com.github.robindevilliers.welcometohell.wizard.FontStyle;

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
