package com.example.grayview.activities.fragments.editfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.grayview.R;
import com.example.grayview.activities.fragments.EditImageFragment;

public class BrightnessFragment extends Fragment {

    private SeekBar seekBar;
    private TextView valueText;
    private EditImageFragment parentFragment;
    private int progress = 100; // valor padrão (100%)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_brightness, container, false);

        seekBar = root.findViewById(R.id.seek_brightness);
        valueText = root.findViewById(R.id.value_brightness);
        parentFragment = (EditImageFragment) getParentFragment();

        // 🔹 Inicializa com 100%
        seekBar.setProgress(progress);
        valueText.setText(progress + "%");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue; // guarda temporariamente
                valueText.setText(progress + "%");

                float brightnessValue = progress / 100f; // 100 = normal
                if (parentFragment != null) {
                    parentFragment.setBrightness(brightnessValue);
                }

                // 🔹 Não salva em SharedPreferences, só mantém na memória
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return root;
    }

    /** Retorna o valor atual do brilho enquanto o app está aberto */
    public int getCurrentProgress() {
        return progress;
    }
}
