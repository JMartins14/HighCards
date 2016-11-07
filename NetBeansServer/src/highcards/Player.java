/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highcards;

import java.net.Socket;

/**
 *
 * @author Martins
 */
public class Player {
    private String username;
    private Socket socket;
    private int thread_number;

    public Player(Socket socket, int thread_number) {
        this.socket = socket;
        this.thread_number = thread_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getThread_number() {
        return thread_number;
    }

    public void setThread_number(int thread_number) {
        this.thread_number = thread_number;
    }
    
    
}
