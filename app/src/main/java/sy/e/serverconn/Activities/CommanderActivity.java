package sy.e.serverconn.Activities;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sy.e.serverconn.R;
import sy.e.serverconn.Utils.ServerAdapter;
import sy.iyad.mikrotik.MikrotikServer;
import sy.iyad.mikrotik.Models.ExecutionEventListener;

import static sy.e.serverconn.ServerInformations.SERVERKEYWORDS;


public class CommanderActivity extends AppCompatActivity {


    ServerAdapter adapter;
    ArrayList<String> arrayList;
    ArrayAdapter<String> stringArrayAdapter;


    Button button;
    MultiAutoCompleteTextView comm;
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

                    sendComm(co, ke);
            }
        });
    }
    private void init(){

        arrayList = new ArrayList<>();
        adapter = new ServerAdapter(this, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,SERVERKEYWORDS);
        comm.setAdapter(stringArrayAdapter);
        comm.setTokenizer(new SlashTokenizer());
        comm.setThreshold(1);
        comm.setText("/");
        stringArrayAdapter.notifyDataSetChanged();

    }


    private void sendComm(String command, final String key) {

        adapter.clear();
        arrayList.clear();

        init();

        MikrotikServer.execute(command).addExecutionEventListener(new ExecutionEventListener() {
            @SuppressLint("SetTextI18n")
            public void onExecutionSuccess(@NonNull List<Map<String, String>> mapList) {

                if (key.equals("")) {

                    arrayList.add(mapList.toString());

                }else {

                for (Map<String, String> map : mapList) {

                    if (!map.get(key).isEmpty()) {
                        arrayList.add(map.get(key));
                    }else {
                        textView.setText("empty");
                    }
                    }
                }
                adapter.notifyDataSetChanged();
                textView.setText(""+adapter.getCount());
            }

            public void onExecutionFailed(@NonNull Exception exception) {
               textView.setText(exception.getMessage());
            }
        });
    }
    private static class SlashTokenizer implements MultiAutoCompleteTextView.Tokenizer{

        @Override
        public int findTokenStart(CharSequence text, int cursor) {

            int i = cursor;
            while (i>0 && text.charAt(i-1) != '/'){
                i--;
            }

            while (i<cursor && text.charAt(i) =='/'){
                i++;
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {

            int i = cursor;
            int len = text.length();
            while (i<len){
                if (text.charAt(i)=='/'){
                    return i;
                }else {
                    i++;
                }
            }

            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {

            int i = text.length();
            while (i>0 && text.charAt(i-1) == '/'){
                i--;
            }
            if (i>0 && text.charAt(i-1) == '/'){
                return text;
            }else {
                if (text instanceof Spanned){
                    SpannableString string = new SpannableString(text+"/");
                    TextUtils.copySpansFrom((Spanned) text,0,text.length(),Object.class,string,0);
                    return string;

                }else {
                    if (text =="print"){
                        return text;
                    }else {

                    }
                    return text+"/";
                }
            }
        }
    }
}
