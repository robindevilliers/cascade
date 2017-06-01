package com.github.robindevilliers.welcometohell.wizard;

import com.github.robindevilliers.welcometohell.wizard.domain.View;
import com.github.robindevilliers.welcometohell.wizard.domain.Wizard;

import java.util.*;

public class WizardState {

    private Wizard wizard;

    private String wizardSessionId = UUID.randomUUID().toString();

    private Deque<TransactionLog> transactionLogs = new LinkedList<>();

    public WizardState(Wizard wizard, View startingView) {
        this.wizard = wizard;

        this.transactionLogs.add(new TransactionLog(startingView));
    }

    public Wizard getWizard() {
        return wizard;
    }

    public String getWizardSessionId() {
        return wizardSessionId;
    }

    public String getLastPageId() {
        return transactionLogs.getLast().getId();
    }

    public void setNextView(String pageId, View nextView) {

        while (!transactionLogs.getLast().getId().equals(pageId)) {
            transactionLogs.removeLast();
        }

        transactionLogs.add(new TransactionLog(nextView));
    }


    public View getViewForPageId(String pageId) {
        for (TransactionLog log : transactionLogs) {
            if (log.getId().equals(pageId)) {
                return log.getView();
            }
        }
        return null;
    }

    public void setDataOnView(String pageId, Map<String, Object> input) {
        transactionLogs
                .stream()
                .filter(l -> l.getId().equals(pageId))
                .findFirst()
                .orElse(null)
                .setData(input);
    }

    public Map<String, Object> getDataOnView(String pageId) {
        return transactionLogs
                .stream()
                .filter(l -> l.getId().equals(pageId))
                .findFirst()
                .orElse(null)
                .getData();
    }

    public Map<String, Object> buildDataImage() {
        Map<String, Object> image = new HashMap<>();

        for (TransactionLog log : transactionLogs) {
            if (log.getData() != null) {
                for (String path : log.getData().keySet()) {
                    String[] tokens = path.split("[.]");
                    Map<String, Object> current = image;
                    for (int i = 0; i < tokens.length - 1; i++) {
                        String token = tokens[i];
                        current = (Map<String, Object>) current.computeIfAbsent(token, k -> new HashMap<String, Object>());
                    }
                    current.put(tokens[tokens.length - 1], log.getData().get(path));
                }
            }
        }
        return image;
    }


    public class TransactionLog {

        private String id = UUID.randomUUID().toString();

        private View view;

        private Map<String, Object> data;

        public TransactionLog(View view) {
            this.view = view;
        }

        public String getId() {
            return id;
        }

        public View getView() {
            return view;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

        public Map<String, Object> getData() {
            return data;
        }
    }
}
