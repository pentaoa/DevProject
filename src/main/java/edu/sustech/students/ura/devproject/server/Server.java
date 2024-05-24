package edu.sustech.students.ura.devproject.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int PORT = 8192;
    private static final long HEARTBEAT_TIMEOUT = 20000; // 超时时间：20秒

    private static ConcurrentHashMap<Socket, Long> clientHeartbeats = new ConcurrentHashMap<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static UserManager userManager = new UserManager();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器已启动，端口：" + PORT);

            // Start the heartbeat monitor
            startHeartbeatMonitor();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("有新的客户端连接：" + clientSocket);
                clientHeartbeats.put(clientSocket, System.currentTimeMillis());

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                Object message = in.readObject();
//                System.out.println("收到客户端信息：" + message + " 发送者：" + clientSocket);

                if ("Heartbeat".equals(message)) {
                    clientHeartbeats.put(clientSocket, System.currentTimeMillis());
                } else {
                    processMessage((String) message, out);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection(clientSocket);
        }
    }

    private static void processMessage(String message, ObjectOutputStream out) {
        try {
            if (message.startsWith("REGISTER:")) {
                String[] parts = message.split(":");
                String username = parts[1];
                String password = parts[2];
                boolean success = userManager.registerUser(username, password);
                out.writeObject(success ? "REGISTER_SUCCESS" : "REGISTER_FAIL:用户名已存在");
            } else if (message.startsWith("LOGIN:")) {
                String[] parts = message.split(":");
                String username = parts[1];
                String password = parts[2];
                boolean success = userManager.loginUser(username, password);
                out.writeObject(success ? "LOGIN_SUCCESS" : "LOGIN_FAIL:用户名或密码错误");
            } else if (message.startsWith("MESSAGE:")) {
                String content = message.substring(8);
                // Process message (e.g., broadcast to other clients)
                out.writeObject("Server received message: " + content);
            } else {
                out.writeObject("Unknown command: " + message);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startHeartbeatMonitor() {
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            clientHeartbeats.forEach((socket, lastHeartbeat) -> {
                if (currentTime - lastHeartbeat > HEARTBEAT_TIMEOUT) {
                    System.out.println("客户端 " + socket + " 超时！");
                    closeConnection(socket);
                }
            });
        }, 0, 5, TimeUnit.SECONDS);
    }

    private static void closeConnection(Socket socket) {
        try {
            System.out.println("与客户端丢失连接：" + socket);
            clientHeartbeats.remove(socket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
