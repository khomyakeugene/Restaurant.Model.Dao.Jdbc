package com.company.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Yevgen on 06.01.2016.
 */

public class Util {
    private final static String PLEASE_REPEAT_ENTER =
            "%s was generated with data \"%s\". Please, repeat enter action";

    private static void printLine(String message) {
        System.out.print(message);
    }

    public static void printMessage(String message) {
        printLine(message + "\n");
    }

    public static String readInputString(String enterMessageInvitation) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        printMessage(enterMessageInvitation);
        do {
            try {
                return bufferedReader.readLine();
            } catch (IOException e) {
                printMessage(String.format(PLEASE_REPEAT_ENTER, e.getClass().getName(), e.getMessage()));
            }
        } while (true);
    }

    public static String getApplicationMainClassName() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement main = stack[stack.length - 1];

        return main.getClassName();
    }

    public static Class getApplicationMainClass() {
        Class result;

        try {
            result = Class.forName(getApplicationMainClassName());
        } catch (ClassNotFoundException e) {
            // Unfortunately, it is difficult to recognize what could be the reason of such situation, but
            // now try to get it from stack directly
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StackTraceElement main = stack[stack.length - 1];
            result = main.getClass();
        }

        return result;
    }

    public static long getNanoTime() {
        return System.nanoTime();
    }

    public static Long nanoToMicroTime(Long nanoTime) {
        return (nanoTime == null) ? null : (nanoTime / 1000);
    }

    public static String toString(Object object) {
        String result;
        if (object == null) {
            result = "null";
        } else {
            result = object.toString();
            if (object instanceof String) {
                result =  "\"" + result + "\"";
            }
        }

        return result;
    }
}