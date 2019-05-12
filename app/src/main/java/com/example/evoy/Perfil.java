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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.simple.JSONObject;

import java.util.concurrent.ExecutionException;

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
        //int[] imgs= {R.drawable.american,R.drawable.flixus,R.drawable.got,R.drawable.wire};
        //Ejemplo TODO quitar cuando tengamos implementado hacerlo con servidor

        Bitmap got = BitmapFactory.decodeResource(getResources(),
                R.drawable.got);
        Bitmap wire = BitmapFactory.decodeResource(getResources(),
                R.drawable.wire);
        Bitmap[] imgs = {got, wire};
        String[] names = {"Game Of Thrones", "The Wire"};
        String[] followers = {"23", "44"};
        Boolean[] followed = {true,false};
        int[] ids = {0,1};
        MyCardViewAdapter myAdapter = new MyCardViewAdapter(names, imgs, followers, followed,ids,getContext());
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
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
