package com.github.robindevilliers.welcometohell.wizard.domain;

import com.github.robindevilliers.welcometohell.FontWeight;
import com.github.robindevilliers.welcometohell.wizard.*;
import com.github.robindevilliers.welcometohell.wizard.domain.types.FlexItemSupport;
import com.github.robindevilliers.welcometohell.wizard.domain.types.PresentationClassesSupport;
import com.github.robindevilliers.welcometohell.wizard.domain.types.TextStyleSupport;

public class TitleSmall extends ElementComposer implements PresentationClassesSupport,
        TextStyleSupport,
        FlexItemSupport {

    private Flavour flavour;

    private String rem;
    private String em;
    private TextDecoration textDecoration;
    private FontStyle fontStyle;
    private FontWeight fontWeight;

    private Alignment alignSelf;
    private Justification justifySelf;
    private String flex;

    public Flavour getFlavour() {
        return flavour;
    }

    public void setFlavour(Flavour flavour) {
        this.flavour = flavour;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    @Override
    public TextDecoration getTextDecoration() {
        return textDecoration;
    }

    @Override
    public void setTextDecoration(TextDecoration textDecoration) {
        this.textDecoration = textDecoration;
    }

    @Override
    public FontWeight getFontWeight() {
        return fontWeight;
    }

    @Override
    public void setFontWeight(FontWeight fontWeight) {
        this.fontWeight = fontWeight;
    }

    @Override
    public FontStyle getFontStyle() {
        return fontStyle;
    }

    @Override
    public void setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }

    @Override
    public Alignment getAlignSelf() {
        return alignSelf;
    }

    @Override
    public Justification getJustifySelf() {
        return justifySelf;
    }

    @Override
    public String getFlex() {
        return flex;
    }

    @Override
    public void setJustifySelf(Justification justifySelf) {
        this.justifySelf = justifySelf;
    }

    @Override
    public void setAlignSelf(Alignment alignSelf) {
        this.alignSelf = alignSelf;
    }

    @Override
    public void setFlex(String flex) {
        this.flex = flex;
    }


}
