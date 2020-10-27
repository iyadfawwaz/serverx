package sy.e.serverconn.FirebaseUtils;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.BigPictureStyle;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.internal.view.SupportMenu;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;
import sy.e.serverconn.Activities.SelectionActivity;
import sy.e.serverconn.R;
import sy.e.serverconn.Utils.ServerInfo;


public class MessagingService extends FirebaseMessagingService {
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println( remoteMessage.getData().get("sender"));
        notificationBuild(remoteMessage);
    }

    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        ServerInfo.tokenReference.push().setValue(new Token(token));
    }

    @SuppressLint("WrongConstant")
    private void notificationBuild(RemoteMessage remoteMessage) {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("sy","sy", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Builder notificationCompat = new Builder(this, "sy");
        notificationCompat.setVibrate(new long[]{500, 600, 700, 800}).setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText( remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setStyle(new BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle( remoteMessage.getData().get("sender"))
                .setAllowSystemGeneratedContextualActions(true)
                .setColor(Color.RED)
                .addAction(new Action( R.drawable.common_full_open_on_phone,  "reply", PendingIntent.getActivity(this, new Random().nextInt(), new Intent(this, SelectionActivity.class), Intent.FILL_IN_ACTION)));
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                .from(this);
        notificationManagerCompat.notify(new Random().nextInt(), notificationCompat.build());
    }
}
