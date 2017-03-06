package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;
import uk.co.malbec.onlinebankingexample.domain.PersonalDetails;

public class PersonalDetailsRendering implements StateRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return true;
    }

    @Override
    public String render(Object value) {
        PersonalDetails personalDetails = (PersonalDetails) value;
        StringBuilder html = new StringBuilder();

        html.append("<table class=\"table small table-bordered\" style=\"margin: 0px\">");
        html.append("<tbody>");

        html.append("<tr><th scope=\"row\">").append("Name: ").append("</th><td>").append(personalDetails.getName()).append("</td></tr>");
        html.append("<tr><th scope=\"row\">").append("Nationality: ").append("</th><td>").append(personalDetails.getNationality()).append("</td></tr>");
        html.append("<tr><th scope=\"row\">").append("Domicile: ").append("</th><td>").append(personalDetails.getDomicile()).append("</td><tr>");
        html.append("<tr><th scope=\"row\">").append("Address: ").append("</th><td>").append(personalDetails.getAddress()).append("</td></tr>");
        html.append("<tr><th scope=\"row\">").append("Email: ").append("</th><td>").append(personalDetails.getEmail()).append("</td></tr>");
        html.append("<tr><th scope=\"row\">").append("Mobile: ").append("</th><td>").append(personalDetails.getMobile()).append("</td></tr>");


        html.append("</tbody>");
        html.append("</table>");

        return html.toString();
    }
}
