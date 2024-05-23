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
    private static final long HEARTBEAT_TIMEOUT = 10000; // 10 seconds

    private static ConcurrentHashMap<Socket, Long> clientHeartbeats = new ConcurrentHashMap<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            // Start the heartbeat monitor
            startHeartbeatMonitor();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
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
                System.out.println("Received: " + message + " from " + clientSocket);

                if ("Heartbeat".equals(message)) {
                    clientHeartbeats.put(clientSocket, System.currentTimeMillis());
                } else {
                    // Echo the message back to the client
                    out.writeObject("Echo: " + message);
                    out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection(clientSocket);
        }
    }

    private static void startHeartbeatMonitor() {
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            clientHeartbeats.forEach((socket, lastHeartbeat) -> {
                if (currentTime - lastHeartbeat > HEARTBEAT_TIMEOUT) {
                    System.out.println("Client " + socket + " has timed out.");
                    closeConnection(socket);
                }
            });
        }, 0, 5, TimeUnit.SECONDS);
    }

    private static void closeConnection(Socket socket) {
        try {
            System.out.println("Closing connection to client: " + socket);
            clientHeartbeats.remove(socket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
