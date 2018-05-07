package app.example.martins.highcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import static app.example.martins.highcards.R.id.editText;

/**
 * Created by Costa on 22/12/2016.
 */

public class nomeTempScreen extends Activity {
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.nometempscreen);
    }

    public void botaoNext(View view) {
        EditText userteste =(EditText) findViewById(R.id.nome);
        user = userteste.getText().toString();
        if(user.length()<2)
            alertMessage("Nome demasiado curto");
        else {
            Intent jogar2 = new Intent(this, MainActivity.class);
            jogar2.putExtra("username", user);
            startActivity(jogar2);
            finish();
        }
    }

    private void alertMessage(String message) {
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage(message);
        exitDialog.setCancelable(true);
        exitDialog.show();
    }
}

