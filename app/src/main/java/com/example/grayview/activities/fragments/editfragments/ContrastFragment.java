package com.example.grayview.activities.fragments.editfragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.grayview.R;
import com.example.grayview.activities.fragments.EditImageFragment;

public class ContrastFragment extends Fragment {

    private static final String ARG_BITMAP = "bitmap_arg";
    private Bitmap originalBitmap;
    private SeekBar seekBar;
    private EditImageFragment parentFragment;


    public static ContrastFragment newInstance(Bitmap bitmap) {
        ContrastFragment fragment = new ContrastFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BITMAP, bitmap); // <-- aqui
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contrast, container, false);

        seekBar = root.findViewById(R.id.seek_contrast);
        parentFragment = (EditImageFragment) getParentFragment();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float contrastValue = progress / 100f; // 100 = normal, 50 = reduzido, 150 = mais forte
                if (parentFragment != null) {
                    parentFragment.setContrast(contrastValue);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return root;
    }
}
