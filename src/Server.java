package com.example.siyamassignent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private int clientCounter = 0;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void serverStart() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Clinet-" +clientCounter+"Joined");

                String clientName = "Client-" + clientCounter++;
                ClientHandler clientHandler = new ClientHandler(socket, clientName);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {

        }
    }
    public void closerServer(){

        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.serverStart();
    }
}