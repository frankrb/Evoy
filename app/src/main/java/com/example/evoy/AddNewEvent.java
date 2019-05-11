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
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class AddNewEvent extends AppCompatActivity {
    EditText nombre;
    EditText descripcion;
    ImageView location;
    TextView valorFecha;
    TextView valorHora;
    ImageView imagen;
    Button okBtn;
    String coordenadas="";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                            boolean correcto = controladorBDWebService.getInstance().insertarEvento(AddNewEvent.this,"insertarEvento",user,nombre.getText().toString().trim(),descripcion.getText().toString().trim(),coordenadas,horaTimestamp,fotoen64);
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

            }
        });


    }

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
