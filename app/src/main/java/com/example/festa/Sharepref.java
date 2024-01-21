package com.example.festa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Sharepref extends AppCompatActivity {

    EditText name;
    EditText no;
    TextView tview;
    TextView btn;

    ArrayList<Shareprefmodel> arrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepref);
        name=findViewById(R.id.name);
        no=findViewById(R.id.number);
        btn=findViewById(R.id.saveBtn);
        tview=findViewById(R.id.tvshow);

        loaddata();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData(name.getText().toString(),no.getText().toString());
            }
        });
    }

    private void loaddata() {

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("DATA",MODE_PRIVATE);

        Gson gson=new Gson();
        String json=sharedPreferences.getString("Student_data",null);
        Type  type=new TypeToken<ArrayList<Shareprefmodel>>(){

        }.getType();

        arrayList=gson.fromJson(json,type);

        if (arrayList==null){
            arrayList=new ArrayList<>();
            tview.setText(""+0);
        }else {
            for (int i=0;i<arrayList.size();i++){
                tview.setText(tview.getText().toString()+"\n"+arrayList.get(i).name);
            }
        }

    }

    private void SaveData(String name, String number) {

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("DATA",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        Gson gson=new Gson();

        arrayList.add(new Shareprefmodel(name,Integer.parseInt(number)));
        String json=gson.toJson(arrayList);
        editor.putString("Guest_data",json);
        editor.apply();
        tview.setText("List Data\n");
        loaddata();


    }
}