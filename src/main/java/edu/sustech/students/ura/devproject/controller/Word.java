package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;
import edu.sustech.students.ura.devproject.model.GameStatus;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.Random;

public class Word extends Label {
    GameStatus status = GameStatus.getInstance();
    private static Word instance;
    Random random = new Random();

    private Word() {
        setStyle("-fx-font-size: 25px; -fx-font-family: HYWenHei-85W; -fx-text-fill: #776e65;");
    }

    public void updateWord() {
        if (status.isOnlineGame() == true) {
            this.setText("现在是在线模式");
        } else {
            this.setText("现在是游客模式");
        }
    }
    public void updateWord(String string){
        this.setText(string);
    }

    public void updateWord2(){
        int randomWord = random.nextInt(4);
        switch (randomWord){
            case 0:
                if (status.getScore() < 64) {
                    setStyle("-fx-font-size: 20px; -fx-font-family: HYWenHei-85W; -fx-text-fill: #776e65;");
                    this.setText("我这么聪明都玩不到66\n分，你能吗？");
                }
                else {
                    setStyle("-fx-font-size: 20px; -fx-font-family: HYWenHei-85W; -fx-text-fill: #776e65;");
                    this.setText("你居然玩到66分了，\n你是不是开挂了🤬。");
                }
                break;
            case 1:
                setStyle("-fx-font-size: 20px; -fx-font-family: HYWenHei-85W; -fx-text-fill: #776e65;");
                this.setText("多摸摸我的头，没准我会\n告诉你下一步走哪里好。");
                break;
            case 2:
                setStyle("-fx-font-size: 20px; -fx-font-family: HYWenHei-85W; -fx-text-fill: #776e65;");
                this.setText("点击我的好朋友mona，\n我就会帮你走一步");
                break;
            case 3:
                setStyle("-fx-font-size: 20px; -fx-font-family: HYWenHei-85W; -fx-text-fill: #776e65;");
                this.setText("作业写完了吗？\n就在这打游戏");
                break;
        }

    }

    public static synchronized Word getInstance() {
        if (instance == null) {
            instance = new Word();
        }
        return instance;
    }
}

