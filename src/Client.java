

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    Scanner sc =new Scanner(System.in);

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void sendMessage() {
        try {
            while (socket.isConnected()) {
                String messageToSend = sc.nextLine();
                buffWriter.write( messageToSend);
                buffWriter.newLine();
                buffWriter.flush();
            }
        } catch (Exception e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void readMessage() {
        new Thread(() -> {
            String msgFromGroupChat;
            try {
                while (socket.isConnected()) {
                    msgFromGroupChat = buffReader.readLine();
                    System.out.println(msgFromGroupChat);
                }
            } catch (Exception e) {
                closeAll(socket, buffReader, buffWriter);
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
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

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket);
        client.readMessage();
        client.sendMessage();
    }
}
