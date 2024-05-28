package edu.sustech.students.ura.devproject.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String USERS_FILE = "src/main/java/edu/sustech/students/ura/devproject/server/users.okgo";
    private Map<String, User> users;

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    // 加载用户信息
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("没有找到用户信息文件，尝试新建文件");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 保存用户信息
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 注册用户
    public synchronized boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // 用户名已存在
        }
        User newUser = new User(username, password);
        users.put(username, newUser);
        saveUsers();
        return true;
    }

    // 登录用户
    public synchronized boolean loginUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    // 修改密码
    public synchronized boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            return false;
        }
        user.setPassword(newPassword);
        saveUsers();
        return true;
    }

    public synchronized boolean setHighScore(String username, int mode, int score) {
        User user = users.get(username);
        if (user == null) {
            return false;
        }
        switch (mode) {
            case 1:
                if (score > user.getEasyModeHighScore()) {
                    user.setEasyModeHighScore(score);
                }
                break;
            case 3:
                if (score > user.getTimeModeHighScore()) {
                    user.setTimeModeHighScore(score);
                }
                break;
            case 2:
                if (score > user.getObstacleModeHighScore()) {
                    user.setObstacleModeHighScore(score);
                }
                break;
            default:
                return false;
        }
        saveUsers();
        return true;
    }
}
