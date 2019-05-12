package com.example.evoy;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class conexionBDWebService extends AsyncTask<Void, Void, JSONObject> {
    String direccion = "https://134.209.235.115/framos001/WEB/php/usuarios.php";
    String usuario="";
    String nombre="";
    String apellidos="";
    String email="";
    String contrasena="";
    int idEvent;
    int altura;
    String fechaNac="";
    String sexo="";
    String ejercicio="";
    Context context;
    String operacion="";
    String token="";
    String foto="";
    String titulo="";
    String eName = "";
    String eDescription ="";
    String eLocation = "";
    String latitude;
    String longitude;
    String eDate = "";


    public conexionBDWebService(Context applicationContext, String saveImg, String nombreUsuario, String fotoen64, String tit) {
        context=applicationContext;
        operacion=saveImg;
        usuario=nombreUsuario;
        foto = fotoen64;
        titulo=tit;
    }

    public conexionBDWebService(Context cont, String oper, String usr, String nom, String apell, String em, String contra, String birth) {
        //insertar usuairo evoy
        context = cont;
        operacion = oper;
        usuario = usr;
        nombre = nom;
        apellidos = apell;
        email = em;
        contrasena = contra;
        fechaNac = birth;
    }

    public conexionBDWebService(Context cont, String oper, String usr, String event_name, String description, String location, String lat, String lon, String fecha, String foto64) {
        //insertar evento
        context = cont;
        operacion = oper;
        usuario = usr;
        eName = event_name;
        eDescription = description;
        eLocation = location;
        latitude = lat;
        longitude = lon;
        eDate = fecha;
        foto = foto64;
    }

    public conexionBDWebService(Context cont, String oper, String usr) {
        //GET ALL FEED
        context = cont;
        operacion = oper;
        usuario = usr;
    }

    public conexionBDWebService(Context cont, String oper, String usr, String param) {
        context = cont;
        operacion = oper;
        usuario = usr;
        contrasena = param;
        ejercicio = param;
        token = param;
    }

    public conexionBDWebService(Context cont, String oper, String usr, int p) {
        context = cont;
        operacion = oper;
        usuario = usr;
        idEvent = p;
    }


    /**Metodo que devuelve un json con los resultados de las distintas peticiones
     * **/
    @Override
    protected JSONObject doInBackground(Void... voids) {

        JSONObject json= null;

        HttpsURLConnection urlConnection = null;
        switch(operacion) {
            case "login":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/login.php";
                break;
            case "insertarEvento":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/insertarEvento.php";
                break;
            case "getDatos":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/getDatos.php";
                break;
            case "insertarUsuario":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/insertarUsuario.php";
                break;
            case "getAllFeed":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/allEvents.php";
                break;
            case "getFollowed":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/getFollowed.php";
                break;
            case "startFollow":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/startFollow.php";
                break;
            case "stopFollow":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/stopFollow.php";
                break;
            case "getImage":
                direccion = "https://134.209.235.115/framos001/WEB/evoy/getImg.php";
                break;
            default:
                break;
        }


        try {
            //establece una conexion HTTPS
            urlConnection = GeneradorConexionesSeguras.getInstance().crearConexionSegura(context, direccion);
            if(!operacion.equals("insertarEvento")) {
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
            }
            //inicializamos los parametros de la petición
            String parametros ="";
            switch (operacion) {
                case "insertarEvento":
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("iduser", usuario).appendQueryParameter("name", eName).appendQueryParameter("details", eDescription).appendQueryParameter("datea", eDate).appendQueryParameter("location", eLocation).appendQueryParameter("lat", latitude).appendQueryParameter("lon", longitude).appendQueryParameter("image", foto);
                    parametros = builder.build().getEncodedQuery();
                    break;
                case "getAllFeed":
                    parametros = "iduser=" + usuario;
                    break;
                case "getDatos":
                    parametros = "user=" + usuario + "&password=" + contrasena + "&birth=" + fechaNac + "&name=" + nombre + "&surname=" + apellidos + "&email=" + email + "&token=" + token + "&foto=" + foto + "&titulo=" + titulo;
                    break;
                case "login":
                    parametros = "user=" + usuario + "&password=" + contrasena + "&birth=" + fechaNac + "&name=" + nombre + "&surname=" + apellidos + "&email=" + email + "&token=" + token + "&foto=" + foto + "&titulo=" + titulo;
                    break;
                case "insertarUsuario":
                    parametros = "user=" + usuario + "&password=" + contrasena + "&birth=" + fechaNac + "&name=" + nombre + "&surname=" + apellidos + "&email=" + email + "&token=" + token + "&foto=" + foto + "&titulo=" + titulo;
                    break;
                case "getFollowed":
                    parametros = "user=" + usuario;
                    break;
                case "startFollow":
                    parametros = "user=" + usuario + "&idEvent=" + idEvent;
                    break;
                case "stopFollow":
                    parametros = "user=" + usuario + "&idEvent=" + idEvent;
                    break;
                case "getImage":
                    parametros = "id=" + usuario;
                    break;
                default:
                    parametros = "";
                    break;
            }
            /*
            if(!operacion.equals("insertarEvento")) {
                //parámetros que le pasamos al php
                 parametros = "user=" + usuario + "&password=" + contrasena  + "&birth=" + fechaNac + "&name=" + nombre + "&surname=" + apellidos + "&email=" + email + "&token=" + token + "&foto=" + foto + "&titulo=" + titulo;
            }else{
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("iduser", usuario).appendQueryParameter("name", eName).appendQueryParameter("details", eDescription).appendQueryParameter("datea", eDate).appendQueryParameter("location", eLocation).appendQueryParameter("lat", latitude).appendQueryParameter("lon", longitude).appendQueryParameter("image", foto);
                 parametros = builder.build().getEncodedQuery();
            }*/

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);//necesario por el método POST u PUT
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //incluimos los parámetros en la conexion
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();

            int statusCode = urlConnection.getResponseCode();
            Log.d("StatusCode", String.valueOf(statusCode));
            if (statusCode == 200){
                //guardamos los resultados en el json
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line, result="";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                inputStream.close();

                System.out.println("********************"+operacion+"\n"+result+"\n************************");

                JSONParser parser = new JSONParser();
                json = (JSONObject) parser.parse(result);

                inputStream.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return json;
        }
        public conexionBDWebService(Context cont, String oper, String usr, int pes, int alt, String nacimiento, String sex){
            context=cont;
            operacion=oper;
            usuario=usr;
            altura=alt;
            fechaNac=nacimiento;
            sexo=sex;
        }



}
