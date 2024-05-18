package edu.sustech.students.ura.devproject.io;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * ClientHandler
 * 客户端处理器
 * @version 1.0
 * 客户端处理器用于处理客户端发送的对象
 *
 */

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