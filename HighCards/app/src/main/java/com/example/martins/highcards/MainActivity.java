package com.example.martins.highcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


public class MainActivity extends Activity {

    private EditText text_name;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        text_name = (EditText) findViewById(R.id.username_text);
        username = text_name.getText().toString();

    }

    public void button_play_clicked(View view) {
        Intent play = new Intent(this,menu_secundario.class);
        play.putExtra("username",username);
        startActivity(play);
        finish();
    }
}