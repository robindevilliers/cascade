package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class NowFunction implements Function<LocalDate> {

    public static Clock clock = Clock.systemDefaultZone();

    public NowFunction(List<Function<?>> arguments) {
        if (!arguments.isEmpty()){
            throw new RuntimeException("Invalid expression, now does not take any arguments.");
        }
    }

    @Override
    public LocalDate apply(Map<String, Object> scope) {
        return LocalDate.now(clock);
    }
}
