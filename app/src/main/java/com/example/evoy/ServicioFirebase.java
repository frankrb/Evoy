package com.example.evoy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.constraint.Constraints.TAG;

/**Clase que se encarga de gestionar las llamadas al servicio Firebase Cloud Messaging(FCM)**/
public class ServicioFirebase extends FirebaseMessagingService {

    public ServicioFirebase() {
    }

    /**Este métdo gestiona los mensajes recibidos desde FCM
     * **/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Si la aplicación está en background, se
        //muestra una notificación, pero no se
        //ejecuta este método

        if (remoteMessage.getData().size() > 0) {
            //Si el mensaje viene con datos
            Log.d(TAG, "Datos del mensaje FCM: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            //Si el mensaje es una notificación
            Log.d(TAG, "Notificación del mensaje FCM: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification());
        }
    }





    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        //Qué hacer cada vez que se genere un
        //token para el dispositivo
        Log.d(TAG, "Nuevo token: " + token);
    }

    /**Este metodo lanza la notificación a nuestro teléfono
     * **/
    private void sendNotification(RemoteMessage.Notification notification) {

        String channelId = "NOTIFICACION";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_web)
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // desde android Oreo el canal de notificación se requiere
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
