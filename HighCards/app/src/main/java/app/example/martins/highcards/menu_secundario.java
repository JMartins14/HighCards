package app.example.martins.highcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Goncalopinto on 07/11/16.
 */

public class menu_secundario extends Activity {
    private String user;
    private String jogo;
    private ArrayList<Button> buttons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("username");
        jogo = bundle.getString("Jogo");
        setContentView(R.layout.menu_secundario);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        Button buttoninstrucoes = (Button) findViewById(R.id.buttoninstrucoes);
        Button buttonjogar = (Button) findViewById(R.id.buttonjogar);
        Button buttonjogaronline = (Button) findViewById(R.id.buttonjogaronline);
        Button buttonvoltar = (Button) findViewById(R.id.buttonvoltar);
        buttons.add(buttonjogar);
        buttons.add(buttonjogaronline);
        buttons.add(buttoninstrucoes);
        buttons.add(buttonvoltar);
        for(int i=0;i<buttons.size();i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            switch(i){
                case 0:
                    params.topMargin=screenWidth/7;
                    params.gravity= Gravity.CENTER;
                    params.width=screenWidth/3;
                    params.height=screenHeight/6;
                    buttons.get(i).setLayoutParams(params);
                    break;
                default:
                    params.gravity= Gravity.CENTER;
                    params.width=screenWidth/3;
                    params.height=screenHeight/6;
                    buttons.get(i).setLayoutParams(params);
                    break;
            }
        }
    }

    public void button_offline_clicked(View view) {
        switch(jogo){
            case "MauMau":
                Intent jogar = new Intent(this,MauMau.class);
                jogar.putExtra("username",user);
                jogar.putExtra("Jogo",jogo);
                startActivity(jogar);
                finish();
                break;
            case "Desconfia":
                Intent jogar2 = new Intent(this,Desconfia.class);
                jogar2.putExtra("username",user);
                jogar2.putExtra("Jogo",jogo);
                startActivity(jogar2);
                finish();
                break;
        }
    }

    public void button_instrucoes_clicked(View view) {
        Intent instrucoes = new Intent(this,instrucoes.class);
        instrucoes.putExtra("username",user);
        instrucoes.putExtra("Jogo",jogo);
        startActivity(instrucoes);
        finish();
    }

    public void button_voltar_clicked(View view) {
        Intent menu = new Intent(this,MainActivity.class);
        menu.putExtra("username",user);
        startActivity(menu);
        finish();
    }

    public void button_online_clicked(View view) {
        Toast.makeText(this,"Brevemente Disponível",Toast.LENGTH_LONG).show();
    }

    private void onlineMessage(String message) {
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage(message);
        exitDialog.setCancelable(false);
        exitDialog.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int wich) {
                Intent menu = new Intent(getBaseContext(), menu_secundario.class);
                menu.putExtra("username",user);
                menu.putExtra("Jogo", jogo);
                startActivity(menu);
                finish();
            }
        });
        exitDialog.show();
    }
}

