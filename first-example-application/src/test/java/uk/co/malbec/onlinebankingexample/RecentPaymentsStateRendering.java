package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;
import uk.co.malbec.onlinebankingexample.domain.Payment;

import java.util.List;

public class RecentPaymentsStateRendering implements StateRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String render(Object value) {
        List<Payment> payments = (List<Payment>) value;
        StringBuilder html = new StringBuilder();

        html.append("<table class=\"table small table-bordered\" style=\"margin: 0px\">");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>#</th>");
        html.append("<th>Date</th>");
        html.append("<th>Description</th>");
        html.append("<th>Reference</th>");
        html.append("<th>Acc. No.</th>");
        html.append("<th>Sort No.</th>");
        html.append("<th>Amount</th>");
        html.append("<th>Cleared</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        int index = 1;
        for (Payment payment : payments) {

            html.append("<tr>");
            html.append("<th scope=\"row\">").append(index).append("</th>");
            html.append("<td>").append(payment.getDate()).append("</td>");
            html.append("<td>").append(payment.getDescription()).append("</td>");
            html.append("<td>").append(payment.getReference()).append("</td>");
            html.append("<td>").append(payment.getAccountNumber()).append("</td>");
            html.append("<td>").append(payment.getSortCode()).append("</td>");
            html.append("<td>").append(payment.getAmount()).append("</td>");
            html.append("<td>").append(payment.getCleared()).append("</td>");
            html.append("</tr>");
            index++;
        }
        html.append("</tbody>");
        html.append("</table>");
        return html.toString();
    }
}

