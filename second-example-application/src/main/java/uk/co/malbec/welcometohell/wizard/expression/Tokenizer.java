package uk.co.malbec.welcometohell.wizard.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tokenizer {

    private List<String> tokens = new ArrayList<>();

    private int index = -1;

    public Tokenizer(String string) {

        CharacterIterator it = new CharacterIterator(string);
        StringBuilder buffer = new StringBuilder();
        while (it.hasNext()) {

            char c = it.next();
            if (c == '(' || c == ')' || c == ',') {

                String token = buffer.toString().trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
                buffer = new StringBuilder();

                tokens.add(Character.toString(c));
            } else if (c == ' ' || c == '\t') {

                String token = buffer.toString().trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
                buffer = new StringBuilder();
            } else if (c == '\'') {

                String token = buffer.toString().trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
                buffer = new StringBuilder();

                buffer.append('\'');

                boolean escaped = true;
                while ((c != '\'' || escaped) && it.hasNext()) {
                    escaped = c == '\\' && !escaped;
                    c = it.next();
                    buffer.append(c);
                }

                tokens.add(buffer.toString());

                buffer = new StringBuilder();

            } else {
                buffer.append(c);
            }
        }

        String token = buffer.toString().trim();
        if (!token.isEmpty()) {
            tokens.add(token);
        }

    }

    public boolean moveNext() {
        index++;
        return index < tokens.size();
    }

    public String item() {
        return tokens.get(index);
    }

    public class CharacterIterator implements Iterator<Character> {

        private String string;

        private int index;

        public CharacterIterator(String string) {
            this.string = string;
        }

        @Override
        public boolean hasNext() {
            return index < string.length();
        }

        @Override
        public Character next() {
            char c = string.charAt(index);
            index++;
            return c;
        }
    }
}


