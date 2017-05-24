package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;
import uk.co.malbec.onlinebankingexample.domain.Account;
import uk.co.malbec.onlinebankingexample.domain.Transaction;

import java.util.List;


public class AccountsStateRendering implements StateRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return true;
    }

    @Override
    public String render(Object value) {
        List<Account> accounts = (List<Account>) value;
        StringBuilder html = new StringBuilder();
        html.append("<table class=\"table small table-bordered\" style=\"margin: 0px\">");
        html.append("<thead>");
        html.append("<tr> <th>#</th><th>Name</th> <th>Type</th> <th>Acc. No.</th><th>Balance</th></tr>");
        html.append("</thead>");
        html.append("<tbody>");

        int index = 1;
        for (Account account : accounts) {
            html.append("<tr>");
            html.append("<th scope=\"row\">").append(index).append("</th>");
            html.append("<td>").append(account.getName()).append("</td>");
            html.append("<td>").append(account.getType()).append("</td>");
            html.append("<td>").append(account.getNumber()).append("</td>");
            html.append("<td>").append(account.getBalance()).append("</td>");
            html.append("</tr>");

            html.append("<tr><td colspan=\"5\">");
            if (!account.getTransactions().isEmpty()){
                List<Transaction> transactions = account.getTransactions();
                html.append("<table class=\"table table-bordered\" style=\"margin: 0px\" >");
                html.append("<caption>Transactions</caption>");
                html.append("<thead>");
                html.append("<tr><th>#</th><th>Date</th><th>Description</th><th>Type</th><th>Amount</th></tr>");
                html.append("</thead><tbody>");
                int transactionIndex = 1;
                for (Transaction transaction: transactions){
                    html.append("<tr> <th scope=\"row\">").append(transactionIndex).append("</th>");
                    html.append("<td>").append(transaction.getDate()).append("</td>");
                    html.append("<td>").append(transaction.getDescription()).append("</td>");
                    html.append("<td>").append(transaction.getType()).append("</td>");
                    html.append("<td>").append(transaction.getAmount()).append("</td>");
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
