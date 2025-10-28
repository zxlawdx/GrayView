package com.example.grayview.activities.fragments.editfragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.grayview.R;
import com.example.grayview.activities.fragments.EditImageFragment;
import com.example.grayview.process.ImageProcessor;

public class FiltersFragment extends Fragment {

    private EditImageFragment parentFragment;
    private final ImageProcessor processor = new ImageProcessor();
    private Bitmap originalBitmap;

    // Flags de filtros ativos
    private boolean grayActive = false;
    private boolean negativeActive = false;
    private boolean blurActive = false;
    private boolean stretchActive = false;
    private boolean bordersActive = false;

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "filters_prefs";

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_filters, container, false);
        parentFragment = (EditImageFragment) getParentFragment();

        if (parentFragment != null) {
            originalBitmap = parentFragment.getCurrentBitmap();
        }


        Toolbar toolbar = root.findViewById(R.id.filters_toolbar);
        toolbar.inflateMenu(R.menu.menu_filters);

        // Configura listener dos filtros
        toolbar.setOnMenuItemClickListener(item -> {
            if (!item.isCheckable()) return false;

            item.setChecked(!item.isChecked());
            toggleFilter(item.getItemId(), item.isChecked());
            return true;
        });

        return root;
    }

    /** Aplica ou remove um filtro, com carregamento */
    private void toggleFilter(int id, boolean enabled) {
        // Atualiza flags
        if (id == R.id.filter_gray) grayActive = enabled;
        else if (id == R.id.filter_negative) negativeActive = enabled;
        else if (id == R.id.filter_blur) blurActive = enabled;
        else if (id == R.id.filter_test_stretch) stretchActive = enabled;
        else if (id == R.id.filter_bordas) bordersActive = enabled;

        if (parentFragment == null || originalBitmap == null) return;

        // Mostra aviso
        Toast.makeText(requireContext(), "Aplicando filtro... Aguarde", Toast.LENGTH_SHORT).show();

        // Exibe dialog de carregamento
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Processando imagem...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Processamento em thread separada
        new Thread(() -> {
            Bitmap result = originalBitmap.copy(originalBitmap.getConfig(), true);

            if (grayActive) result = processor.toGray(result);
            if (negativeActive) result = processor.invertColors(result);
            if (blurActive) result = processor.simpleBlur(result);
            if (stretchActive) result = processor.stretchPerChannel(result);
            if (bordersActive) result = processor.edgeDetection(result);

            Bitmap finalResult = result;

            requireActivity().runOnUiThread(() -> {
                parentFragment.setCurrentBitmap(finalResult);
                if (progressDialog.isShowing()) progressDialog.dismiss();
                Toast.makeText(requireContext(), "Filtro aplicado com sucesso!", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
