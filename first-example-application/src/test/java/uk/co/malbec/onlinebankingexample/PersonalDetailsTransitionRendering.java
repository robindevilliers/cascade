package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.TransitionRenderingStrategy;
import uk.co.malbec.onlinebankingexample.domain.PersonalDetails;

public class PersonalDetailsTransitionRendering implements TransitionRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return value instanceof PersonalDetails;
    }

    @Override
    public Object copy(Object value) {
        return new PersonalDetails((PersonalDetails) value);
    }

    @Override
    public String render(Object value, Object copy) {
        PersonalDetails personalDetails = (PersonalDetails) value;
        PersonalDetails personalDetailsCopy = (PersonalDetails) copy;

        StringBuilder arrow = new StringBuilder();
        arrow.append("<svg width=\"50\" height=\"1em\"");
        arrow.append("<defs>");
        arrow.append("<marker id=\"markerArrow\" markerWidth=\"13\" markerHeight=\"13\" refX=\"2\" refY=\"6\" orient=\"auto\">");
        arrow.append("<path d=\"M2,2 L2,11 L10,6 L2,2\" style=\"fill: #000000;\"/>");
        arrow.append("</marker>");
        arrow.append("</defs>");
        arrow.append("<line x1=\"10\" y1=\"0.5em\" x2=\"30\" y2=\"0.5em\" stroke=\"#000\" stroke-width=\"1\" marker-end=\"url(#markerArrow)\" />");
        arrow.append("</svg>");

        StringBuilder html = new StringBuilder();

        html.append("<table class=\"table small table-bordered\" style=\"margin: 0px\">");
        html.append("<tbody>");

        if (!personalDetails.getMobile().equals(personalDetailsCopy.getMobile())){
            html.append("<tr><th scope=\"row\">").append("Mobile: ").append("</th><td>")
                    .append(personalDetailsCopy.getMobile())
                    .append(arrow.toString())
                    .append(personalDetails.getMobile())
                    .append("</td></tr>");
        }

        if (!personalDetails.getEmail().equals(personalDetailsCopy.getEmail())){
            html.append("<tr><th scope=\"row\">").append("Email: ").append("</th><td>")
                    .append(personalDetailsCopy.getEmail())
                    .append(arrow.toString())
                    .append(personalDetails.getEmail())
                    .append("</td></tr>");
        }

        if (!personalDetails.getAddress().equals(personalDetailsCopy.getAddress())){
            html.append("<tr><th scope=\"row\">").append("Address: ").append("</th><td>")
                    .append(personalDetailsCopy.getAddress())
                    .append(arrow.toString())
                    .append(personalDetails.getAddress())
                    .append("</td></tr>");
        }

        html.append("</tbody>");
        html.append("</table>");

        return html.toString();
    }
}
