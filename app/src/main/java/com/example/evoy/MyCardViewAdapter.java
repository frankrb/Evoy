package com.example.evoy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MyCardViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private int[] ids;
    private String[] names;
    private String[] details;
    private Bitmap[] imgs;
    private String[] locations;
    private String[] dates;
    private String[] creators;
    private Boolean[] followed;
    private int[] followers;
    Context context;

    public MyCardViewAdapter(String[] nombres, Bitmap[] imagenes, String[] lugares, Boolean[] follow, int[] losids, Context cont) {
        //TODO establecer los elementos a mostrar
        names = nombres;
        imgs = imagenes;
        locations = lugares;
        followed = follow;
        ids = losids;
        context = cont;
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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        ImageView img = myViewHolder.myImg;
        TextView name = myViewHolder.eventName;
        TextView location = myViewHolder.location;
        final ToggleButton tb = myViewHolder.tb;
        img.setImageBitmap(imgs[i]);
        name.setText(names[i]);
        location.setText(String.valueOf(locations[i]));
        tb.setChecked(followed[i]);

        //obtenemos el nombre del usuario de sharedpreferences

        SharedPreferences sharedpreferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        final String user = sharedpreferences.getString("user", "");


        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idEvent = ids[i];

                if(tb.isChecked()){
                    //a true -> esta siguiendo el evento
                    controladorBDWebService.getInstance().startFollow(context,user,idEvent);
                    Toast.makeText(context,"Ha empezado a seguir al evento",Toast.LENGTH_SHORT).show();
                }else{
                    //esta a false -> ha dejado de seguir el evento
                    controladorBDWebService.getInstance().stopFollow(context,user,idEvent);
                    Toast.makeText(context,"Ha dejado de seguir al evento",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }



    @Override
    public int getItemCount() {
        return imgs.length;
    }
}
