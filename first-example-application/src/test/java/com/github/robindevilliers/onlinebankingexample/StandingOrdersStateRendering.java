package com.github.robindevilliers.onlinebankingexample;

import com.github.robindevilliers.cascade.modules.reporter.StateRenderingStrategy;
import com.github.robindevilliers.onlinebankingexample.domain.StandingOrder;

import java.util.List;

public class StandingOrdersStateRendering implements StateRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String render(Object value) {
        List<StandingOrder> standingOrders = (List<StandingOrder>) value;

        StringBuilder html = new StringBuilder();
        html.append("<table class=\"table small table-bordered\" style=\"margin: 0px\">");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>#</th>");
        html.append("<th>Due Date</th>");
        html.append("<th>Description</th>");
        html.append("<th>Reference</th>");
        html.append("<th>Period</th>");
        html.append("<th>Amount</th>");
        html.append("<th>Acc. No.</th>");
        html.append("<th>S. No.</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        for (StandingOrder standingOrder : standingOrders) {

            html.append("<tr>");
            html.append("<th scope=\"row\">").append(standingOrder.getId()).append("</th>");
            html.append("<td>").append(standingOrder.getDueDate()).append("</td>");
            html.append("<td>").append(standingOrder.getDescription()).append("</td>");
            html.append("<td>").append(standingOrder.getReference()).append("</td>");
            html.append("<td>").append(standingOrder.getPeriod()).append("</td>");
            html.append("<td>").append(standingOrder.getAmount()).append("</td>");
            html.append("<td>").append(standingOrder.getAccountNumber()).append("</td>");
            html.append("<td>").append(standingOrder.getSortCode()).append("</td>");
            html.append("</tr>");
        }
        html.append("</tbody>");
        html.append("</table>");
        return html.toString();
    }
}
