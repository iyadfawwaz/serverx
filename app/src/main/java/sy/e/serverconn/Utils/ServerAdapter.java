package sy.e.serverconn.Utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import sy.e.serverconn.R;


public class ServerAdapter extends ArrayAdapter<String> {

    ArrayList<String> arrayList;
    Context context;

    public ServerAdapter(@NonNull Context context,@NonNull ArrayList<String> arrayList) {

        super(context, 0, arrayList);
        this.arrayList = arrayList;
        this.context = context;

    }

    public int getCount() {
        return this.arrayList.size();
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        String string = arrayList.get(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activityadapter, parent, false);
            TextView textView = convertView.findViewById(R.id.container);
            textView.setText(string);
        }
        return convertView;
    }

}
