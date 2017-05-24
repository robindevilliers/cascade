package uk.co.malbec.welcometohell.ui;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import uk.co.malbec.welcometohell.FontWeight;
import uk.co.malbec.welcometohell.wizard.*;
import uk.co.malbec.welcometohell.wizard.domain.types.*;

import java.io.StringWriter;
import java.util.Map;

public class RendererUtilities {

    public static void generateClasses(PresentationClassesSupport element, VelocityContext context, String input, String flavourPrefix) {
        StringBuilder classes = new StringBuilder();
        classes.append(input + " ");

        if (element.getFlavour() != null) {
            classes.append(flavourPrefix + "-" + element.getFlavour().getName() + " ");
        }
        context.put("classes", classes.toString());
    }

    public static void generateStyles(Element element, VelocityContext context) {
        StringBuilder styles = new StringBuilder();


        if (element instanceof TextStyleSupport) {
            TextStyleSupport textStyleSupport = (TextStyleSupport) element;
            if (textStyleSupport.getRem() != null) {
                styles.append("font-size: " + textStyleSupport.getRem() + "rem; ");
            }

            if (textStyleSupport.getEm() != null) {
                styles.append("font-size: " + textStyleSupport.getEm() + "em; ");
            }

            if (textStyleSupport.getTextDecoration() != null){
                if (TextDecoration.OVERLINE.equals(textStyleSupport.getTextDecoration())){
                    styles.append("text-decoration: overline; ");
                } else if (TextDecoration.UNDERLINE.equals(textStyleSupport.getTextDecoration())){
                    styles.append("text-decoration: underline; ");
                } else if (TextDecoration.LINE_THROUGH.equals(textStyleSupport.getTextDecoration())){
                    styles.append("text-decoration: line-through; ");
                }
            }

            if (textStyleSupport.getFontStyle() != null){
                if (FontStyle.NORMAL.equals(textStyleSupport.getFontStyle())){
                    styles.append("font-style: normal; ");
                } else if (FontStyle.ITALIC.equals(textStyleSupport.getFontStyle())){
                    styles.append("font-style: italic; ");
                } else if (FontStyle.OBLIQUE.equals(textStyleSupport.getFontStyle())){
                    styles.append("font-style: oblique; ");
                }
            }

            if (textStyleSupport.getFontWeight() != null){
                if (FontWeight.NORMAL.equals(textStyleSupport.getFontWeight())){
                    styles.append("font-weight: normal; ");
                } else if (FontWeight.BOLD.equals(textStyleSupport.getFontWeight())){
                    styles.append("font-weight: bold; ");
                } else if (FontWeight.BOLDER.equals(textStyleSupport.getFontWeight())){
                    styles.append("font-weight: bolder; ");
                }  else if (FontWeight.LIGHTER.equals(textStyleSupport.getFontWeight())){
                    styles.append("font-weight: lighter; ");
                } else if (FontWeight.INITIAL.equals(textStyleSupport.getFontWeight())){
                    styles.append("font-weight: initial; ");
                } else if (FontWeight.INHERIT.equals(textStyleSupport.getFontWeight())){
                    styles.append("font-weight: inherit; ");
                }
            }
        }

        if (element instanceof FlexContainerSupport) {
            FlexContainerSupport flexContainerSupport = (FlexContainerSupport) element;

            if (flexContainerSupport.getOrientation() != null) {
                if (Orientation.COLUMN.equals(flexContainerSupport.getOrientation())) {
                    styles.append("flex-direction: column; ");
                } else if (Orientation.ROW.equals(flexContainerSupport.getOrientation())) {
                    styles.append("flex-direction: row; ");
                } else if (Orientation.ROW_REVERSE.equals(flexContainerSupport.getOrientation())) {
                    styles.append("flex-direction: row-reverse; ");
                } else if (Orientation.COLUMN_REVERSE.equals(flexContainerSupport.getOrientation())) {
                    styles.append("flex-direction: column-reverse; ");
                }
            }

            if (flexContainerSupport.getJustifyContent() != null) {
                if (Justification.START.equals(flexContainerSupport.getJustifyContent())) {
                    styles.append("justify-content: flex-start; ");
                } else if (Justification.END.equals(flexContainerSupport.getJustifyContent())) {
                    styles.append("justify-content: flex-end; ");
                } else if (Justification.CENTER.equals(flexContainerSupport.getJustifyContent())) {
                    styles.append("justify-content: center; ");
                } else if (Justification.SPACE_BETWEEN.equals(flexContainerSupport.getJustifyContent())) {
                    styles.append("justify-content: space-between; ");
                } else if (Justification.SPACE_AROUND.equals(flexContainerSupport.getJustifyContent())) {
                    styles.append("justify-content: space-around; ");
                }
            }

            if (flexContainerSupport.getAlignItems() != null) {
                if (Alignment.START.equals(flexContainerSupport.getAlignItems())) {
                    styles.append("align-items: flex-start; ");
                } else if (Alignment.END.equals(flexContainerSupport.getAlignItems())) {
                    styles.append("align-items: flex-end; ");
                } else if (Alignment.CENTER.equals(flexContainerSupport.getAlignItems())) {
                    styles.append("align-items: center; ");
                } else if (Alignment.STRETCH.equals(flexContainerSupport.getAlignItems())) {
                    styles.append("align-items: stretch; ");
                } else if (Alignment.BASELINE.equals(flexContainerSupport.getAlignItems())) {
                    styles.append("align-items: baseline; ");
                }
            }

            if (flexContainerSupport.getWrap() != null){
                if (Wrap.NOWRAP.equals(flexContainerSupport.getWrap())){
                    styles.append("flex-wrap: nowrap; ");
                } else if (Wrap.WRAP.equals(flexContainerSupport.getWrap())){
                    styles.append("flex-wrap: wrap; ");
                } else if (Wrap.WRAP_REVERSE.equals(flexContainerSupport.getWrap())){
                    styles.append("flex-wrap: wrap-reverse; ");
                }
            }

            if (flexContainerSupport.getAlignContent() != null){
                if (Alignment.START.equals(flexContainerSupport.getAlignContent())) {
                    styles.append("align-content: flex-start; ");
                } else if (Alignment.END.equals(flexContainerSupport.getAlignContent())) {
                    styles.append("align-content: flex-end; ");
                } else if (Alignment.CENTER.equals(flexContainerSupport.getAlignContent())) {
                    styles.append("align-content: center; ");
                } else if (Alignment.STRETCH.equals(flexContainerSupport.getAlignContent())) {
                    styles.append("align-content: stretch; ");
                } else if (Alignment.BASELINE.equals(flexContainerSupport.getAlignContent())) {
                    styles.append("align-content: baseline; ");
                }
            }
        }


        if (element instanceof FlexItemSupport) {
            FlexItemSupport flexItemSupport = (FlexItemSupport) element;

            if (flexItemSupport.getFlex() != null) {
                styles.append("flex: " + flexItemSupport.getFlex() + "; ");
            }

            if (flexItemSupport.getAlignSelf() != null) {

                if (Alignment.START.equals(flexItemSupport.getAlignSelf())) {
                    styles.append("align-self: flex-start; ");
                } else if (Alignment.END.equals(flexItemSupport.getAlignSelf())) {
                    styles.append("align-self: flex-end; ");
                } else if (Alignment.CENTER.equals(flexItemSupport.getAlignSelf())) {
                    styles.append("align-self: center; ");
                } else if (Alignment.BASELINE.equals(flexItemSupport.getAlignSelf())) {
                    styles.append("align-self: baseline; ");
                } else if (Alignment.STRETCH.equals(flexItemSupport.getAlignSelf())) {
                    styles.append("align-self: stretch; ");
                }
            }

            if (flexItemSupport.getJustifySelf() != null) {

                if (Justification.START.equals(flexItemSupport.getJustifySelf())) {
                    styles.append("margin-right: auto; ");
                }

                if (Justification.CENTER.equals(flexItemSupport.getJustifySelf())) {
                    styles.append("justify-self: flex-start");
                    styles.append("margin-left: auto; margin-right: auto; ");
                }

                if (Justification.END.equals(flexItemSupport.getJustifySelf())) {
                    styles.append("margin-left: auto; ");
                }
            }
        }

        context.put("styles", styles.toString());
    }

    public static void generateContent(VelocityEngine velocityEngine, TextElement element, Map<String, Object> data, VelocityContext context) {
        StringWriter content = new StringWriter();
        velocityEngine.evaluate(new VelocityContext(data), content, "utilities-renderer", element.getText());
        context.put("content", content.toString());
    }

    public static void generateContent(String wizardSessionId, String pageId, RenderingEngine renderingEngine, ElementComposer element, Map<String, Object> data, VelocityContext context) {
        context.put("content", renderingEngine.renderElementComposer(wizardSessionId, pageId, element, data));
    }
}
