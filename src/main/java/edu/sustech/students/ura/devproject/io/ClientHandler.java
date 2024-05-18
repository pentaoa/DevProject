package edu.sustech.students.ura.devproject.io;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            Player player = (Player) objectInputStream.readObject();
            // Process the player object, e.g., store in database or perform some action
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}