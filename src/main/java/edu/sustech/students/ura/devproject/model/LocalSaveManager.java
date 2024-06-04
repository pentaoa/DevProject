package edu.sustech.students.ura.devproject.model;

import javafx.scene.control.Alert;

import java.io.*;

public class LocalSaveManager {
    private GameStatus status = GameStatus.getInstance();
    private static final String SAVE_FILE_PATH = "src/main/java/edu/sustech/students/ura/devproject/model/savegame.okcome";

    public void save(GameManager gameManager) {
        status.setGridNumber(gameManager.getGridNumber());
        status.setScore(gameManager.getScore());
        status.setSteps(gameManager.getSteps());
        status.setTime(gameManager.getElapsedTime());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(status);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("保存成功");
            alert.setContentText("分数：" + gameManager.getScore() + "  步数：" + gameManager.getSteps() + "  时间：" + gameManager.getElapsedTime());
            alert.showAndWait();
            System.out.println("成功保存游戏");
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("保存失败");
            alert.setContentText("没有找到用户信息文件，尝试新建文件");
            alert.showAndWait();
            System.out.println("没有找到用户信息文件，尝试新建文件");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("保存失败");
            alert.setContentText("无法保存游戏：" + e.getMessage());
            alert.showAndWait();
            System.err.println("无法保存游戏：" + e.getMessage());
        }
    }

    public void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            GameStatus.setInstance((GameStatus) ois.readObject());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("加载成功");
            alert.setContentText("分数：" + status.getScore() + "  步数：" + status.getSteps() + "  时间：" + status.getTime());
            alert.showAndWait();
            System.out.println("成功加载游戏");
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("加载失败");
            alert.setContentText("没有找到保存文件：" + SAVE_FILE_PATH);
            alert.showAndWait();
            System.err.println("没有找到保存文件：" + SAVE_FILE_PATH);
        } catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("加载失败");
            alert.setContentText("无法加载游戏：" + e.getMessage());
            System.err.println("无法加载游戏：" + e.getMessage());
        }
    }

    public void autoSave(GameManager gameManager) {
        status.setGridNumber(gameManager.getGridNumber());
        status.setScore(gameManager.getScore());
        status.setSteps(gameManager.getSteps());
        status.setTime(gameManager.getElapsedTime());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(status);
        } catch (IOException e) {
            System.err.println("无法保存游戏：" + e.getMessage());
        }
    }
}
