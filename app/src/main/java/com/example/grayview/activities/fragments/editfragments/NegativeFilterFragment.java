package com.example.grayview.activities.fragments.editfragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.grayview.R;
import com.example.grayview.activities.fragments.EditImageFragment;
import com.example.grayview.process.ImageProcessor;

public class NegativeFilterFragment extends Fragment {

    private EditImageFragment parentFragment;
    private ImageProcessor processor = new ImageProcessor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_negative_filter, container, false);

        // Pega o fragmento pai (EditImageFragment)
        parentFragment = (EditImageFragment) getParentFragment();

        // Botão para aplicar negativo
        Button btnApply = root.findViewById(R.id.btn_apply_negative);
        btnApply.setOnClickListener(v -> {
            if (parentFragment != null) {
                Bitmap current = parentFragment.getCurrentBitmap();
                if (current != null) {
                    Bitmap inverted = processor.invertColors(current);
                    parentFragment.setCurrentBitmap(inverted);
                }
            }
        });

        // Botão para resetar imagem
        Button btnReset = root.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(v -> {
            if (parentFragment != null) {
                Bitmap current = parentFragment.getCurrentBitmap();
                if (current != null) {
                    current = processor.invertColors(current);
                    parentFragment.setCurrentBitmap(current);
                }
;
            }
        });

        return root;
    }
}
