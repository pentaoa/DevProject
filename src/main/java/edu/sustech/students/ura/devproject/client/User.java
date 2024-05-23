package edu.sustech.students.ura.devproject.client;

import edu.sustech.students.ura.devproject.model.*;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private  static final long serialVersionUID = 1L; // 在序列化和反序列化过程中保持版本的兼容性，避免因类的修改导致的反序列化失败

    private String username;
    private String password;
    private GameManager gameManager;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}