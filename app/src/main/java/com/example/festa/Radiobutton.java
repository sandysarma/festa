package com.example.festa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Radiobutton extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radiobutton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiobutton);

        radioGroup=findViewById(R.id.radiogroup);


    }
    public void checkButton(View v){
        int radioId=radioGroup.getCheckedRadioButtonId();
        radiobutton=findViewById(radioId);
        Toast.makeText(this, "selected radio button"+radiobutton.getText(), Toast.LENGTH_SHORT).show();
    }
}