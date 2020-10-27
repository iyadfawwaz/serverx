package sy.e.serverconn;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import sy.e.serverconn.Activities.SelectionActivity;
import sy.e.serverconn.FirebaseUtils.Token;
import sy.e.serverconn.Utils.ServerInfo;
import sy.iyad.mikrotik.MikrotikServer;
import sy.iyad.mikrotik.Models.ConnectionEventListener;
import sy.iyad.mikrotik.Utils.Api;
import  static sy.e.serverconn.ServerInformations.IP;
import  static sy.e.serverconn.ServerInformations.ADMIN;
import  static sy.e.serverconn.ServerInformations.PASSWORD;


public class MainActivity extends AppCompatActivity {


    EditText admin;
    Button button;
    EditText ip;
    EditText pass;
    TextView textView;


    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

            button =  findViewById(R.id.connect);
            ip =  findViewById(R.id.ip);
            admin =  findViewById(R.id.admin);
            pass =  findViewById(R.id.password);
            textView =  findViewById(R.id.textView);

            laodInfo();

            button.setOnClickListener(new OnClickListener() {

                public void onClick(View view) {

                    String ips = ip.getText().toString();
                    String admins = admin.getText().toString();
                    String passs = pass.getText().toString();
                    IP = ips;
                    ADMIN = admins;
                    PASSWORD = passs;

                    doLogin(ips, admins, passs);

                }
            });
            button.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {

                    registerId();
                    return true;

                }
            });
    }

    private void init(){

    }

    private void laodInfo() {
        ip.setText(ServerInformations.IP);
        admin.setText(ServerInformations.ADMIN);
        pass.setText(ServerInformations.PASSWORD);
    }

    private void doLogin(String aips, String aadmins, String apasss) {
        MikrotikServer.connect(aips, aadmins, apasss)
        .addConnectionEventListener(new ConnectionEventListener() {
            public void onConnectionSuccess(Api api) {
                startActivity(new Intent(MainActivity.this, SelectionActivity.class));
            }

            public void onConnectionFailed(Exception exp) {
                textView.setText(exp.getMessage());
            }
        });
    }

    private void registerId() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this, new OnCompleteListener<InstanceIdResult>() {
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    ServerInfo.tokenReference.push().setValue(new Token( task.getResult().getToken()));
                    ServerInfo.isRegistered = true;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!ServerInfo.isRegistered){
            registerId();
        }
    }
}