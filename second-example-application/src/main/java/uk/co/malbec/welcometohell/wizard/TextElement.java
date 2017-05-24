package uk.co.malbec.welcometohell.wizard;

import uk.co.malbec.welcometohell.wizard.domain.types.Element;

/*
This class is used by the general TextElementParser so that elements
that contain general text content are handled automatically.
 */
public class TextElement implements Element {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
