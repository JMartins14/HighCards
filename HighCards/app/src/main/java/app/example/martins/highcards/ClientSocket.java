package app.example.martins.highcards;

/**
 * Created by Martins on 09/11/2016.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ClientSocket extends Activity {
    Handler UIHandler;
    Thread thread = null;

    public Thread1 thread1;
    private EditText editText;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        editText = (EditText)findViewById(R.id.editText);
        UIHandler = new Handler();
        checkConnectionDialog();
    }

    private void startThread(){
        thread1 = new Thread1(this);
        thread = new Thread(thread1);
        thread.start();
    }
    private void checkConnectionDialog(){
        //Verifica se wifi esta conectado
        if(!isNetworkAvailable()) {
            final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
            exitDialog.setMessage("Por favor conecte o Wifi");
            exitDialog.setCancelable(false);
            exitDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int wich) {
                }
            });
            final AlertDialog alerta = exitDialog.create();
            alerta.show();
            alerta.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable()) {
                        startThread();
                        alerta.dismiss();
                    }
                }
            });
        }
        else{
            Log.d("strigr","deu");
            startThread();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if( activeNetworkInfo != null)
            return true;
        return false;
    }

    public void send_button_clicked(View view) {
        String text = editText.getText().toString();
        //thread1.main_thread.send_to_server(text);
    }



    class Thread1 implements Runnable {
        //public Thread2 main_thread;
        public ClientSocket context;

        public Thread1(ClientSocket context) {
            this.context = context;
        }

        @Override
        public void run() {

            Intent jogar = new Intent(context, OnlineGame.class);
            jogar.putExtra("username", "user1");
            context.startActivity(jogar);
            finish();
        }
    }
    /*main_thread = new Thread2(socket,context);
    main_thread.run();
}
}
/*
class Thread2 implements Runnable{

private Socket clientSocket;
private ClientSocket context;
public Thread2(Socket clientSocket,ClientSocket context){
this.clientSocket = clientSocket;
this.context = context;


}
public void send_to_server(String text){
if(!clientSocket.isClosed())
    outToServer.println(text);
}
@Override
public void run() {
            Intent jogar = new Intent(context,OnlineGame.class);
            jogar.putExtra("username","user1");
            context.startActivity(jogar);
            finish();
       } else {
            thread = new Thread(new Thread1(context));
            thread.start();
            return;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}*/
    class UpdateUIThread implements Runnable{
        private String msg;

        public UpdateUIThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            editText.setText(editText.getText().toString()+"Server Says:"+msg+"\n");
        }
    }
}
