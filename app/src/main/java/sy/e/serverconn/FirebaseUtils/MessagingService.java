package sy.e.serverconn.FirebaseUtils;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;
import sy.e.serverconn.Activities.SelectionActivity;
import sy.e.serverconn.R;
import sy.e.serverconn.Utils.ServerInfo;


public class MessagingService extends FirebaseMessagingService {


    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        notificationBuild(remoteMessage);
    }

    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        ServerInfo.tokenReference.push().setValue(new Token(token));
    }


    private void notificationBuild(RemoteMessage remoteMessage) {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("sy","sy", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Builder notificationCompat = new Builder(this, "sy");
        notificationCompat.setVibrate(new long[]{500, 600, 700, 800}).setSmallIcon(R.drawable.avatarx)
                .setContentText( remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")))
                //.setStyle(new BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.syavatar)))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.symain))
                .setContentTitle( remoteMessage.getData().get("sender"))
                .setAllowSystemGeneratedContextualActions(true)
                .setColor(Color.RED)
                .addAction(new Action( R.drawable.remain,  "reply", PendingIntent.getActivity(this, new Random().nextInt(), new Intent(this, SelectionActivity.class), Intent.FILL_IN_ACTION)));
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                .from(this);
        notificationManagerCompat.notify(new Random().nextInt(), notificationCompat.build());
    }
}
