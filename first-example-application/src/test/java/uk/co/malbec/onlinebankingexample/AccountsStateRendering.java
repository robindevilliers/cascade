package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;

import java.util.List;
import java.util.Map;


public class AccountsStateRendering implements StateRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return true;
    }

    @Override
    public String render(Object value) {
        List<Map> accounts = (List<Map>) value;
        StringBuilder html = new StringBuilder();
        html.append("<table class=\"table small table-bordered\" style=\"margin: 0px\">");
        html.append("<thead>");
        html.append("<tr> <th>#</th><th>Name</th> <th>Type</th> <th>Acc. No.</th><th>Balance</th></tr>");
        html.append("</thead>");
        html.append("<tbody>");

        int index = 1;
        for (Map account : accounts) {
            html.append("<tr>");
            html.append("<th scope=\"row\">").append(index).append("</th>");
            html.append("<td>").append(account.get("name")).append("</td>");
            html.append("<td>").append(account.get("type")).append("</td>");
            html.append("<td>").append(account.get("number")).append("</td>");
            html.append("<td>").append(account.get("balance")).append("</td>");
            html.append("</tr>");

            html.append("<tr><td colspan=\"5\">");
            if (account.get("transactions") != null && !((List<Map>) account.get("transactions")).isEmpty()){
                List<Map> transactions = (List<Map>) account.get("transactions");
                html.append("<table class=\"table table-bordered\" style=\"margin: 0px\" >");
                html.append("<caption>Transactions</caption>");
                html.append("<thead>");
                html.append("<tr><th>#</th><th>Date</th><th>Description</th><th>Type</th><th>Amount</th></tr>");
                html.append("</thead><tbody>");
                int transactionIndex = 1;
                for (Map transaction: transactions){
                    html.append("<tr> <th scope=\"row\">").append(transactionIndex).append("</th>");
                    html.append("<td>").append(transaction.get("date")).append("</td>");
                    html.append("<td>").append(transaction.get("description")).append("</td>");
                    html.append("<td>").append(transaction.get("type")).append("</td>");
                    html.append("<td>").append(transaction.get("amount")).append("</td>");
                    html.append("</tr>");
                    transactionIndex++;
                }
                html.append("</tbody> </table>");
            } else {
                html.append("There are no transactions");
            }
            html.append("</tr>");

            index++;
        }
        html.append("</tbody>");
        html.append("</table>");
        return html.toString();
    }
}
