package com.excelsiormc.excelsiorsponge.excelsiorcore.services;

public class TimeFormatter {

    public static String getFormattedTime(int seconds) {
        return getFormattedTime(seconds / 60, seconds % 60);
    }

    public static String getFormattedTime(int minutes, int seconds) {
        String m = minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
        String s = seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds);
        return m + ":" + s;
    }
}
