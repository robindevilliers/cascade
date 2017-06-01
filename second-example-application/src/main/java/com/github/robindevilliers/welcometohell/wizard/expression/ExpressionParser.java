package com.github.robindevilliers.welcometohell.wizard.expression;

import com.github.robindevilliers.welcometohell.wizard.expression.function.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExpressionParser {

    public Expression generate(String expression) {

        return new Expression(interpret(new Tokenizer(expression)));
    }


    private Function<Boolean> interpret(Tokenizer it) {

        if (!it.moveNext()) {
            throw new RuntimeException("Expected expression.");
        }

        boolean complement = false;
        if (it.item().toLowerCase().equals("not")) {
            complement = true;

            if (!it.moveNext()) {
                throw new RuntimeException("Expected expression.");
            }


//            if (!it.item().equals("(")) {
//                throw new RuntimeException("Invalid syntax.  'not' must preceed parenthesis '('");
//            }
        }

        Function<Boolean> function = null;

        if ("(".equals(it.item())) {
            function = interpret(it);

            if (complement) {
                function = new NotFunction(function);
            }

            if (!it.item().equals(")")) {
                throw new RuntimeException("Expected closing parenthesis ')'");
            }

            while (it.moveNext() && !it.item().equals(")")) {

                function = logical(it, function);
            }

        } else {

            Function<?> lhs = operand(it);

            String op = it.item().toLowerCase();
            if (!it.moveNext()) {
                throw new RuntimeException("Invalid syntax. Expression not complete.");
            }
            Function<?> rhs = operand(it);

            function = operator(lhs, op, rhs);
        }
        return function;
    }

    private Function<?> func(String name, List<Function<?>> arguments) {
        //List<Function<?>> args = arguments.stream().map(this::literal).collect(toList());
        switch (name) {
            //date functions
            case "now":
                return new NowFunction(arguments);
            case "date":
                return new DateFunction(arguments);
            case "year":
                return new YearFunction(arguments);
            case "month":
                return new MonthFunction(arguments);
            case "day":
                return new DayFunction(arguments);
            case "dayOfWeek":
                return new DayOfWeekFunction(arguments);
            //duration functions
            case "years":
                return new YearsFunction(arguments);
            case "months":
                return new MonthsFunction(arguments);
            case "days":
                return new DaysFunction(arguments);
            //integer functions
            case "abs":
                return new AbsFunction(arguments);
            case "period":
                return new PeriodFunction(arguments);
            case "sum":
                return new SumFunction(arguments);
            //string functions
            case "startsWith":
                return new StartsWithFunction(arguments);
            case "endsWith":
                return new EndsWithFunction(arguments);
            case "contains":
                return new ContainsFunction(arguments);
            case "upperCase":
                return new UpperCaseFunction(arguments);
            case "lowerCase":
                return new LowerCaseFunction(arguments);
            default:
                throw new RuntimeException("Invalid expression.  Unknown function " + name);
        }
    }

    private Function<Boolean> logical(Tokenizer it, Function<Boolean> function) {

        String op = it.item().toLowerCase();

        if (!op.equals("and") && !op.equals("or")) {
            throw new RuntimeException("Expected AND or OR logical operator");
        }

        Function<Boolean> next = interpret(it);

        if (op.equals("or")) {

            if (next instanceof AndFunction) {
                return new OrFunction(function, next);
            } else if (next instanceof OrFunction) {
                ((OrFunction) next).add(function);
                return next;
            } else {
                return new OrFunction(function, next);
            }

        } else if (op.equals("and")) {

            if (next instanceof AndFunction) {
                ((AndFunction) next).add(function);
                return next;
            } else if (next instanceof OrFunction) {
                OrFunction or = (OrFunction) next;
                Function<Boolean> l = or.removeFirst();
                or.add(new AndFunction(function, l));
                return next;
            } else {
                return new AndFunction(function, next);
            }
        }

        return function;
    }

    private Function<Boolean> operator(Function<?> lhs, String op, Function<?> rhs) {
        switch (op) {
            case "=":
                return new EqualsFunction(lhs, rhs);
            case "<":
                return new LessThanFunction(lhs, rhs);
            case ">":
                return new GreaterThanFunction(lhs, rhs);
            case ">=":
                return new GreaterThanOrEqualFunction(lhs, rhs);
            case "<=":
                return new LessThanOrEqualFunction(lhs, rhs);
            case "~":
                return new MatchesFunction(lhs, rhs);
            default:
                throw new RuntimeException("Invalid expression. Unknown operator " + op);
        }
    }

    private Function<?> operand(Tokenizer it) {
        String token = it.item();

        if (it.moveNext() && it.item().equals("(")) {
            //arguments to functions are limited to simple tokens for now.

            if (!it.moveNext()) {
                throw new RuntimeException("Invalid syntax, token expected.");
            }

            List<Function<?>> arguments = new ArrayList<>();
            while (!it.item().equals(")")) {

                arguments.add(operand(it));

//                if (!it.moveNext()) {
//                    throw new RuntimeException("Invalid syntax, token expected.");
//                }

                //TODO you can follow a , with a ) here.
                if (it.item().equals(",")) {
                    if (!it.moveNext()) {
                        throw new RuntimeException("Invalid syntax, token expected.");
                    }
                }
            }

            it.moveNext();

            return func(token, arguments);
        } else {
            return literal(token);
        }
    }

    public Function<?> literal(String token) {
        if ("true".equals(token)) {
            return new TrueFunction();
        } else if ("false".equals(token)) {
            return new FalseFunction();
        } else if (token.startsWith("'")) {
            return new StringFunction(token);
        } else if (isInteger(token)) {
            return new IntegerFunction(token);
        } else {
            return new PathFunction(token);
        }
    }


    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
