package edu.sustech.students.ura.devproject.model;

import java.io.*;

public class LocalSaveManager {
    private static final String SAVE_FILE_PATH = "src/main/java/edu/sustech/students/ura/devproject/model/savegame.okcome";

    public void save(GameManager gameManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(gameManager);
            System.out.println("成功保存游戏");
        } catch (FileNotFoundException e) {
            System.out.println("没有找到用户信息文件，尝试新建文件");
        } catch (IOException e) {
            System.err.println("无法保存游戏：" + e.getMessage());
        }
    }

    public GameManager load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            GameManager gameManager = (GameManager) ois.readObject();
            System.out.println("成功加载游戏");
            return gameManager;
        } catch (FileNotFoundException e) {
            System.err.println("没有找到保存文件：" + SAVE_FILE_PATH);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("无法加载游戏：" + e.getMessage());
        }
        return null;
    }
}
