package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.Map;

public class StringFunction implements Function<String> {

    private String string;

    public StringFunction(String literal){
        this.string = literal.substring(1, literal.length() - 1).replaceAll("\\'","'").replaceAll("\\\\","\\");
    }

    @Override
    public String apply(Map<String, Object> scope) {
        return string;
    }
}
