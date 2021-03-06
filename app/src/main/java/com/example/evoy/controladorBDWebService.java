package com.example.evoy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**Clase que se encarga de gestionar las llamadas a la clase que se conecta con el servidor**/
public class controladorBDWebService {

    private static controladorBDWebService instancia;

    private controladorBDWebService(){
    }

    public static controladorBDWebService getInstance(){

        if (instancia==null){
            instancia = new controladorBDWebService();
        }

        return instancia;
    }

    //devuelve true si los datos del usuario son correctos
    public boolean login(Context cont, String oper, String usr, String contra) throws ExecutionException, InterruptedException {

        JSONObject json = new conexionBDWebService(cont, oper, usr, contra).execute().get();

        return !json.isEmpty();
    }

    //devuelve true si existen datos adicionales del usuario
    public boolean existenDatos(Context cont, String oper, String usr) throws ExecutionException, InterruptedException {

        boolean existen=true;
        JSONObject json = new conexionBDWebService(cont, oper, usr).execute().get();
        String user = null;

        if(!json.isEmpty()){
            user = (String) json.get("usuario");
        }


        if(user!=null){
            existen=false;
        }
        return existen;
    }

    //devuelve un json con los datos del usuario
    public JSONObject getDatos(Context cont, String oper, String usr) throws ExecutionException, InterruptedException {

        JSONObject json = new conexionBDWebService(cont, oper, usr).execute().get();
        return json;
    }

    //devuelve true si se ha realizado correctamente la petición
    public boolean updateUsuarioDetalles(Context applicationContext, String oper, String usr, int peso, int alt, String fecha, String genero) throws ExecutionException, InterruptedException {

        JSONObject json = new conexionBDWebService(applicationContext, oper, usr,peso, alt, fecha, genero).execute().get();

        String res = (String) json.get("respuesta");

        return res.equals("correcto");
    }

    //inserta un nuevo usuario con los datos proporcionados
    public boolean insertarUsuario(Context applicationContext, String insertarUsuario, String usuario, String nombre, String apellidos, String email, String contra, String fecha) throws ExecutionException, InterruptedException {

        boolean correcto=false;
        JSONObject json = new conexionBDWebService(applicationContext, insertarUsuario,usuario,nombre,apellidos,email,contra,fecha).execute().get();

        String res = (String) json.get("respuesta");

        return res.equals("correcto");
    }

    //inserta un nuevo token del usuario
    public void insertarToken(Context context,String oper,String user,String token) {

        new conexionBDWebService(context,oper,user,token).execute();
    }

    //genera una notificación FCM desde php del servidor
    public void mensajesFCMweb(Context applicationContext, String mensajesFCMweb, String nombreUsuario) {

        new conexionBDWebService(applicationContext,mensajesFCMweb,nombreUsuario).execute();
    }

    //guarda la imagen encoded en string64
    public void saveImg(Context applicationContext, String saveImg, String nombreUsuario, String fotoen64, String titulo) throws ExecutionException, InterruptedException {

        Toast.makeText(applicationContext, "*******saveIMGCONNNNN: \n" + fotoen64 + "\n**********", Toast.LENGTH_SHORT).show();
        JSONObject json = new conexionBDWebService(applicationContext, saveImg,nombreUsuario,fotoen64,titulo).execute().get();
        String res = (String) json.get("respuesta");
        Toast.makeText(applicationContext, "*******RESPUESTAS: \n" + res + "\n**********", Toast.LENGTH_SHORT).show();
    }

    //insertar nuevo evento
    public boolean insertarEvento(Context context, String insertarEvento, String user, String nombre, String descripcion, String location_name, String lat, String lon, String horaTimestamp, String imagen64) throws ExecutionException, InterruptedException {

        boolean correcto=false;
        JSONObject json = new conexionBDWebService(context, insertarEvento, user, nombre, descripcion, location_name, lat, lon, horaTimestamp, imagen64).execute().get();

        String res = (String) json.get("respuesta");

        return res.equals("correcto");
    }

    public JSONArray getAllFeed(Context context, String username) throws ExecutionException, InterruptedException {

        JSONArray json = new ConexionBDEventos(context, username).execute().get();
        return json;
    }

    //devuelve un array con el id de todos los eventos que sigue el usuario
    public int[] getFollowed(Context applicationContext, String getFollowed, String nombreUsuario) throws ExecutionException, InterruptedException {

        JSONObject json = new conexionBDWebService(applicationContext,getFollowed,nombreUsuario).execute().get();
        JSONArray arr = (JSONArray) json.get("Followed");
        int total = arr.size();
        int[] pesos = new int[total];

        Iterator i = arr.iterator();
        int cont =0;

        while (i.hasNext()) {

            JSONObject idFollowed = (JSONObject) i.next();
            int idEvent = Integer.valueOf((String) idFollowed.get("idEvent"));
            pesos[cont]=idEvent;
            cont++;

        }
        return pesos;
    }

    //insertar nuevo evento a la lista de seguidos del usuario
    public void startFollow(Context context, String user, int idEvent) {
        new conexionBDWebService(context,"startFollow",user,idEvent).execute();
    }

    //elimninar el evento de la lista de seguidos del usuario
    public void stopFollow(Context context, String user, int idEvent) {
        new conexionBDWebService(context,"stopFollow",user,idEvent).execute();
    }

    //devuelve la imagen de un evento dado
    public Bitmap getImage(Context context, String idEvent) throws ExecutionException, InterruptedException {

        JSONObject res = (JSONObject) new conexionBDWebService(context,"getImage",idEvent).execute().get();
        String img64 = (String) res.get("0");
        InputStream stream = new ByteArrayInputStream(Base64.decode(img64.getBytes(), Base64.DEFAULT));
        return BitmapFactory.decodeStream(stream);

    }

    //devuelve los eventos subidos de un usuario
    public JSONArray getProfileFeed(Context context, String user) throws ExecutionException, InterruptedException {

        JSONArray json = new ConexionBDEventos(context, "getProfileFeed",user).execute().get();
        return json;
    }

    //devuelve los eventos que ha seguido un usuario
    public JSONArray getFollowsFeed(Context context, String user) throws ExecutionException, InterruptedException {

        JSONArray json = new ConexionBDEventos(context, "getFollowsFeed",user).execute().get();
        return json;
    }
}
