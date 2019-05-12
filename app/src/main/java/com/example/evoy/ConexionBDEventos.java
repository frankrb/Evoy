package com.example.evoy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class ConexionBDEventos extends AsyncTask<Void, Void, JSONArray> {

    Context context;
    String direccion;
    String username;
    String operacion;

    public ConexionBDEventos(Context cont, String user) {
        context = cont;
        username = user;

        direccion = "https://134.209.235.115/framos001/WEB/evoy/allEvents.php";
    }

    public ConexionBDEventos(Context cont, String oper, String user) {
        context=cont;
        username=user;
        operacion = oper;
        if(operacion.equals("getProfileFeed")) {
            direccion = "https://134.209.235.115/framos001/WEB/evoy/getProfileFeed.php";
        }else if(operacion.equals("getFollowsFeed")){
            direccion = "https://134.209.235.115/framos001/WEB/evoy/getFollowsFeed.php";
        }else{
            direccion = "https://134.209.235.115/framos001/WEB/evoy/allEvents.php";
        }
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        JSONArray json = null;

        HttpsURLConnection urlConnection = null;
        try {
            //establece una conexion HTTPS
            urlConnection = GeneradorConexionesSeguras.getInstance().crearConexionSegura(context, direccion);

            //inicializamos los parametros de la petición
            String parametros = "iduser=" + username;

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);//necesario por el método POST u PUT
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //incluimos los parámetros en la conexion
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();

            int statusCode = urlConnection.getResponseCode();
            Log.d("StatusCode", String.valueOf(statusCode));
            if (statusCode == 200) {
                //guardamos los resultados en el json
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();

                System.out.println("********************" + "\n" + result + "\n************************");

                JSONParser parser = new JSONParser();
                json = (JSONArray) parser.parse(result);

                inputStream.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return json;
    }
}
