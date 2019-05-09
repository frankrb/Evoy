package com.example.evoy;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCardViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private int[] ids;
    private String[] names;
    private String[] details;
    private Bitmap[] imgs;
    private String[] locations;
    private String[] dates;
    private String[] creators;
    private int[] followers;

    public MyCardViewAdapter(String[] nombres, Bitmap[] imagenes, int[] seguidores) {
        //TODO establecer los elementos a mostrar
        names = nombres;
        imgs = imagenes;
        followers = seguidores;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myCardLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycardview, null);
        MyViewHolder mvh = new MyViewHolder(myCardLayout);
        return mvh;
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ImageView img = myViewHolder.myImg;
        TextView name = myViewHolder.eventName;
        TextView followerz = myViewHolder.followers;
        img.setImageBitmap(imgs[i]);
        name.setText(names[i]);
        followerz.setText(String.valueOf(followers[i]));
    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }
}
