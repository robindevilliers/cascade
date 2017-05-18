package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;

import java.util.UUID;

public class ScreenshotStateRendering implements StateRenderingStrategy {
    @Override
    public boolean accept(Object value) {
        return true;
    }

    @Override
    public String render(Object value) {
        String path = (String) value;
        String id = UUID.randomUUID().toString();


        StringBuilder html = new StringBuilder();
        html.append("<div style=\"width: 30%; cursor: pointer;\" data-zoomed=\"false\" onclick=\"$('#" + id + "').modal('show');\" class=\"thumbnail\">");
        html.append("<img style=\" height: auto; display: block;\" src=\"").append(path).append("\">");
        html.append("</div>");

        html.append("<div id=\"" + id + "\" class=\"modal fade\" role=\"dialog\">");
        html.append("    <div class=\"modal-dialog modal-lg\">");
        html.append("        <div class=\"modal-content\">");
        html.append("            <div class=\"modal-header\">");
        html.append("                <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
        html.append("                <div class=\"modal-title\">");
        html.append("                    <h4>Screenshot</h4>");
        html.append("                </div>");
        html.append("            </div>");
        html.append("            <div class=\"modal-body\">");
        html.append("                <img style=\" height: auto; display: block; max-width: 100%\" src=\"").append(path).append("\">");
        html.append("            </div>");
        html.append("            <div class=\"modal-footer\">");
        html.append("                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>");
        html.append("            </div>");
        html.append("        </div>");
        html.append("    </div>");
        html.append("</div>");

        return html.toString();
    }
}
