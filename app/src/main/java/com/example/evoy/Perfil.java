package com.example.evoy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

//import com.google.firebase.messaging.FirebaseMessaging;

public class Perfil extends Fragment {

    private static final String TAG = "Perfil";

    TextView usuario;
    TextView nombreYapellidos;
    TextView email;
    FloatingActionButton addEventBtn;
    Button logout;
    Button suscripcion;

    JSONObject datos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_perfil);

    }

    //este método es como hacer logout
    private void eliminarPreferencias(){
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_perfil, container, false);
        RecyclerView feed = rootView.findViewById(R.id.myevents);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String user = prefs.getString("user", "");

        JSONArray results = null;

        int[] idEvents = null;

        try {

            results = controladorBDWebService.getInstance().getProfileFeed(this.getActivity(), user);
            idEvents = controladorBDWebService.getInstance().getFollowed(getActivity(),"getFollowed",user);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final int[] ids;
        final Bitmap[] imgs;
        final String[] names;
        final String[] locations;
        final String[] latitudes;
        final String[] longitudes;
        final String[] descriptions;
        final String[] dates;

        Boolean[] followed = null;

        if (!results.equals(null)) {

            ids = new int[results.size()];
            imgs = new Bitmap[results.size()];
            names = new String[results.size()];
            locations = new String[results.size()];
            followed = new Boolean[results.size()];

            latitudes = new String[results.size()];
            longitudes = new String[results.size()];
            descriptions = new String[results.size()];
            dates = new String[results.size()];

            for (int i = 0; i < results.size(); i++) {

                JSONObject tmp = (JSONObject) results.get(i);
                ids[i] = Integer.parseInt((String)tmp.get("id"));
                names[i] = (String) tmp.get("name");
                locations[i] = (String) tmp.get("location");
                latitudes[i] = (String) tmp.get("latitude");
                longitudes[i] = (String) tmp.get("longitude");
                descriptions[i] = (String) tmp.get("details");
                dates[i] = (String) tmp.get("date");
                String img64 = (String) tmp.get("image");
                ids[i]= Integer.parseInt((String) tmp.get("id"));
                InputStream stream = new ByteArrayInputStream(Base64.decode(img64.getBytes(), Base64.DEFAULT));
                Bitmap img = BitmapFactory.decodeStream(stream);
                imgs[i] = img;
                int idEvent = Integer.valueOf((String) tmp.get("id"));

                if(idEvents.equals(null)){
                    followed[i]=false;
                }else {

                    if (esta(idEvents,idEvent)) {
                        followed[i] = true;
                    } else {
                        followed[i] = false;
                    }

                }
            }
        } else {

            ids = new int[0];
            imgs = new Bitmap[0];
            names = new String[0];
            locations = new String[0];
            descriptions = new String[0];
            longitudes = new String[0];
            latitudes = new String[0];
            dates = new String[0];
        }

        MyCardViewAdapter myAdapter = new MyCardViewAdapter(names, imgs, locations, followed, ids,descriptions,dates,latitudes,longitudes,getContext());
        feed.setAdapter(myAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        feed.setLayoutManager(linearLayout);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        usuario = view.findViewById(R.id.puser);
        nombreYapellidos = view.findViewById(R.id.pnombre);
        email = view.findViewById(R.id.pemail);
        addEventBtn = view.findViewById(R.id.addEventBtn);
        logout = view.findViewById(R.id.logoutBtn);
        suscripcion = view.findViewById(R.id.suscripcionR);

        //obtenemos el nombre del usuario de sharedpreferences
        SharedPreferences sharedpreferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("user","");
        if(user==""){
            Intent i = new Intent(getContext(), Login.class);
            startActivity(i);
        }

        try {
            datos = controladorBDWebService.getInstance().getDatos(getContext(),"getDatos",user);
            usuario.setText("Usuario: "+datos.get("usuario").toString());
            nombreYapellidos.setText("Nombre: "+datos.get("nombre").toString()+" "+datos.get("apellidos").toString());
            email.setText("Email: "+datos.get("email").toString());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AddNewEvent.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPreferencias();
                Intent i = new Intent(getContext(), Login.class);
                startActivity(i);
            }
        });
        /**
        suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Suscribiendose al servicio de recomendaciones");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("recomendacion")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Suscrito al servicio de recomendaciones!";
                                if (!task.isSuccessful()) {
                                    msg = "Error al suscribirse al servicio de recomendaciones!";
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END subscribe_topics]
            }
        });**/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    private boolean esta(int[] idEvents, int idEvent) {
        boolean esta = false;
        int i = 0;
        while(i<idEvents.length){
            if (idEvents[i]==idEvent){
                esta = true;
                break;
            }
            i++;
        }
        return esta;
    }
}
