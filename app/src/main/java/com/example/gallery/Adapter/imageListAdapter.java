package com.example.gallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.model.Photo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class imageListAdapter extends RecyclerView.Adapter<imageListAdapter.ViewHolder> {

    Context context;
    List<Photo> list;
    protected PhotoListener photoListener;

    public imageListAdapter(Context context, List<Photo> list, PhotoListener photoListener) {
        this.context = context;
        this.list = list;
        this.photoListener = photoListener;
    }

    @NonNull
    @NotNull
    @Override
    public imageListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //Inflating recycler_item.xml
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull imageListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUrlS()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> photoListener.onPhotoClick(list.get(position).getUrlS()));

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
        }
    }

    public interface PhotoListener {
        void onPhotoClick(String path);
    }
}
