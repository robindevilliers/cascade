package com.github.robindevilliers.welcometohell.wizard.domain;

import com.github.robindevilliers.welcometohell.wizard.*;
import com.github.robindevilliers.welcometohell.wizard.domain.types.*;
import com.github.robindevilliers.welcometohell.FontWeight;
import com.github.robindevilliers.welcometohell.wizard.type.Type;

public class Input extends ElementComposer implements PresentationClassesSupport,
        DataElement,
        TextStyleSupport,
        FlexContainerSupport,
        FlexItemSupport {

    private String data;
    private Type type;

    private Flavour flavour;

    private String rem;
    private String em;
    private TextDecoration textDecoration;
    private FontStyle fontStyle;
    private FontWeight fontWeight;

    private Orientation orientation;
    private Justification justifyContent;
    private Alignment alignItems;
    private Wrap wrap;
    private Alignment alignContent;

    private Alignment alignSelf;
    private Justification justifySelf;
    private String flex;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

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

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public Justification getJustifyContent() {
        return justifyContent;
    }

    @Override
    public void setJustifyContent(Justification justifyContent) {
        this.justifyContent = justifyContent;
    }

    @Override
    public Alignment getAlignItems() {
        return alignItems;
    }

    @Override
    public void setAlignItems(Alignment alignItems) {
        this.alignItems = alignItems;
    }

    @Override
    public Wrap getWrap() {
        return wrap;
    }

    @Override
    public void setWrap(Wrap wrap) {
        this.wrap = wrap;
    }

    @Override
    public Alignment getAlignContent() {
        return alignContent;
    }

    @Override
    public void setAlignContent(Alignment alignContent) {
        this.alignContent = alignContent;
    }
}
