package com.example.grayview.activities.fragments.editfragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grayview.R;

import java.io.IOException;

public class AddImageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        // Assim que o fragment for aberto, já abre a galeria:
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);

                Fragment parent = getParentFragment();
                if (parent instanceof com.example.grayview.activities.fragments.EditImageFragment) {
                    ((com.example.grayview.activities.fragments.EditImageFragment) parent).setCurrentBitmap(bitmap);
                    // Atualiza a imagem no ImageView do pai
                    ((com.example.grayview.activities.fragments.EditImageFragment) parent).setCurrentBitmap(bitmap);
                }

                // Volta pro editor de brilho, por exemplo

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
