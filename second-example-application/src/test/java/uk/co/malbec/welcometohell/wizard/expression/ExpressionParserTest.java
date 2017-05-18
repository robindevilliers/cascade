package uk.co.malbec.welcometohell.wizard.expression;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import uk.co.malbec.welcometohell.wizard.expression.function.NowFunction;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExpressionParserTest {
    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void allTests() throws Exception {

        test("gender = 'MALE'", "{'gender': 'MALE'}", true, null);

        test("gender = 'FEMALE'", "{'gender': 'MALE'}", false, null);
        test("male = true", "{'male': true}", true, null);
        test("male = false", "{'male': true}", false, null);
        test("(gender = 'MALE')", "{'gender': 'MALE'}", true, null);
        test("(gender = 'FEMALE')", "{'gender': 'MALE'}", false, null);
        test("(gender = 'MALE') AND (role = 'ADMIN')", "{'gender': 'MALE', 'role': 'ADMIN'}", true, null);
        test("(gender = 'FEMALE') AND (role = 'ADMIN')", "{'gender': 'MALE', 'role': 'ADMIN'}", false, null);
        test("(gender = 'MALE') AND (role = 'ADMIN')", "{'gender': 'MALE', 'role': 'GUEST'}", false, null);
        test("(gender = 'MALE') OR (role = 'GUEST')", "{'gender': 'MALE', 'role': 'ADMIN'}", true, null);
        test("(gender = 'FEMALE') OR (role = 'ADMIN')", "{'gender': 'MALE', 'role': 'ADMIN'}", true, null);
        test("(gender = 'FEMALE') OR (role = 'GUEST')", "{'gender': 'MALE', 'role': 'ADMIN'}", false, null);
        test("(gender = 'MALE') AND (religion = 'Christian') OR (role = 'ADMIN')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN'}", true, null);
        test("(gender = 'MALE') AND (religion = 'Hindu') OR (role = 'ADMIN')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN'}", true, null);
        test("(gender = 'MALE') AND (religion = 'Christian') OR (role = 'GUEST')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN'}", true, null);

        test("(gender = 'FEMALE') AND (religion = 'Christian') OR (role = 'GUEST')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN'}", false, null);
        test("(gender = 'MALE') AND (religion = 'Hindu') OR (role = 'GUEST')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN'}", false, null);
        test("(gender = 'MALE') AND (religion = 'Christian') OR (role = 'ADMIN') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN', 'env': 'DEV'}", true, null);
        test("(gender = 'MALE') AND (religion = 'Hindu') OR (role = 'GUEST') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN', 'env': 'DEV'}", false, null);
        test("(gender = 'MALE') AND (religion = 'Hindu') OR (role = 'ADMIN') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN', 'env': 'DEV'}", true, null);
        test("(gender = 'MALE') AND (religion = 'Christian') OR (role = 'ADMIN') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'GUEST', 'env': 'DEV'}", true, null);
        test("(gender = 'FEMALE') OR (religion = 'Hindu') AND (role = 'ADMIN') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'GUEST', 'env': 'DEV'}", false, null);
        test("(gender = 'FEMALE') OR (religion = 'Christian') AND (role = 'ADMIN') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'GUEST', 'env': 'DEV'}", false, null);
        test("(gender = 'FEMALE') OR (religion = 'Christian') AND (role = 'ADMIN') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'GUEST', 'env': 'DEV'}", false, null);
        test("(gender = 'FEMALE') OR (religion = 'Christian') AND (role = 'ADMIN') AND (env = 'PROD')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN', 'env': 'DEV'}", false, null);
        test("(gender = 'MALE') AND (((religion = 'Hindu'))) OR (role = 'ADMIN') AND (env = 'DEV')", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN', 'env': 'DEV'}", true, null);
        test("(((religion = 'Christian')))", "{'religion': 'Christian'}", true, null);
        test(" ( gender = 'MALE' ) AND  ( ( (  religion    =     'Hindu'    )   )    )    OR   (role =           'ADMIN')        AND (env =           'DEV' )      ", "{'gender': 'MALE', 'religion': 'Christian', 'role': 'ADMIN', 'env': 'DEV'}", true, null);

        test("upperCase(gender) = 'MALE'", "{'gender': 'male'}", true, null);
        test("lowerCase(gender) = 'male'", "{'gender': 'MALE'}", true, null);
        test("startsWith(gender, 'M') = true", "{'gender': 'MALE'}", true, null);
        test("startsWith(gender, 'F') = true", "{'gender': 'MALE'}", false, null);

        test("endsWith(gender, 'E') = true", "{'gender': 'MALE'}", true, null);
        test("endsWith(gender, 'T') = true", "{'gender': 'MALE'}", false, null);

        test("contains(gender, 'AL') = true", "{'gender': 'MALE'}", true, null);
        test("contains(gender, 'XY') = true", "{'gender': 'MALE'}", false, null);


        test("breed ~ '[R][Oo][Tt][Tt][Ww][Ee][Ii][Ll][Ee][Rr]'", "{'breed': 'Rottweiler'}", false, null);
    }

    private static void test(String expression, String data, boolean expected, Clock clock) throws IOException {
        NowFunction.clock = clock == null ? Clock.systemDefaultZone() : clock;
        assertThat(new ExpressionParser().generate(expression).matches(mapper.readValue(data.replaceAll("'", "\""), Map.class)), is(expected));
    }

    @Test
    public void simpleDateLessThanAndNow() {
        Expression expression = new ExpressionParser().generate("birthDate < now()");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("2000-04-08"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void simpleDateGreaterThanADate() {
        Expression expression = new ExpressionParser().generate("birthDate > date('1990-01-01')");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("2000-04-08"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void simpleDateEqualADate() {
        Expression expression = new ExpressionParser().generate("birthDate = date('1990-01-01')");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("1990-01-01"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void simpleDateGreaterThanOrEqualADate() {
        Expression expression = new ExpressionParser().generate("birthDate >= date('1990-01-01')");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("1990-01-01"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void simpleDateLessThanOrEqualADate() {
        Expression expression = new ExpressionParser().generate("birthDate <= date('1990-01-01')");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("1990-01-01"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void yearsGreaterThanOrEqualTo21() {
        NowFunction.clock = Clock.fixed(Instant.parse("2017-12-03T11:00:00.00Z"), ZoneId.systemDefault());
        Expression expression = new ExpressionParser().generate("years(period(now(), birthDate)) >= 21");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("1990-04-08"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void monthsLessThanOrEqualTo3() {
        NowFunction.clock = Clock.fixed(Instant.parse("2007-12-03T11:00:00.00Z"), ZoneId.systemDefault());
        Expression expression = new ExpressionParser().generate("months(period(now(), birthDate)) <= 3");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("2007-08-08"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void daysLessThan7() {
        NowFunction.clock = Clock.fixed(Instant.parse("2007-12-03T11:00:00.00Z"), ZoneId.systemDefault());
        Expression expression = new ExpressionParser().generate("days(period(now(), birthDate)) < 7");

        Map<String, Object> data = new HashMap<>();
        data.put("birthDate", LocalDate.parse("1990-04-08"));
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void monthOfSeptember() {
        NowFunction.clock = Clock.fixed(Instant.parse("2007-09-03T11:00:00.00Z"), ZoneId.systemDefault());
        Expression expression = new ExpressionParser().generate("month(now()) = 9");

        Map<String, Object> data = new HashMap<>();
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void year() {
        NowFunction.clock = Clock.fixed(Instant.parse("2007-09-03T11:00:00.00Z"), ZoneId.systemDefault());
        Expression expression = new ExpressionParser().generate("year(now()) = 2007");

        Map<String, Object> data = new HashMap<>();
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void day() {
        NowFunction.clock = Clock.fixed(Instant.parse("2007-09-03T11:00:00.00Z"), ZoneId.systemDefault());
        Expression expression = new ExpressionParser().generate("day(now()) = 3");

        Map<String, Object> data = new HashMap<>();
        assertThat(expression.matches(data), is(true));
    }

    @Test
    public void dayOfTheWeek() {
        NowFunction.clock = Clock.fixed(Instant.parse("2017-05-10T11:00:00.00Z"), ZoneId.systemDefault());
        Expression expression = new ExpressionParser().generate("dayOfWeek(now()) = 3");

        Map<String, Object> data = new HashMap<>();
        assertThat(expression.matches(data), is(true));
    }

    //TODO - syntactic validation
    //TODO - path tests - invalid paths, deep paths, arrays, paths both sides.
}