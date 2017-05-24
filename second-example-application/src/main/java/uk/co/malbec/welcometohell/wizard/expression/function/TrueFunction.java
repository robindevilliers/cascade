package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.Map;

public class TrueFunction implements Function<Boolean> {
    @Override
    public Boolean apply(Map<String, Object> scope) {
        return true;
    }
}
