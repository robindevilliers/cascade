package com.github.robindevilliers.welcometohell.wizard.expression;

import java.util.Map;

public interface Function<R> {

    R apply(Map<String, Object> scope);

}
