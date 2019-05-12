package com.example.evoy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    EditText user;
    EditText pass;
    Button login;
    Button register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_login2);

        user = findViewById(R.id.usuario);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.registro);

        try {
            cargarPreferencias();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, BottomNavActivity.class);

                try {
                    if(login(user.getText().toString().trim(),pass.getText().toString().trim())){
                        guardarpreferencias(user.getText().toString().trim(),pass.getText().toString().trim());
                        //i.putExtra("user",user.getText().toString().trim());
                        startActivity(i);
                        finish();

                    }else{
                        Toast error=Toast.makeText(getApplicationContext(), getText(R.string.error_form), Toast.LENGTH_SHORT);
                        error.show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Registro.class);
                startActivity(i);
            }
        });
    }



    private void guardarpreferencias(String user, String pass) {
        //creamos un archivo xml con las preferencias
        SharedPreferences sharedpreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = user;
        String contra = pass;
        //insertamos los datos que queramos
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user",usuario);
        editor.putString("password",contra);
        editor.commit();

    }


    private void cargarPreferencias() throws InterruptedException, ExecutionException, ParseException, IOException {
        SharedPreferences sharedpreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = sharedpreferences.getString("user","");
        String pass = sharedpreferences.getString("password","");
        if(user!="" && controladorBDWebService.getInstance().login(this,"login",user,pass)){
            //usuario.setText(user);
            Intent i = new Intent(Login.this,BottomNavActivity.class);
            startActivity(i);
            finish();
        }
    }


    private boolean login(String user, String pass) throws InterruptedException, ExecutionException, ParseException, IOException {
        boolean correcto;

        correcto = controladorBDWebService.getInstance().login(this,"login",user,pass);

        return correcto;
    }


}
