package com.example.evoy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**Actividad que muestra un evento con sus datos y descripciones**/
public class DescripcionEventoActivity extends AppCompatActivity {

    TextView nombreEvento;
    TextView fechaEvento;
    TextView horaEvento;
    TextView descEvento;
    ImageView imageEvento;
    ImageButton locationEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        nombreEvento =  findViewById(R.id.nombreE);
        descEvento =  findViewById(R.id.descE);
        fechaEvento = findViewById(R.id.fechaE);
        horaEvento = findViewById(R.id.horaE);
        imageEvento =  findViewById(R.id.imagenE);
        locationEvento =  findViewById(R.id.locationE);

        //Obtenemos los extras del Bundle para posteriormente sacar el Usuario actual.
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String datosEvento = b.getString("DATOS_EVENTO");
        String[] datosDesglosados = datosEvento.split("&");
        System.out.println("ID DEL EVENTO:"+datosDesglosados[6]);
        //Cargamos la imagen


        Bitmap img64 = null;
        try {
            img64 = controladorBDWebService.getInstance().getImage(this,datosDesglosados[6]);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String nombre_evento = datosDesglosados[0];
        String desc_evento = datosDesglosados[1];
        String date_evento = datosDesglosados[2];
        final String location_evento = datosDesglosados[3];
        final String latitude_evento =datosDesglosados[4];
        final String longitude_evento = datosDesglosados[5];

        //Gestionamos la fecha para obtener fecha y hora
        String[] dates = date_evento.split(" ");


        nombreEvento.setText(nombre_evento);
        descEvento.setText(desc_evento);
        imageEvento.setImageBitmap(img64);
        fechaEvento.setText(dates[0]);
        horaEvento.setText(dates[1]);

        locationEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //muestra un mapa con la localización del evento
                Intent mapa = new Intent(v.getContext(), ShowLocationActivity.class);
                mapa.putExtra("location", location_evento);
                mapa.putExtra("latitude", latitude_evento);
                mapa.putExtra("longitude", longitude_evento);
                startActivity(mapa);
            }
        });

    }
}
