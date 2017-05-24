package uk.co.malbec.welcometohell.wizard.expression;

import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TokenizerTest {

    @Test
    public void basicEqualityParse() {
        Tokenizer it = new Tokenizer("gender equals 'MALE'");

        assertThat(next(it), is("gender"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("'MALE'"));
    }

    @Test
    public void basicEqualityParseWithCaps() {
        Tokenizer it = new Tokenizer("birthDate equals 'test'");

        assertThat(next(it), is("birthDate"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("'test'"));
    }

    @Test
    public void literalTest() {
        Tokenizer it = new Tokenizer("ship equals 'Corellian Corvette'");

        assertThat(next(it), is("ship"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("'Corellian Corvette'"));
    }

    @Test
    public void escapingTest() {
        Tokenizer it = new Tokenizer("ship equals 'Slave \\'One\\''");

        assertThat(next(it), is("ship"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("'Slave \\'One\\''"));
    }

    @Test
    public void doubleEscapingTest() {
        Tokenizer it = new Tokenizer("'Slave I\\\\' equals ship");

        assertThat(next(it), is("'Slave I\\\\'"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("ship"));
    }

    @Test
    public void complexParse() {
        Tokenizer it = new Tokenizer("(gender equals 'MALE')");

        assertThat(next(it), is("("));
        assertThat(next(it), is("gender"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("'MALE'"));
        assertThat(next(it), is(")"));
    }

    @Test
    public void whiteSpaceRemoval() {
        Tokenizer it = new Tokenizer(" ( gender      equals   ' M A L E '  )   ");

        assertThat(next(it), is("("));
        assertThat(next(it), is("gender"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("' M A L E '"));
        assertThat(next(it), is(")"));
    }

    @Test
    public void complexCompositionParse() {
        Tokenizer it = new Tokenizer("((gender equals 'MALE') and (religion equals 'JEWISH')) and (beard equals true)");

        assertThat(next(it), is("("));
        assertThat(next(it), is("("));
        assertThat(next(it), is("gender"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("'MALE'"));
        assertThat(next(it), is(")"));
        assertThat(next(it), is("and"));
        assertThat(next(it), is("("));
        assertThat(next(it), is("religion"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("'JEWISH'"));
        assertThat(next(it), is(")"));
        assertThat(next(it), is(")"));
        assertThat(next(it), is("and"));
        assertThat(next(it), is("("));
        assertThat(next(it), is("beard"));
        assertThat(next(it), is("equals"));
        assertThat(next(it), is("true"));
        assertThat(next(it), is(")"));
    }

    @Test
    public void functionParse() {
        Tokenizer it = new Tokenizer("startsWith(gender, 'M') = true");

        assertThat(next(it), is("startsWith"));
        assertThat(next(it), is("("));
        assertThat(next(it), is("gender"));
        assertThat(next(it), is(","));
        assertThat(next(it), is("'M'"));
        assertThat(next(it), is(")"));
        assertThat(next(it), is("="));
        assertThat(next(it), is("true"));
    }

    public static String next(Tokenizer it){
        it.moveNext();
        return it.item();
    }
}