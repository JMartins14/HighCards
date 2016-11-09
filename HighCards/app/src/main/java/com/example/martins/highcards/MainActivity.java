package com.example.martins.highcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends Activity {

    private EditText text_name;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner);
        int[] array = {0,1,2,3,4,5,6,7};
        //ArrayAdapter array2 = ArrayAdapter.createFromResource(this,array,android.R.layout.simple_spinner_item);
        //spnner.setAdapter(array);

        text_name = (EditText)findViewById(R.id.editText);
    }

    public void playClicked(View view) {
        String name = String.valueOf(text_name.getText());
        if(name.equals("")){
            Toast.makeText(this,"Please enter your username!",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent activityGame = new Intent(this,Game.class);
            activityGame.putExtra("username",name);
            startActivity(activityGame);
            finish();
        }

    }

}