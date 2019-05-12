package com.example.evoy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**Actividad que se encarga de añadir un nuevo evento a la bbdd**/
public class AddNewEvent extends AppCompatActivity {

    EditText nombre;
    EditText descripcion;
    static final int REQUEST_MAP_LOCATION = 2;
    TextView valorFecha;
    LinearLayout hora;
    TextView valorHora;
    ImageView imagen;
    ImageButton location;
    Button okBtn;
    TextView valorLocation;
    String location_name = "";
    double latitude;
    String date="";
    String hour="";
    String horaTimestamp="";
    String fotoen64 = "";
    String user = "";
    SimpleDateFormat simpleDateFormat;

    private static final String CERO = ":00";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();

    final int horaact = c.get(Calendar.HOUR_OF_DAY);
    final int minutoact = c.get(Calendar.MINUTE);

    static final int REQUEST_IMAGE_CAPTURE = 1;

    double longitude;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //obtenemos la imagen
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Uri uri=data.getData();
            Bitmap imageBitmap = null;
            try {

                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //reducimos su tamaño
            int anchoDestino = imagen.getWidth();
            int altoDestino = imagen.getHeight();

            int anchoImagen = imageBitmap.getWidth();
            int altoImagen = imageBitmap.getHeight();

            float ratioImagen = (float) anchoImagen / (float) altoImagen;
            float ratioDestino = (float) anchoDestino / (float) altoDestino;
            int anchoFinal = anchoDestino;
            int altoFinal = altoDestino;
            if (ratioDestino > ratioImagen) {
                anchoFinal = (int) ((float)altoDestino * ratioImagen);
            } else {
                altoFinal = (int) ((float)anchoDestino / ratioImagen);
            }

            Bitmap newBitmap = Bitmap.createScaledBitmap(imageBitmap,anchoFinal,altoFinal,true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            imagen.setImageBitmap(newBitmap);

            byte[] fototransformada = stream.toByteArray();

            fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);
        }
        //obtenemos los valores de la localización
        if (requestCode == REQUEST_MAP_LOCATION) {
            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng myPlace = place.getLatLng();
                latitude = myPlace.latitude;
                longitude = myPlace.longitude;
                location_name = place.getName();
                Log.i("Google Places", "Place: " + place.getName() + ", " + place.getId());
                Log.d("Places: ", place.getName());
                Log.d("Latitude: ", String.valueOf(latitude));
                Log.d("Longitude: ", String.valueOf(longitude));
                valorLocation.setText(place.getName());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Google Places", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_event);

        //inicializamos
        nombre = findViewById(R.id.ename);
        descripcion = findViewById(R.id.edetails);
        location = findViewById(R.id.imageButton);
        valorFecha = findViewById(R.id.fecha);
        valorHora = findViewById(R.id.hora);
        imagen = findViewById(R.id.foto);
        okBtn = findViewById(R.id.okBtn2);
        valorLocation = findViewById(R.id.textView5);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        imagen.setImageResource(R.drawable.picture);

        //obtenemos el nombre del usuario de sharedpreferences
        SharedPreferences sharedpreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        user = sharedpreferences.getString("user","");

        if(user==""){

            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombre.getText().toString().trim().equals("") && !descripcion.getText().toString().trim().equals("")){

                    try {
                        if(!date.equals("")&& !hour.equals("")){

                            horaTimestamp=date+" "+hour+CERO;
                            String lat = String.valueOf(latitude);
                            String lon = String.valueOf(longitude);
                            //añadimos los valores del evento a la bbdd
                            boolean correcto = controladorBDWebService.getInstance().insertarEvento(AddNewEvent.this, "insertarEvento", user, nombre.getText().toString().trim(), descripcion.getText().toString().trim(), location_name, lat, lon, horaTimestamp, fotoen64);

                            if(correcto){

                                finish();

                            }else{

                                Toast.makeText(AddNewEvent.this,"Error al añadir evento a la BBDD",Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            Toast.makeText(AddNewEvent.this,"Debe de poner una fecha y una hora al evento",Toast.LENGTH_SHORT).show();

                        }


                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{

                    Toast.makeText(AddNewEvent.this,"Debe de poner un nombre y descripción al evento",Toast.LENGTH_SHORT).show();
                }
            }
        });

        valorFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });

        valorHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourPicker();
            }
        });

        /**añadir la imagen desde la galería**/
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Seleccione la foto del evento"),REQUEST_IMAGE_CAPTURE);

            }
        });

        /**añadir la localizacion desde la maps**/
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent mapsIntent = new Intent(getApplicationContext(), ShowLocationActivity.class);
                // startActivityForResult(mapsIntent,REQUEST_MAP_LOCATION);
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getApplicationContext());
                startActivityForResult(intent, REQUEST_MAP_LOCATION);

            }
        });


    }

    /**event picker para seleccionar la fecha del evento**/
    private void datePickerDialog(){

        Calendar calendario=Calendar.getInstance();
        int anyo=calendario.get(Calendar.YEAR);
        int mes=calendario.get(Calendar.MONTH);
        int dia=calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpck= new DatePickerDialog(AddNewEvent.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                simpleDateFormat = new SimpleDateFormat(year+"-"+ month +"-"+dayOfMonth);
                date = year+"-" + (month+1) +"-"+dayOfMonth;
                valorFecha.setText(date);
            }
        }, anyo, mes, dia);
        dpck.show();
    }

    /**event picker para seleccionar la hora del evento**/
    private void hourPicker(){

        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaFormateada =  Integer.toString(hourOfDay);

                String minutoFormateado = Integer.toString(minute);

                hour=horaFormateada + DOS_PUNTOS + minutoFormateado;
                //Muestro la hora con el formato deseado
                valorHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);

            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, horaact, minutoact, true);

        recogerHora.show();
    }


}
