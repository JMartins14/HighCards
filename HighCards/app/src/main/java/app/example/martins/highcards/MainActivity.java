package app.example.martins.highcards;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private EditText text_name;
    private String username;
    private String jogo;
    private ArrayList<String> stringImages;
    private int posActual;
    private ImageButton imgButton;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("username");
        posActual=1;
        imgButton = (ImageButton) findViewById(R.id.image_button);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,(screenHeight/5),0,(screenHeight/7));
        params.width=screenWidth/5;
        params.height=screenHeight/2;
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgButton.setLayoutParams(params);
        jogo = "MauMau";

    }

    public void button_play_clicked(View view) {
        Intent play = new Intent(this,menu_secundario.class);
        play.putExtra("username",user);
        play.putExtra("Jogo",jogo);
        startActivity(play);
        finish();
    }

    public void buttonLeftClicked(View view) {
        Drawable drawable;
        if(jogo=="MauMau"){
            jogo="Desconfia";
        }
        else if(jogo=="Desconfia"){
            jogo = "MauMau";
        }
        switch(posActual){
            case 2:
                posActual--;
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.maumau);
                imgButton = (ImageButton) findViewById(R.id.image_button);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imgButton.setBackground(drawable);
                }
                DisplayMetrics displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int screenWidth = displaymetrics.widthPixels;
                int screenHeight = displaymetrics.heightPixels;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.topMargin=screenWidth/7;
                params.width=screenWidth/5;
                params.height=screenHeight/2;
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);;
                imgButton.setLayoutParams(params);
                break;
            case 1:
                posActual++;
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.desconfia);
                imgButton = (ImageButton) findViewById(R.id.image_button);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imgButton.setBackground(drawable);
                }
                displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                screenWidth = displaymetrics.widthPixels;
                screenHeight = displaymetrics.heightPixels;
                params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.topMargin=screenWidth/7;
                params.width=screenWidth/5;
                params.height=screenHeight/2;
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);;
                imgButton.setLayoutParams(params);
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void buttonRightClicked(View view) {
        Drawable drawable;
        if(jogo=="MauMau"){
            jogo="Desconfia";
        }
        else if(jogo=="Desconfia"){
            jogo = "MauMau";
        }
        switch(posActual){
            case 0:
                posActual++;
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.maumau);
                imgButton = (ImageButton) findViewById(R.id.image_button);
                imgButton.setBackground(drawable);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int screenWidth = displaymetrics.widthPixels;
                int screenHeight = displaymetrics.heightPixels;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.topMargin=screenWidth/7;
                params.width=screenWidth/6;
                params.height=screenHeight/2;
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                imgButton.setLayoutParams(params);
                break;
            case 1:
                posActual++;
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.desconfia);
                imgButton = (ImageButton) findViewById(R.id.image_button);
                imgButton.setBackground(drawable);
                displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                screenWidth = displaymetrics.widthPixels;
                screenHeight = displaymetrics.heightPixels;
                params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.topMargin=screenWidth/7;
                params.width=screenWidth/5;
                params.height=screenHeight/2;
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                imgButton.setLayoutParams(params);
                break;
            case 2:
                posActual--;
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.maumau);
                imgButton = (ImageButton) findViewById(R.id.image_button);
                imgButton.setBackground(drawable);
                displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                screenWidth = displaymetrics.widthPixels;
                screenHeight = displaymetrics.heightPixels;
                params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.topMargin=screenWidth/7;
                params.width=screenWidth/5;
                params.height=screenHeight/2;
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);;
                imgButton.setLayoutParams(params);
                break;
        }
    }
    public void pauseClicked(View view) {

        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage("Pretende sair?");
        exitDialog.setCancelable(true);
        exitDialog.setPositiveButton("Sim",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                finish();
            }
        });
        exitDialog.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                dialog.dismiss();
            }
        });
        exitDialog.show();

    }
}