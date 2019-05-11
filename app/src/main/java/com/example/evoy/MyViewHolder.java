package com.example.evoy;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView eventName;
    ImageView myImg;
    TextView location;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        eventName = itemView.findViewById(R.id.textView6);
        myImg = itemView.findViewById(R.id.imageView5);
        location = itemView.findViewById(R.id.textView13);
    }
}
