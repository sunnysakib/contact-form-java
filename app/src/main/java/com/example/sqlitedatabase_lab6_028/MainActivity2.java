package com.example.sqlitedatabase_lab6_028;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    ListView lvContact;
    Button btnBack;
    ArrayList<Event> events;
    CustomEventAdapter adapter;
    TextView tvName,tvEmail,tvPhone;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lvContact = findViewById(R.id.lvContact);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        events = new ArrayList<>();
        adapter = new CustomEventAdapter(this, events);
        lvContact.setAdapter(adapter);
        // handle the click on an event-list item
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // String item = (String) parent.getItemAtPosition(position);
                System.out.println(position);
                Intent i = new Intent(MainActivity2.this, MainActivity.class);
                i.putExtra("EVENT_KEY", events.get(position).key);
                startActivity(i);
            }
        });
        loadData();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] keys = {"action", "id", "semester"};
        String[] values = {"restore", "2019360050", "2023-1"};
        httpRequest(keys, values);
    }

    private void loadData() {
        events.clear();

        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        if (rows.getCount() == 0) {
            return;
        }
        //events = new Event[rows.getCount()];
        while (rows.moveToNext()) {
            String key = rows.getString(1);
            String eventData = rows.getString(1);

            String[] fieldValues = eventData.split(",");

            String name = fieldValues[0];
            String email = fieldValues[1];
            String phoneHome = fieldValues[2];
            String phoneOffice = fieldValues[3];

            Event e = new Event(key, name, email, phoneHome, phoneOffice);
            events.add(e);
        }
        db.close();

        adapter.notifyDataSetChanged();
    }

        private void httpRequest(final String keys[],final String values[]){
            new AsyncTask<Void,Void,String>(){

                @Override
                protected String doInBackground(Void... voids) {
                    List<NameValuePair> params=new ArrayList<NameValuePair>();
                    for (int i=0; i<keys.length; i++){
                        params.add(new BasicNameValuePair(keys[i],values[i]));
                    }
                    String url= "https://muthosoft.com/univ/cse489/index.php";
                    String data="";
                    try {
                        data=JSONParser.getInstance().makeHttpRequest(url,"POST",params);
                        user = data;
                        return data;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                protected void onPostExecute(String data){
                    if(data!=null){
                        System.out.println(data);
                        Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
}