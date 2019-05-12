package com.example.evoy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        String nombre_evento = b.getString("NOMBRE_EVENTO");
        String desc_evento = b.getString("DESC_EVENTO");
        Bitmap image_evento = (Bitmap) b.get("IMAGE_EVENTO");
        String date_evento = b.getString("DATE_EVENTO");
        final String location_evento = b.getString("LOCATION_EVENTO");
        final String latitude_evento = b.getString("LATITUDE_EVENTO");
        final String longitude_evento = b.getString("LONGITUDE_EVENTO");

        //Gestionamos la fecha para obtener fecha y hora
        String[] dates = date_evento.split(" ");


        nombreEvento.setText(nombre_evento);
        descEvento.setText(desc_evento);
        imageEvento.setImageBitmap(image_evento);
        fechaEvento.setText(dates[0]);
        horaEvento.setText(dates[1]);

        locationEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapa = new Intent(v.getContext(), ShowLocationActivity.class);
                mapa.putExtra("location", location_evento);
                mapa.putExtra("latitude", latitude_evento);
                mapa.putExtra("longitude", longitude_evento);
                startActivity(mapa);
            }
        });

    }
}
