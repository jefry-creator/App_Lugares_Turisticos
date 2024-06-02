package com.example.app_lugares_turisticos;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalTime;

public class TimePicker extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener timeObserver;
    private int hour;
    private int minute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hour = getArguments().getInt("args.hour");
            minute = getArguments().getInt("args.minute");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Crear el TimePickerDialog con el tema personalizado
        Context context = requireContext();
        ContextThemeWrapper themedContext = new ContextThemeWrapper(context, R.style.Theme_TimePickerEnAndroid);
        return new TimePickerDialog(themedContext, timeObserver, hour, minute, false);
    }

    public static TimePicker newInstance(int hour, int minute, TimePickerDialog.OnTimeSetListener observer) {
        Bundle args = new Bundle();
        args.putInt("args.hour", hour);
        args.putInt("args.minute", minute);

        TimePicker fragment = new TimePicker();
        fragment.setArguments(args);
        fragment.setTimeObserver(observer);
        return fragment;
    }

    public void setTimeObserver(TimePickerDialog.OnTimeSetListener timeObserver) {
        this.timeObserver = timeObserver;
    }
}

