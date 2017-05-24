package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.Map;

public class IntegerFunction implements Function<Integer> {

    private int value;

    public IntegerFunction(String str){
        value = Integer.parseInt(str);
    }

    @Override
    public Integer apply(Map<String, Object> scope) {
        return value;
    }
}
