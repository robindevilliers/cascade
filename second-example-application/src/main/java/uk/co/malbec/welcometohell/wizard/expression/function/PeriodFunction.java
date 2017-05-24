package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

public class PeriodFunction implements Function<Period> {

    private Function<?> lhs;
    private Function<?> rhs;

    public PeriodFunction(List<Function<?>> arguments) {
        if (arguments.size() != 2) {
            throw new RuntimeException("Invalid expression. Diff function accepts only 2 arguments.");
        }
        this.lhs = arguments.get(0);
        this.rhs = arguments.get(1);
    }

    @Override
    public Period apply(Map<String, Object> scope) {
        Object lhs = this.lhs.apply(scope);
        Object rhs = this.rhs.apply(scope);

        if (lhs instanceof LocalDate && rhs instanceof LocalDate) {
            LocalDate leftDate = (LocalDate) lhs;
            LocalDate rightDate = (LocalDate) rhs;

            return Period.between(leftDate, rightDate);
        } else {
            throw new RuntimeException("Invalid Expression. Unsupported argument type or mismatched argument types to perform diff.");
        }
    }
}
