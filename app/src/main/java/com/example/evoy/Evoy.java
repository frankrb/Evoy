package com.example.evoy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Evoy extends AppCompatActivity {
    Button gologin;
    TextView usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = findViewById(R.id.usuario);

        gologin = findViewById(R.id.gologin);

        gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Evoy.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        //vemos si existe algún usuario en las preferencias
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


    }
    private void cargarPreferencias() throws InterruptedException, ExecutionException, ParseException, IOException {
        SharedPreferences sharedpreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = sharedpreferences.getString("user","");
        String pass = sharedpreferences.getString("password","");
        if(user!="" && controladorBDWebService.getInstance().login(this,"login",user,pass)){
            usuario.setText(user);
        }
    }
}
