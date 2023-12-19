package com.example.siyamassignent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;
    private static ArrayList<String> serverMessages = new ArrayList<>();
    private static final String MESSAGES_FILE = "messages.txt";


    public ClientHandler(Socket socket, String name) {
        try {
            this.socket = socket;
            this.name = name;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlers.add(this);
            sendMessageToClient("You are " + name);
            sendPreviousMessages();
        } catch (Exception e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    private void sendMessageToClient(String message) {
        try {
            buffWriter.write(message);
            buffWriter.newLine();
            buffWriter.flush();
        } catch (Exception e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }
    private static void writeMessagesToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MESSAGES_FILE, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = buffReader.readLine();
                broadcastMessage(name + ": " + messageFromClient);
            } catch (Exception e) {
                closeAll(socket, buffReader, buffWriter);
                break;
            }
        }
    }

    public void sendPreviousMessages() {
        for (String message : serverMessages) {
            try {
                buffWriter.write(message);
                buffWriter.newLine();
                buffWriter.flush();
            } catch (Exception e) {
                closeAll(socket, buffReader, buffWriter);
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        writeMessagesToFile(messageToSend);

        System.out.println("From "+messageToSend);

        serverMessages.add(messageToSend);
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.name.equals(name)) {
                    clientHandler.buffWriter.write(messageToSend);
                    clientHandler.buffWriter.newLine();
                    clientHandler.buffWriter.flush();
                }
            } catch (Exception e) {
                closeAll(socket, buffReader, buffWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
        removeClientHandler();
        try {
            if (buffReader != null) {
                buffReader.close();
            }
            if (buffWriter != null) {
                buffWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
