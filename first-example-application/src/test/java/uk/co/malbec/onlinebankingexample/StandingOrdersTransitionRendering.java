package uk.co.malbec.onlinebankingexample;

import uk.co.malbec.cascade.modules.reporter.TransitionRenderingStrategy;
import uk.co.malbec.onlinebankingexample.domain.StandingOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StandingOrdersTransitionRendering implements TransitionRenderingStrategy {
    @Override
    public boolean accept(Object value) {
        if (value instanceof List) {
            List list = (List) value;
            return !list.isEmpty() && list.get(0) instanceof StandingOrder;
        }
        ;
        return false;
    }

    @Override
    public Object copy(Object value) {
        List list = (List) value;

        List<StandingOrder> results = new ArrayList<>();
        for (Object o : list) {
            results.add(new StandingOrder((StandingOrder) o));
        }
        return results;
    }

    @Override
    public String render(Object value, Object copy) {
        List<StandingOrder> currentList = (List<StandingOrder>) value;
        List<StandingOrder> originalList = (List<StandingOrder>) copy;

        List<StandingOrder> copyOfOriginalList = new ArrayList<>(originalList);

        List<StandingOrder> added = new ArrayList<>();
        List<StandingOrder> deleted = new ArrayList<>();
        List<Moved> moved = new ArrayList<>();


        for (StandingOrder current : currentList) {
            if (originalList.contains(current)) {
                int indexOfCurrent = currentList.indexOf(current);
                int indexOfOriginal = originalList.indexOf(current);
                if (indexOfCurrent != indexOfOriginal) {
                    moved.add(new Moved(current, indexOfCurrent, indexOfOriginal));
                }
                copyOfOriginalList.remove(current);
            } else {
                added.add(current);
            }
        }

        for (StandingOrder removed : copyOfOriginalList) {
            deleted.add(removed);
        }

        List<Integer> indexesOfDeletedElements = deleted.stream().map(originalList::indexOf).collect(Collectors.toList());
        List<Edited> edited = new ArrayList<>();

        for (StandingOrder standingOrder : new ArrayList<>(added)) {
            int index = currentList.indexOf(standingOrder);
            if (indexesOfDeletedElements.contains(index)) {
                edited.add(new Edited(currentList.get(index), originalList.get(index)));
                added.remove(currentList.get(index));
                deleted.remove(originalList.get(index));
            }
        }

        StringBuilder html = new StringBuilder();

        if (!moved.isEmpty()) {
            html.append("<h5>Standing orders that have changed position: </h5>");
            html.append("<ul>");

            for (Moved movedElement : moved) {
                html.append("<li>").append("Index ")
                        .append(movedElement.getLhs())
                        .append(" has moved to ")
                        .append(movedElement.getRhs())
                        .append("</li>");
            }

            html.append("</ul>");
        }

        if (!added.isEmpty()) {
            html.append("<h5>New: </h5>");
            renderStandingOrderTable(html, added);
        }

        if (!edited.isEmpty()) {
            for (Edited editedPair : edited) {
                html.append("<h5>Modified standing order at index ").append(currentList.indexOf(editedPair.getCurrent())).append(":</h5>");

                html.append("<table class=\"table small table-bordered\" style=\"margin: 0px\">");
                html.append("<tbody>");
                renderFieldChange(html, "Id", editedPair.getCurrent().getId(), editedPair.getOriginal().getId());
                renderFieldChange(html, "Due Date", editedPair.getCurrent().getDueDate(), editedPair.getOriginal().getDueDate());
                renderFieldChange(html, "Description", editedPair.getCurrent().getDescription(), editedPair.getOriginal().getDescription());
                renderFieldChange(html, "Reference", editedPair.getCurrent().getReference(), editedPair.getOriginal().getReference());
                renderFieldChange(html, "Account Number", editedPair.getCurrent().getAccountNumber(), editedPair.getOriginal().getAccountNumber());
                renderFieldChange(html, "Sort Code", editedPair.getCurrent().getSortCode(), editedPair.getOriginal().getSortCode());
                renderFieldChange(html, "Period", editedPair.getCurrent().getPeriod(), editedPair.getOriginal().getPeriod());
                renderFieldChange(html, "Amount", editedPair.getCurrent().getAmount(), editedPair.getOriginal().getAmount());
                html.append("</tbody>");
                html.append("</table>");
            }
        }

        if (!deleted.isEmpty()) {
            html.append("<h5>Removed: </h5>");
            renderStandingOrderTable(html, deleted);
        }
        return html.toString();
    }

    private void renderFieldChange(StringBuilder html, String fieldName, String current, String original) {
        StringBuilder arrow = new StringBuilder();
        arrow.append("<svg width=\"50\" height=\"1em\"");
        arrow.append("<defs>");
        arrow.append("<marker id=\"markerArrow\" markerWidth=\"13\" markerHeight=\"13\" refX=\"2\" refY=\"6\" orient=\"auto\">");
        arrow.append("<path d=\"M2,2 L2,11 L10,6 L2,2\" style=\"fill: #000000;\"/>");
        arrow.append("</marker>");
        arrow.append("</defs>");
        arrow.append("<line x1=\"10\" y1=\"0.5em\" x2=\"30\" y2=\"0.5em\" stroke=\"#000\" stroke-width=\"1\" marker-end=\"url(#markerArrow)\" />");
        arrow.append("</svg>");

        if (!current.equals(original)) {
            html.append("<tr><th scope=\"row\">").append(fieldName).append(": ").append("</th><td>")
                    .append(original)
                    .append(arrow.toString())
                    .append(current)
                    .append("</td></tr>");
        }

    }


    private void renderStandingOrderTable(StringBuilder html, List<StandingOrder> standingOrders) {
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
    }

    private class Edited {
        private StandingOrder current;
        private StandingOrder original;

        public Edited(StandingOrder current, StandingOrder original) {
            this.current = current;
            this.original = original;
        }

        public StandingOrder getCurrent() {
            return current;
        }

        public void setCurrent(StandingOrder current) {
            this.current = current;
        }

        public StandingOrder getOriginal() {
            return original;
        }

        public void setOriginal(StandingOrder original) {
            this.original = original;
        }
    }

    private class Moved {
        private StandingOrder standingOrder;
        private int lhs;
        private int rhs;

        public Moved(StandingOrder standingOrder, int lhs, int rhs) {
            this.standingOrder = standingOrder;
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public StandingOrder getStandingOrder() {
            return standingOrder;
        }

        public void setStandingOrder(StandingOrder standingOrder) {
            this.standingOrder = standingOrder;
        }

        public int getLhs() {
            return lhs;
        }

        public void setLhs(int lhs) {
            this.lhs = lhs;
        }

        public int getRhs() {
            return rhs;
        }

        public void setRhs(int rhs) {
            this.rhs = rhs;
        }
    }
}
