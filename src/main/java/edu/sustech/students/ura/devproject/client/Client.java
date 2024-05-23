package edu.sustech.students.ura.devproject.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Start listening for messages from the server
            new Thread(this::listenForServerMessages).start();
            System.out.println("Connected to server!");

            // Start sending heartbeat messages
            startHeartbeat();

            // Listen for console input
            handleConsoleInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForServerMessages() {
        try {
            while (true) {
                Object message = in.readObject();
                System.out.println("Received from server: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startHeartbeat() {
        new Thread(() -> {
            while (true) {
                try {
                    // Send heartbeat message
                    out.writeObject("Heartbeat");
                    out.flush();
                    System.out.println("Sent heartbeat");
                    // Wait for 5 seconds before sending the next heartbeat
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    private void handleConsoleInput() {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    if ("quit".equalsIgnoreCase(inputLine.trim())) {
                        close();
                        break;
                    } else {
                        sendMessage(inputLine);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void close() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Object msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8192);
        System.out.println("Client started");
        // Example: Send a message to the server
//        client.sendMessage("Hello, Server!");
    }
}
