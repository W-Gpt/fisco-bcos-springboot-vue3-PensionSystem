package com.ooyyh.top.util;

public class ColorFul {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static void print(String message, String color) {
        System.out.println(color + "[ColorFul.DEBUG]: ===>"+RESET+CYAN+message+RESET);
    }
    public static void error(String message) {
        System.out.println(RED+"[ColorFul.ERROR]: ===>"+RESET+RED+message+RESET);
    }
    public static void info(String message) {
        System.out.println(YELLOW+"[ColorFul.INFO]: ===>"+RESET+YELLOW+message+RESET);
    }
    public static void success(String message) {
        System.out.println(GREEN+"[ColorFul.SUCCESS]: ===>"+RESET+GREEN+message+RESET);
    }
}