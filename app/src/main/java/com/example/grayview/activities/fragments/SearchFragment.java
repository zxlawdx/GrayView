package com.example.grayview.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grayview.R;
import com.example.grayview.models.Imagem;
import com.example.grayview.models.ResultadoPixabay;
import com.example.grayview.services.APIservice;
import com.example.grayview.services.Adapter;
import com.example.grayview.services.RetrofitClientInstance;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private TextInputLayout search;
    private RecyclerView list;
    private List<Imagem> imageList = new ArrayList<>();
    private Adapter adapter;
    private ImageButton btnSearch;
    private APIservice apiservice;

    public SearchFragment() {
        // Construtor vazio obrigatório
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Infla o layout do fragment (usa o mesmo XML da Activity)
        View view = inflater.inflate(R.layout.search_activity, container, false);

        // Inicializa os componentes
        search = view.findViewById(R.id.search_bar);
        list = view.findViewById(R.id.recycler_pesquisa);
        btnSearch = view.findViewById(R.id.btnSearch);

        adapter = new Adapter(imageList);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        apiservice = RetrofitClientInstance.getRetrofitInstance().create(APIservice.class);

        btnSearch.setOnClickListener(v -> {
            String searchText = search.getEditText().getText().toString();

            if (searchText.isEmpty()) {
                Toast.makeText(getContext(), "Preencha o campo de pesquisa", Toast.LENGTH_SHORT).show();
                return;
            }

            Call<ResultadoPixabay> call = apiservice.getImages("52904097-4fedbe5a813894fff0847ff4b", searchText);
            call.enqueue(new Callback<ResultadoPixabay>() {
                @Override
                public void onResponse(Call<ResultadoPixabay> call, Response<ResultadoPixabay> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        imageList.clear();
                        imageList.addAll(response.body().hits);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ResultadoPixabay> call, Throwable t) {
                    Toast.makeText(getContext(), "Erro na requisição", Toast.LENGTH_SHORT).show();
                    Log.e("PIXABAY", "Erro na requisição", t);
                }
            });
        });

        return view;
    }
}
