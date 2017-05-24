package uk.co.malbec.welcometohell.wizard.domain;

import uk.co.malbec.welcometohell.FontWeight;
import uk.co.malbec.welcometohell.wizard.*;
import uk.co.malbec.welcometohell.wizard.domain.types.DataElement;
import uk.co.malbec.welcometohell.wizard.domain.types.FlexItemSupport;
import uk.co.malbec.welcometohell.wizard.domain.types.PresentationClassesSupport;
import uk.co.malbec.welcometohell.wizard.domain.types.TextStyleSupport;
import uk.co.malbec.welcometohell.wizard.type.Type;

public class Option extends TextElement implements PresentationClassesSupport,
        TextStyleSupport,
        DataElement,
        FlexItemSupport {

    private String data;
    private Object value;
    private Type type;

    private Flavour flavour;

    private String rem;
    private String em;
    private TextDecoration textDecoration;
    private FontStyle fontStyle;
    private FontWeight fontWeight;

    private Alignment alignSelf;
    private Justification justifySelf;
    private String flex;

    @Override
    public Flavour getFlavour() {
        return flavour;
    }

    @Override
    public void setFlavour(Flavour flavour) {
        this.flavour = flavour;
    }

    @Override
    public String getRem() {
        return rem;
    }

    @Override
    public void setRem(String rem) {
        this.rem = rem;
    }

    @Override
    public String getEm() {
        return em;
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
    public String getData() {
        return data;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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
