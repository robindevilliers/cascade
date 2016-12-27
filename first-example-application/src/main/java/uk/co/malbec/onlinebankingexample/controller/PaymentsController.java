package uk.co.malbec.onlinebankingexample.controller;


import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.co.malbec.onlinebankingexample.Utils;
import uk.co.malbec.onlinebankingexample.model.*;

import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.joda.time.DateTime.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;

@Controller
public class PaymentsController {

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/payments")
    public ModelAndView payments() {

        User user = (User) httpSession.getAttribute("user");

        return populatePaymentsView(user, new ModelAndView("payments"));
    }


    @RequestMapping("/payments/removeStandingOrder/{id}")
    public ModelAndView removeStandingOrder(@PathVariable Integer id) {

        User user = (User) httpSession.getAttribute("user");

        Iterator<StandingOrder> it = user.getStandingOrders().iterator();
        while (it.hasNext()) {
            StandingOrder standingOrder = it.next();
            if (standingOrder.getId().equals(id)) {
                it.remove();
                break;
            }
        }

        return populatePaymentsView(user, new ModelAndView("payments"));
    }

    @RequestMapping(value = "/payments/pay", method = RequestMethod.POST)
    public ModelAndView pay(@RequestParam String description,
                            @RequestParam String reference,
                            @RequestParam String accountNumber,
                            @RequestParam String sortCodeOne,
                            @RequestParam String sortCodeTwo,
                            @RequestParam String sortCodeThree,
                            @RequestParam String type,
                            @RequestParam String dateDay,
                            @RequestParam String dateMonth,
                            @RequestParam String dateYear,
                            @RequestParam String period,
                            @RequestParam String amount) {

        //the javascript currency events doesn't always send the right stuff back to us.
        //TODO - find a better solution to this hack
        Matcher matcher = Pattern.compile("([\\d,]*[.][\\d]*)").matcher(amount);

        if (!matcher.find()){
            throw new RuntimeException("invalid amount supplied");
        }


        User user = (User) httpSession.getAttribute("user");

        Integer amountInPence;
        try {
            amountInPence = (int) (new DecimalFormat("#,###,##0.00").parse(matcher.group()).doubleValue() * 100);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Integer nextStandingOrderId = 0;
        for (StandingOrder order : user.getStandingOrders()) {
            nextStandingOrderId = Math.max(order.getId(), nextStandingOrderId);
        }
        nextStandingOrderId++;


        Date dueDate;

        if ("now".equals(type)) {
            Account currentAccount = null;
            for (Account account : user.getAccounts()) {
                if (account.getType().equals(AccountType.Current)) {
                    currentAccount = account;
                    break;
                }
            }

            Integer available = currentAccount.getBalance();
            for (RecentPayment recentPayment : user.getRecentPayments()) {
                if (recentPayment.getCleared() == null) {
                    available -= recentPayment.getAmount();
                }
            }

            if (amountInPence > available) {
                ModelAndView modelAndView = new ModelAndView("payments");
                modelAndView.addObject("error","Insuffient Funds");
                return populatePaymentsView(user, modelAndView);
            }


            dueDate = new Date();
            RecentPayment recentPayment = new RecentPayment();
            recentPayment.setDescription(description);
            recentPayment.setReference(reference);
            recentPayment.setAccountNumber(accountNumber);
            recentPayment.setSortCode(sortCodeOne + sortCodeTwo + sortCodeThree);
            recentPayment.setAmount(amountInPence);
            recentPayment.setDate(dueDate);
            user.getRecentPayments().add(0, recentPayment);

            StandingOrderPeriod standingOrderPeriod = StandingOrderPeriod.valueOf(period);
            switch (standingOrderPeriod) {
                case Once:
                    dueDate = null;
                    break;
                case Monthly:
                    dueDate = new DateTime(dueDate).plusMonths(1).toDate();
                    break;
                case Quarterly:
                    dueDate = new DateTime(dueDate).plusMonths(3).toDate();
                    break;
                case Yearly:
                    dueDate = new DateTime(dueDate).plusYears(1).toDate();
                    break;
            }

        } else {
            dueDate = parse(String.format("%s/%s/%s", dateDay, dateMonth, dateYear), forPattern("MM/dd/yyyy")).toDate();
        }

        if (dueDate != null) {
            StandingOrder standingOrder = new StandingOrder();
            standingOrder.setId(nextStandingOrderId);
            standingOrder.setDescription(description);
            standingOrder.setReference(reference);
            standingOrder.setAccountNumber(accountNumber);
            standingOrder.setSortCode(sortCodeOne + sortCodeTwo + sortCodeThree);
            standingOrder.setPeriod(StandingOrderPeriod.valueOf(period));
            standingOrder.setDueDate(dueDate);
            standingOrder.setAmount(amountInPence);
            user.getStandingOrders().add(standingOrder);
        }

        return populatePaymentsView(user, new ModelAndView("payments"));
    }

    @RequestMapping(value = "/payments/transfer", method = RequestMethod.POST)
    public ModelAndView transfer(
            @RequestParam String type,
            @RequestParam String amount) {

        User user = (User) httpSession.getAttribute("user");

        Integer amountInPence;
        try {
            amountInPence = (int) (new DecimalFormat("#,###,##0.00").parse(amount).doubleValue() * 100);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Account debitAccount = null;
        Account creditAccount = null;

        if ("deposit".equals(type)) {
            for (Account account : user.getAccounts()) {
                if (AccountType.Saver.equals(account.getType())) {
                    creditAccount = account;
                } else if (AccountType.Current.equals(account.getType())) {
                    debitAccount = account;
                }
            }

            Account currentAccount = null;
            for (Account account : user.getAccounts()) {
                if (account.getType().equals(AccountType.Current)) {
                    currentAccount = account;
                    break;
                }
            }

            Integer available = currentAccount.getBalance();
            for (RecentPayment recentPayment : user.getRecentPayments()) {
                if (recentPayment.getCleared() == null) {
                    available -= recentPayment.getAmount();
                }
            }

            if (amountInPence > available) {
                ModelAndView modelAndView = new ModelAndView("payments");
                modelAndView.addObject("error","Insuffient Funds");
                return populatePaymentsView(user, modelAndView);
            }

        } else {
            for (Account account : user.getAccounts()) {
                if (AccountType.Saver.equals(account.getType())) {
                    debitAccount = account;
                } else if (AccountType.Current.equals(account.getType())) {
                    creditAccount = account;
                }
            }

            if (amountInPence > debitAccount.getBalance()){
                ModelAndView modelAndView = new ModelAndView("payments");
                modelAndView.addObject("error","Insuffient Funds");
                return populatePaymentsView(user, modelAndView);
            }
        }

        Date dueDate = DateTime.now().toDate();

        Transaction fromTransaction = new Transaction();
        fromTransaction.setDate(dueDate);
        fromTransaction.setDescription("Transfer Easy Saver Account");
        fromTransaction.setType(TransactionType.DEBIT);
        fromTransaction.setAmount(amountInPence);
        debitAccount.getTransactions().add(0, fromTransaction);
        debitAccount.setBalance(debitAccount.getBalance() - amountInPence);

        Transaction toTransaction = new Transaction();
        toTransaction.setDate(dueDate);
        toTransaction.setDescription("Transfer Premium Current Account");
        toTransaction.setType(TransactionType.CREDIT);
        toTransaction.setAmount(amountInPence);
        creditAccount.getTransactions().add(0, toTransaction);
        creditAccount.setBalance(creditAccount.getBalance() + amountInPence);

        return populatePaymentsView(user, new ModelAndView("payments"));
    }

    private ModelAndView populatePaymentsView(User user, ModelAndView modelAndView) {
        modelAndView.addObject("currentAccountPresent", Utils.isAccountPresent(user, AccountType.Current));
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("standingOrders", user.getStandingOrders());
        modelAndView.addObject("recentPayments", user.getRecentPayments());
        modelAndView.addObject("savingAccountPresent", Utils.isAccountPresent(user, AccountType.Saver));

        return modelAndView;
    }
}
