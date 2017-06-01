package com.github.robindevilliers.cascade.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static <T> Map<String, ?> map(String name, T o) {
        Map<String, T> map = new HashMap<>();
        map.put(name, o);
        return map;
    }

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //ignore
        }
    }

    public static void printException(String message, Throwable f){
        StringWriter stringWriter = new StringWriter();
        stringWriter.append("------------ CASCADE ERROR -----------------\n");
        stringWriter.append("\n");
        stringWriter.append(message).append("\n");
        stringWriter.append("\n");
        f.printStackTrace(new PrintWriter(stringWriter));
        stringWriter.append("--------------------------------------------");

        System.err.println(stringWriter.toString());
    }

    @FunctionalInterface
    public interface ExceptionalRunnable<T> {
        void doIt() throws Exception;
    }

    public static void wrapChecked(ExceptionalRunnable runnable){
        try {
            runnable.doIt();
        } catch (Exception e){
            if (e instanceof RuntimeException){
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}
