package com.example.app_lugares_turisticos;

import android.widget.TextView;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    public static LocalTime getTime(TextView textView) {
        return LocalTime.parse(textView.getText(), formatter);
    }

    public static void setTime(TextView textView, LocalTime time) {
        textView.setText(time.format(formatter));
    }

    public static String toTimeText(LocalTime time) {
        return time.format(formatter);
    }

    public static double hoursBetween(LocalTime start, LocalTime end) {
        return Duration.between(start, end).toMinutes() / 60.0;
    }
}
