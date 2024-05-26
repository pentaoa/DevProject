package edu.sustech.students.ura.devproject.client;

/**
 * ClientManager
 * 客户端管理器
 * @version 1.0
 * 客户端管理器用于管理客户端对象, 使用单例模式，确保只有一个客户端对象
 */
public class ClientManager {
    private static Client client;

    private ClientManager() {
        // private constructor to prevent instantiation
    }

    public static Client getClient() {
        if (client == null) {
            client = new Client("localhost", 8192);
        }
        return client;
    }
}