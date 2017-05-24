package uk.co.malbec.welcometohell.wizard.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Serialization {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Object deserialize(Type type, String value){
        if (Type.BOOLEAN.equals(type)){
            return Boolean.parseBoolean(value);
        } else if (Type.INTEGER.equals(type)) {
            return Integer.parseInt(value);
        } else if (Type.DATE.equals(type)){
            try {
                return dateFormat.parse(value);
            } catch (ParseException e) {
                throw new RuntimeException("invalid date format", e);
            }
        } else {
            return value;
        }
    }

    public static String serialize(Type type, Object value){
        if (Type.BOOLEAN.equals(type)){
            return Boolean.toString((Boolean) value);
        } else if (Type.INTEGER.equals(type)){
            return Integer.toString((Integer) value);
        } else if (Type.DATE.equals(type)){
            return dateFormat.format((Date) value);
        } else {
            return value.toString();
        }
    }
}
