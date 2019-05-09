package com.example.evoy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    EditText user;
    EditText name;
    EditText surname;
    EditText email;
    EditText birth;
    EditText pass;
    EditText pass1;
    Button ok;

    SimpleDateFormat simpleDateFormat;
    String fecha = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        user=findViewById(R.id.ruser);
        name=findViewById(R.id.rname);
        surname=findViewById(R.id.rsurname);
        email=findViewById(R.id.remail);
        birth=findViewById(R.id.rbirth);
        pass=findViewById(R.id.rpass);
        pass1=findViewById(R.id.rpass1);
        ok= findViewById(R.id.okbtn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correcto = false;
                if(datosOK(user.getText().toString().trim(),name.getText().toString().trim(), surname.getText().toString().trim(), pass.getText().toString().trim(), pass1.getText().toString().trim(), email.getText().toString().trim(), birth.getText().toString().trim())) {
                    try {
                        correcto = controladorBDWebService.getInstance().insertarUsuario(getApplicationContext(),"insertarUsuario",user.getText().toString().trim(), name.getText().toString().trim(), surname.getText().toString().trim(), email.getText().toString().trim(), pass.getText().toString().trim(),birth.getText().toString().trim());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (correcto) {
                        finish();
                    }
                }
            }
        });

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });
    }

    public boolean datosOK(String user, String nombre, String apellidos, String contra, String contra1, String email, String fecha){
        boolean ok = false;
        Pattern usuario = Pattern.compile("[a-zA-Z0-9]{1,30}");
        Pattern nombre_apellido = Pattern.compile("[a-zA-Z]{1,30}");
        Pattern contrasena = Pattern.compile("[A-Za-z0-9_*@#%&!¡=.;,]{1,30}");
        Pattern email_pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher match1 = usuario.matcher(user);
        Matcher match2 = nombre_apellido.matcher(nombre);
        Matcher match3 = nombre_apellido.matcher(apellidos);
        Matcher match4 = contrasena.matcher(contra);
        Matcher match5 = email_pattern.matcher(email);

        if(user.length()>=1 && nombre.length()>=1 && apellidos.length()>=1 && contra.length()>=1){
            ok=true;
            if(match1.matches()){
                ok = true;
                if(match2.matches()){
                    ok=true;
                    if(match3.matches()){
                        ok=true;
                        if(match4.matches()){
                            ok=true;
                        }else{
                            Toast error = Toast.makeText(getApplicationContext(), "Cambie su Contraseña", Toast.LENGTH_SHORT);
                            error.show();
                            ok=false;
                        }
                    }else{
                        Toast error = Toast.makeText(getApplicationContext(), "Cambie su Apellido", Toast.LENGTH_SHORT);
                        error.show();
                        ok=false;
                    }
                }else{
                    Toast error = Toast.makeText(getApplicationContext(), "Cambie su Nombre", Toast.LENGTH_SHORT);
                    error.show();
                    ok=false;
                }
            }else{
                Toast error = Toast.makeText(getApplicationContext(), "Cambie el nombre de usuario", Toast.LENGTH_SHORT);
                error.show();
                ok=false;
            }
        }else{
            Toast error = Toast.makeText(getApplicationContext(), "No puede dejar campos vacíos", Toast.LENGTH_SHORT);
            error.show();
            ok=false;
        }
        if(ok){

            if(match5.find()){
                ok=true;
            }else{
                Toast error = Toast.makeText(getApplicationContext(), "Introduzca un email válido", Toast.LENGTH_SHORT);
                error.show();
                ok=false;
            }
        }
        if (ok) {
            if(fecha.equals("Fecha de Nacimiento")|| fecha.equals("")){
                Toast error = Toast.makeText(getApplicationContext(), "Introduzca una fecha", Toast.LENGTH_SHORT);
                error.show();
                ok = false;
            }else{
                ok = true;
            }
        }

        if(ok){
            if(contra.equals(contra1)){
                ok=true;
            }else{
                Toast error = Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir", Toast.LENGTH_SHORT);
                error.show();
                ok=false;
            }
        }

        return ok;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nom", String.valueOf(name.getText()));
        savedInstanceState.putString("apell", String.valueOf(surname.getText()));
        savedInstanceState.putString("us", String.valueOf(user.getText()));
        savedInstanceState.putString("em", String.valueOf(email.getText()));
        savedInstanceState.putString("cont1", String.valueOf(pass.getText()));
        savedInstanceState.putString("cont2", String.valueOf(pass1.getText()));
        savedInstanceState.putString("fecha", String.valueOf(birth.getText()));


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name.setText(savedInstanceState.getString("nom"));
        surname.setText(savedInstanceState.getString("apell"));
        user.setText(savedInstanceState.getString("us"));
        email.setText(savedInstanceState.getString("em"));
        pass.setText(savedInstanceState.getString("cont1"));
        pass1.setText(savedInstanceState.getString("cont2"));
        birth.setText(savedInstanceState.getString("fecha"));

    }

    private void datePickerDialog(){
        Calendar calendario=Calendar.getInstance();
        int anyo=calendario.get(Calendar.YEAR);
        int mes=calendario.get(Calendar.MONTH);
        int dia=calendario.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpck= new DatePickerDialog(Registro.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                simpleDateFormat = new SimpleDateFormat(year+"-"+ month +"-"+dayOfMonth);
                fecha = year+"-" + (month+1) +"-"+dayOfMonth;
                birth.setText(fecha);
            }
        }, anyo, mes, dia);
        dpck.show();
    }
}


