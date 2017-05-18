package uk.co.malbec.welcometohell.wizard.domain;

import uk.co.malbec.welcometohell.FontWeight;
import uk.co.malbec.welcometohell.wizard.*;
import uk.co.malbec.welcometohell.wizard.domain.types.FlexItemSupport;
import uk.co.malbec.welcometohell.wizard.domain.types.PresentationClassesSupport;
import uk.co.malbec.welcometohell.wizard.domain.types.TextStyleSupport;

public class Link extends TextElement implements PresentationClassesSupport,
        TextStyleSupport,
        FlexItemSupport {

    private String view;

    private Flavour flavour;

    private String em;
    private String rem;
    private TextDecoration textDecoration;
    private FontStyle fontStyle;
    private FontWeight fontWeight;

    private Alignment alignSelf;
    private Justification justifySelf;
    private String flex;

    @Override
    public String getRem() {
        return rem;
    }

    @Override
    public String getEm() {
        return em;
    }

    @Override
    public void setRem(String rem) {
        this.rem = rem;
    }

    @Override
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
    public Flavour getFlavour() {
        return flavour;
    }

    @Override
    public void setFlavour(Flavour flavour) {
        this.flavour = flavour;
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

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }


}
