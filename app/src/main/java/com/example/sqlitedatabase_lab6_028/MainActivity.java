package com.example.sqlitedatabase_lab6_028;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.net.Uri;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhoneHome, etPhoneOffice;
    TextView tvCencel;
    Button btnSave;
    ImageView image;
    boolean isAllFieldsChecked = false;
    KeyValueDB contactDB = new KeyValueDB(this);
    String key ="";
    int SELECT_IMAGE_CODE = 1;
    String stringImage;
    String[] contactValues = {"name", "email", "phoneHome", "phoneOffice"};
    String eventValue = TextUtils.join(",", contactValues);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhoneHome = findViewById(R.id.phoneHome);
        etPhoneOffice = findViewById(R.id.phoneOffice);
        btnSave = findViewById(R.id.btnSave);
        image = findViewById(R.id.image);
        tvCencel = findViewById(R.id.cencel);

        tvCencel.setOnClickListener(view -> finish());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();

                if (isAllFieldsChecked){
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String phoneHome = etPhoneHome.getText().toString();
                    String PhoneOffice = etPhoneOffice.getText().toString();
                    String values = name + "," + email + "," + phoneHome + "," + PhoneOffice + "," + stringImage;
                    key = name + System.currentTimeMillis();
                    System.out.println(key);
                    System.out.println(values);
                    contactDB.insertKeyValue(key, values);
                    contactDB.close();
                    String[] keys = {"action", "id", "semester", "key", "event"};
                    String[] value = {"backup", "2019360028", "2023-1",key, values};
                    httpRequest(keys, value);
                    Toast.makeText(getApplicationContext(), "Contact saved successfully",  Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Filled All Field",  Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadData("");
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
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    System.out.println(data);
                    //updateEventListByServerData(data);
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void loadData(String key) {
        String v = contactDB.getValueByKey(key);
        if (v != null) {
            String values[] = v.split(",");
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            Uri uri = data.getData();
            image.setImageURI(uri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                stringImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean CheckAllFields(){
        if(etName.length() == 0){
            etName.setError("This field is required");
            return false;
        }
        if(etEmail.length() == 0){
            etEmail.setError("This field is required");
            return false;
        }
        if(etPhoneHome.length() == 0){
            etPhoneHome.setError("This field is required");
            return false;
        }
        if(etPhoneOffice.length() == 0){
            etPhoneOffice.setError("This field is required");
            return false;
        }
        return true;
    }
}