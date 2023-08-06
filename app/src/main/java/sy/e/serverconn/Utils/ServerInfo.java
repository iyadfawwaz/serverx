package sy.e.serverconn.Utils;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ServerInfo {

    public static final DatabaseReference tokenReference = FirebaseDatabase.getInstance().getReference().child("serverLogger/tokens");
    public static boolean isRegistered = false;

}
