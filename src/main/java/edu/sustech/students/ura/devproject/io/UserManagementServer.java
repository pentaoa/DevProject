package edu.sustech.students.ura.devproject.io;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UserManagementServer {
    public static void startUserManagementServer() {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("服务器开始运行。");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startUserManagementServer();
    }
}