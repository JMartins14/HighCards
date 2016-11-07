package com.example.martins.sockets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AppCompatActivity{
    Handler UIHandler;
    Thread thread = null;
    public static final int server_port = 6000;
    public static final String hostname = "192.168.1.153";
    public Thread1 thread1;
    private EditText editText;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        editText = (EditText)findViewById(R.id.editText);
        UIHandler = new Handler();
        checkConnectionDialog();
            thread1 = new Thread1();
            this.thread = new Thread(thread1);
            this.thread.start();


    }
    private void checkConnectionDialog(){
        //Verifica se wifi esta conectado
        if(!checkWifiOnAndConnected()){
            final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
            exitDialog.setMessage("Por favor conecte o Wifi");
            exitDialog.setCancelable(true);
            exitDialog.setPositiveButton("Done",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int wich){
                }
            });
            final AlertDialog alerta = exitDialog.create();
            alerta.show();
            alerta.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(checkWifiOnAndConnected())
                        alerta.dismiss();
                }
            });
        }
    }
    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    public void send_button_clicked(View view) {
        String text = editText.getText().toString();
       thread1.main_thread.send_to_server(text);
    }



    class Thread1 implements Runnable{
        public Thread2 main_thread;
        @Override
        public void run() {
            try{

                InetAddress serverAddr =InetAddress.getByName(hostname);
               Socket socket = new Socket(serverAddr, server_port);
                 main_thread = new Thread2(socket);
                main_thread.run();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    class Thread2 implements Runnable{

        private Socket clientSocket;
        private BufferedReader input;
        private PrintWriter outToServer;
        public Thread2(Socket clientSocket){
            this.clientSocket = clientSocket;
            try {
                this.outToServer = new PrintWriter(clientSocket.getOutputStream(),true);
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public void send_to_server(String text){
            if(!clientSocket.isClosed())
                outToServer.println(text);
        }
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                   String read = input.readLine();

                    if(read != null){
                        UIHandler.post(new UpdateUIThread(read));

                    }
                    else{
                        thread = new Thread(new Thread1());
                        thread.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
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
