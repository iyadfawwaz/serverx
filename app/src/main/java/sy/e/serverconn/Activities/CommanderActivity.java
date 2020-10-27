package sy.e.serverconn.Activities;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sy.e.serverconn.R;
import sy.e.serverconn.Utils.ServerAdapter;
import sy.iyad.mikrotik.MikrotikServer;
import sy.iyad.mikrotik.Models.ExecutionEventListener;


public class CommanderActivity extends AppCompatActivity {


    private ServerAdapter adapter;
    ArrayList<String> arrayList;

    Button button;
    EditText comm;
    EditText keyg;
    ListView listView;
    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander);

        listView =  findViewById(R.id.listview);
        textView =  findViewById(R.id.warn);
        comm =  findViewById(R.id.command);
        keyg =  findViewById(R.id.key);
        button =  findViewById(R.id.sendcomm);

       init();

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String ke = keyg.getText().toString();
                String co = comm.getText().toString();
                if (ke.trim().length() <= 0) {
                    sendComm(co, "");
                } else {
                    sendComm(co, ke);
                }
            }
        });
    }
    private void init(){
        arrayList = new ArrayList<>();
        adapter = new ServerAdapter(this, arrayList);
        listView.setAdapter(adapter);
    }

    private void sendComm(String command, final String key) {
        this.adapter.clear();
        MikrotikServer.execute(command).addExecutionEventListener(new ExecutionEventListener() {
            public void onExecutionSuccess(List<Map<String, String>> mapList) {
                for (Map<String, String> map : mapList) {
                    if (key.equals("")) {
                        arrayList.add(map.toString());
                    } else {
                       arrayList.add(map.get(key));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            public void onExecutionFailed(Exception exp) {
               textView.setText(exp.getMessage());
            }
        });
    }
}
