package edu.sustech.students.ura.devproject.server;

import edu.sustech.students.ura.devproject.model.GameManager;
import edu.sustech.students.ura.devproject.model.SaveData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// TODO: 增加修改密码功能
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
            } else if (message.startsWith("SET_HIGHSCORE:")) {
                System.out.println(message);
                String[] parts = message.split(":");
                String username = parts[1];
                int mode = Integer.parseInt(parts[2]);
                int score = Integer.parseInt(parts[3]);
                boolean success = userManager.setHighScore(username, mode, score);
                out.writeObject(success ? "SET_HIGHSCORE_SUCCESS" : "SET_HIGHSCORE_FAIL:用户不存在");
            } else if (message.startsWith("GET_HIGHSCORE:")) {
                String[] parts = message.split(":");
                String username = parts[1];
                int mode = Integer.parseInt(parts[2]);
                int highScore = userManager.getHighScore(username, mode);
                out.writeObject("GET_HIGHSCORE:"+ mode+ ":" + highScore);
            } else if (message.startsWith("CHANGE_PASSWORD:")) {
                String[] parts = message.split(":");
                String username = parts[1];
                String oldPassword = parts[2];
                String newPassword = parts[3];
                boolean success = userManager.changePassword(username, oldPassword, newPassword);
                out.writeObject(success ? "CHANGE_PASSWORD_SUCCESS" : "CHANGE_PASSWORD_FAIL:用户名或密码错误");
            } else if (message.startsWith("SAVE:")) {
                String[] parts = message.split(":");
                String username = parts[1];
                int mode = Integer.parseInt(parts[2]);
                byte[] bytes = Base64.getDecoder().decode(parts[3]);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                SaveData saveData = (SaveData) objectInputStream.readObject();
                userManager.saveUserSaveData(username, mode, saveData);
                out.writeObject("SAVE_SUCCESS");
            } else if (message.startsWith("LOAD:")) {
                String[] parts = message.split(":");
                String username = parts[1];
                int mode = Integer.parseInt(parts[2]);
                SaveData saveData = userManager.getUserSaveData(username, mode);
                if (saveData != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(saveData);
                    objectOutputStream.close();
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    String outMessage = Base64.getEncoder().encodeToString(bytes);
                    out.writeObject("LOAD_SUCCESS:" + outMessage);
                } else {
                    out.writeObject("LOAD_FAIL:没有找到存档");
                }
            }
            else {
                out.writeObject("Unknown command: " + message);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
