package uk.co.malbec.cascade.modules.reporter;

import java.util.List;

public class ListOfStringsStateRendering implements StateRenderingStrategy {


    @Override
    public boolean accept(Object value) {
        if (value instanceof List) {
            List list = (List) value;
            if (!list.isEmpty() && list.get(0) instanceof String) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String render(Object value) {
        List list = (List) value;

        StringBuilder html = new StringBuilder();
        html.append("<ul>");
        for (Object item : list) {
            html.append("<li>").append(item).append("</li>");
        }
        html.append("</ul>");

        return html.toString();
    }
}
