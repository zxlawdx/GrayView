package com.example.grayview.services;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.grayview.R;
import com.example.grayview.models.Imagem;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.viewholder>{

    private List<Imagem> imageList = new ArrayList<>();
    private List<String> ListImageNumber = new ArrayList<>();

    public Adapter(List<Imagem> imageList){
        this.imageList = imageList;
    }




    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        return new viewholder(item);
    }

    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        // Exibir texto (se quiser)
        holder.autor.setText(imageList.get(position).getUserName());

        // Carregar imagem com Glide
        Glide.with(holder.itemView.getContext())
                .load(imageList.get(position).getPreviewURL())
                .into(holder.image);

        Glide.with(holder.itemView.getContext())
                .load(imageList.get(position).getUserImage())
                .transform(new CenterCrop(), new RoundedCorners(24))
                .into(holder.imageUser);

        holder.buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManagement.downloadImage(
                        holder.itemView.getContext(),
                        imageList.get(position).getLargeImageURL()
                );
            }
        });
    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView autor;
        ImageView image;
        ImageView imageUser;
        Button buttonDownload;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            autor = itemView.findViewById(R.id.autor);
            imageUser = itemView.findViewById(R.id.imageAutor);
            buttonDownload = itemView.findViewById(R.id.buttonDownload);
        }
    }

}
