package com.example.grayview.activities.fragments;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.grayview.R;
import com.example.grayview.activities.fragments.editfragments.AddImageFragment;
import com.example.grayview.activities.fragments.editfragments.BrightnessFragment;
import com.example.grayview.activities.fragments.editfragments.ContrastFragment;
import com.example.grayview.activities.fragments.editfragments.FiltersFragment;
import com.example.grayview.activities.fragments.editfragments.TranslateFragment;
import com.example.grayview.process.ImageProcessor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditImageFragment extends Fragment {

    private ImageView imageEdit;
    private Bitmap originalBitmap;  // imagem base
    private Bitmap currentBitmap;   // imagem atual exibida

    // Estado dos filtros
    private float currentBrightness = 0f;
    private float currentContrast = 1f;
    private boolean isNegative = false;

    private final ImageProcessor processor = new ImageProcessor();

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
        
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.edit_fragment_container, new BrightnessFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(this::onBottomNavItemSelected);
    }

    private boolean onBottomNavItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.edit_brilho) {
            selectedFragment = new BrightnessFragment();
        } else if (id == R.id.edit_contraste) {
            selectedFragment = new ContrastFragment();
        } else if (id == R.id.add_image) {
            selectedFragment = new AddImageFragment();
        } else if (id == R.id.edit_negativo) {
            selectedFragment = new FiltersFragment();
        } else if (id == R.id.translate) {
            selectedFragment =  new TranslateFragment();
        }
        if (selectedFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.edit_fragment_container, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }

    // ================== CONTROLE DE FILTROS ==================

    /** Atualiza brilho */
    public void setBrightness(float brightness) {
        this.currentBrightness = brightness;
        applyAllFilters();
    }

    /** Atualiza contraste */
    public void setContrast(float contrast) {
        this.currentContrast = contrast;
        applyAllFilters();
    }

    /** Alterna negativo */
    public void setNegative(boolean enabled) {
        this.isNegative = enabled;
        applyAllFilters();
    }

    /** Aplica todos os filtros sobre a imagem original */
    private void applyAllFilters() {
        if (originalBitmap == null || imageEdit == null) return;

        Bitmap bmp = originalBitmap.copy(originalBitmap.getConfig(), true);

        // Aplica negativo se ativo
        if (isNegative) {
            bmp = processor.invertColors(bmp);
        }

        // Combina brilho e contraste
        ColorMatrix combined = new ColorMatrix();
        combined.postConcat(processor.contrast(currentContrast));
        combined.postConcat(processor.brightness(currentBrightness));

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(combined);
        imageEdit.setImageBitmap(bmp);
        imageEdit.setColorFilter(filter);

        currentBitmap = bmp;
    }

    // ================== CONTROLE DE IMAGEM ==================

    /** Define a imagem atual e reseta filtros */
    public void setCurrentBitmap(Bitmap bitmap) {
        if (bitmap == null) return;
        this.originalBitmap = bitmap;
        this.currentBitmap = bitmap.copy(bitmap.getConfig(), true);

        resetFilters();
        imageEdit.setImageBitmap(currentBitmap);
        imageEdit.clearColorFilter();
    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    /** Reseta todos os filtros para o estado original */
    public void resetFilters() {
        currentBrightness = 0f;
        currentContrast = 1f;
        isNegative = false;

        if (originalBitmap != null) {
            currentBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
            imageEdit.setImageBitmap(currentBitmap);
            imageEdit.clearColorFilter();
        }
    }
}
