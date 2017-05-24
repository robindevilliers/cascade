package uk.co.malbec.welcometohell.wizard;

import org.xml.sax.Attributes;
import uk.co.malbec.welcometohell.FontWeight;
import uk.co.malbec.welcometohell.wizard.domain.types.*;

import static java.util.Optional.ofNullable;

public class ParserUtilities {

    public static void assignAttributes(Element element, Attributes attributes){

        if (element instanceof PresentationClassesSupport){
            PresentationClassesSupport presentationClassesSupport = (PresentationClassesSupport) element;

            presentationClassesSupport.setFlavour(ofNullable(attributes.getValue("flavour")).map(Flavour::valueOf).orElse(null));
        }

        if (element instanceof TextStyleSupport){
            TextStyleSupport textStyleSupport = (TextStyleSupport) element;

            textStyleSupport.setEm(attributes.getValue("em"));
            textStyleSupport.setRem(attributes.getValue("rem"));

            textStyleSupport.setFontStyle(ofNullable(attributes.getValue("fontStyle")).map(FontStyle::valueOf).orElse(null));
            textStyleSupport.setFontWeight(ofNullable(attributes.getValue("fontWeight")).map(FontWeight::valueOf).orElse(null));
            textStyleSupport.setTextDecoration(ofNullable(attributes.getValue("textDecoration")).map(TextDecoration::valueOf).orElse(null));
        }

        if (element instanceof FlexContainerSupport){
            FlexContainerSupport flexContainerSupport = (FlexContainerSupport) element;

            flexContainerSupport.setOrientation(ofNullable(attributes.getValue("orientation")).map(Orientation::valueOf).orElse(null));
            flexContainerSupport.setJustifyContent(ofNullable(attributes.getValue("justifyContent")).map(Justification::valueOf).orElse(null));
            flexContainerSupport.setAlignItems(ofNullable(attributes.getValue("alignItems")).map(Alignment::valueOf).orElse(null));
            flexContainerSupport.setWrap(ofNullable(attributes.getValue("wrap")).map(Wrap::valueOf).orElse(null));
            flexContainerSupport.setAlignContent(ofNullable(attributes.getValue("alignContent")).map(Alignment::valueOf).orElse(null));
        }

        if (element instanceof FlexItemSupport){
            FlexItemSupport flexItemSupport = (FlexItemSupport) element;

            flexItemSupport.setJustifySelf(ofNullable(attributes.getValue("justifySelf")).map(Justification::valueOf).orElse(null));
            flexItemSupport.setAlignSelf(ofNullable(attributes.getValue("alignSelf")).map(Alignment::valueOf).orElse(null));
            flexItemSupport.setFlex(attributes.getValue("flex"));
        }
    }
}
