package com.newrelic;

public class Utils {

    public static <E> E verifyNonNull(E input, String message) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException(message);
        }
        return input;
    }

    public static String verifyNonBlank(String input, String message)
            throws IllegalArgumentException {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return input;
    }

    public static <E> E verifyNonNull(E input) throws IllegalArgumentException {
        return verifyNonNull(input, "input cannot be null");
    }
}
