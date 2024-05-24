package edu.sustech.students.ura.devproject.client;

import edu.sustech.students.ura.devproject.controller.LoginViewController;
import javafx.scene.control.Alert;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientListener listener;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Start listening for messages from the server
            new Thread(this::listenForServerMessages).start();
            System.out.println("连接到服务器！");

            // Start sending heartbeat messages
            startHeartbeat();

            // Listen for console input
            handleConsoleInput();
        } catch (Exception e) {
            System.out.println("无法连接服务器，尝试使用离线模式！");
//            e.printStackTrace();
        }
    }

    private void listenForServerMessages() {
        try {
            while (true) {
                Object message = in.readObject();
                System.out.println("接收到信息: " + message);
                if (message instanceof String) {
                    listener.onMessageReceived((String) message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(ClientListener listener) {
        this.listener = listener;
    }

    private void startHeartbeat() {
        new Thread(() -> {
            while (true) {
                try {
                    // Send heartbeat message
                    out.writeObject("Heartbeat");
                    out.flush();
                    System.out.println("心跳！");
                    // Wait for 5 seconds before sending the next heartbeat
                    Thread.sleep(10000);
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
                        processCommand(inputLine);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void processCommand(String command) {
        try {
            if (command.startsWith("register")) {
                String[] parts = command.split(" ");
                if (parts.length == 3) {
                    String username = parts[1];
                    String password = parts[2];
                    sendMessage("REGISTER:" + username + ":" + password);
                } else {
                    System.out.println("Usage: register <username> <password>");
                }
            } else if (command.startsWith("login")) {
                String[] parts = command.split(" ");
                if (parts.length == 3) {
                    String username = parts[1];
                    String password = parts[2];
                    sendMessage("LOGIN:" + username + ":" + password);
                } else {
                    System.out.println("Usage: login <username> <password>");
                }
            } else {
                sendMessage("MESSAGE:" + command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
//            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Object msg) {
        try {
            if (out != null) {
                out.writeObject(msg);
                out.flush();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText("登录失败");
                alert.setContentText("无法连接到服务器，请使用离线模式");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Client client = new Client("localhost", 8192);
        // Example: Send a message to the server
//        client.sendMessage("Hello, Server!");
    }
}
