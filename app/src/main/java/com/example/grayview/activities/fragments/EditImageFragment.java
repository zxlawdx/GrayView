package com.example.grayview.activities.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.grayview.R;
import com.example.grayview.activities.fragments.editfragments.AddImageFragment;
import com.example.grayview.activities.fragments.editfragments.BrightnessFragment;
import com.example.grayview.activities.fragments.editfragments.ContrastFragment;
import com.example.grayview.activities.fragments.editfragments.NegativeFilterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditImageFragment extends Fragment {

    private ImageView imageEdit;
    private Bitmap currentBitmap;

    public EditImageFragment() {}

    public static EditImageFragment newInstance(String param1, String param2) {
        EditImageFragment fragment = new EditImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageEdit = view.findViewById(R.id.edit_image);
        imageEdit.setImageResource(R.drawable.layout);

        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);

        // Define o primeiro fragmento padrão (exemplo: brilho)
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.edit_fragment_container, new BrightnessFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> onBottomNavItemSelected(item));
    }

    private boolean onBottomNavItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.edit_brilho) {
            selectedFragment = new BrightnessFragment();
        } else if (id == R.id.edit_contraste) {
            selectedFragment = new ContrastFragment();
        }
        else if (id == R.id.edit_cortar) {

        }
        else if (id == R.id.add_image) {
            selectedFragment = new AddImageFragment();
        }
        else if (id == R.id.edit_negativo) {
            selectedFragment = new NegativeFilterFragment();
        }

        if (selectedFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.edit_fragment_container, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }

    // ✅ Contraste realista (como no Octave)
    public void setContrast(float contrast) {
        if (imageEdit == null) return;

        // Mapeia 0%–200% (SeekBar 0–200) para faixa -100 a +100
        // 100% (padrão) = contraste normal
        float scale = contrast; // contraste vindo do SeekBar (0 a 2.0)
        float translate = (-0.5f * scale + 0.5f) * 255f * (1f - scale);

        ColorMatrix matrix = new ColorMatrix(new float[]{
                scale, 0, 0, 0, translate,
                0, scale, 0, 0, translate,
                0, 0, scale, 0, translate,
                0, 0, 0, 1, 0
        });

        imageEdit.setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    public void setCurrentBitmap(Bitmap bitmap) {
        this.currentBitmap = bitmap;
        if (imageEdit != null) {
            imageEdit.setImageBitmap(bitmap);
        }
    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }
}
