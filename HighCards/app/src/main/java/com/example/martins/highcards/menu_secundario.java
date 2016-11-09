package com.example.martins.highcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Goncalopinto on 07/11/16.
 */

public class menu_secundario extends Activity {
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("username");
        setContentView(R.layout.menu_secundario);
    }

    public void button_offline_clicked(View view) {
        Intent jogar = new Intent(this,Game.class);
        jogar.putExtra("username",user);
        startActivity(jogar);
        finish();
    }

    public void button_instrucoes_clicked(View view) {
        Intent instrucoes = new Intent(this,instrucoes.class);
        startActivity(instrucoes);
        finish();
    }

    public void button_voltar_clicked(View view) {
        Intent menu = new Intent(this,MainActivity.class);
        startActivity(menu);
        finish();
    }

    public void button_online_clicked(View view) {
        Intent online_game = new Intent(this,ClientSocket.class);
        startActivity(online_game);
        finish();
        }
    }

