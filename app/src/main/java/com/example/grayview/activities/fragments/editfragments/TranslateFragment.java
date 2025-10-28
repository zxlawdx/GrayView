package com.example.grayview.activities.fragments.editfragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.grayview.R;
import com.example.grayview.activities.fragments.EditImageFragment;
import com.example.grayview.process.ImageProcessor;


public class TranslateFragment extends Fragment {

    private EditImageFragment parentFragment;
    private ImageProcessor processor = new ImageProcessor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_translate, container, false);

        // Pega o fragmento pai (EditImageFragment)
        parentFragment = (EditImageFragment) getParentFragment();


        Button rotate_90 = root.findViewById(R.id.btn_rotate_90);
        rotate_90.setOnClickListener(v -> {
            if (parentFragment != null) {
                Bitmap current = parentFragment.getCurrentBitmap();
                if (current != null) {
                    Bitmap inverted = processor.rotate(current, 90);
                    parentFragment.setCurrentBitmap(inverted);
                }
            }
        });

        Button rotate_45 = root.findViewById(R.id.btn_rotate_270);
        rotate_45.setOnClickListener(v -> {
            if (parentFragment != null) {
                Bitmap current = parentFragment.getCurrentBitmap();
                if (current != null) {
                    Bitmap inverted = processor.rotate(current, 270);
                    parentFragment.setCurrentBitmap(inverted);
                }
            }
        });

        Button rotate_180 = root.findViewById(R.id.btn_rotate_180);
        rotate_180.setOnClickListener(v -> {
            if (parentFragment != null) {
                Bitmap current = parentFragment.getCurrentBitmap();
                if (current != null) {
                    Bitmap inverted = processor.rotate(current, 180);
                    parentFragment.setCurrentBitmap(inverted);
                }
            }
        });

        return root;
    }
}