package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;

public class ScreenshotStateRendering implements StateRenderingStrategy {
    @Override
    public boolean accept(Object value) {
        return true;
    }

    @Override
    public String render(Object value) {
        String base64 = (String) value;

        StringBuilder html = new StringBuilder();
        html.append("<div style=\"width: 30%; cursor: pointer;\" data-zoomed=\"false\" onclick=\"toggleImageSize($(this));\" class=\"thumbnail\">");
        html.append("<img style=\" height: auto; display: block;\" src=\"").append(base64).append("\">");
        html.append("</div>");
        return html.toString();
    }
}
