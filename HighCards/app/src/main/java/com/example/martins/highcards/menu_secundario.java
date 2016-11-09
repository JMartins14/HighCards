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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.menu_secundario);
    }

    public void button_jogar_clicked(View view) {
        Intent jogar = new Intent(this,MainActivity.class);
        startActivity(jogar);
        finish();
    }

    public void button_instrucoes_clicked(View view) {
        Intent instrucoes = new Intent(this,instrucoes.class);
        startActivity(instrucoes);
        finish();
    }

    public void button_voltar_clicked(View view) {

    }
}
