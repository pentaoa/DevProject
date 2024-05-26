package edu.sustech.students.ura.devproject.model;

import java.util.Scanner;

public class PlayableTest {
    public static void main(String[] args) {
        System.out.println("测试指南：w(上) s(下) a(左) d(右) q(退出)");
        GameStatus status = GameStatus.getInstance();
        status.setMode(1);
        GameManager gameManager = new GameManager();
        GridNumber model = gameManager.getGrid();
        model.printNumber();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            switch (input) {
                case "w":
                    clearScreen();
                    model.moveUp();
                    break;
                case "s":
                    clearScreen();
                    model.moveDown();
                    break;
                case "a":
                    clearScreen();
                    model.moveLeft();
                    break;
                case "d":
                    clearScreen();
                    model.moveRight();
                    break;
                case "q":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Please use w, s, a, d, or q.");
                    continue;
            }
            model.printNumber();
        }
    }

    private static void clearScreen() {
        for (int i = 0; i < 5; i++) { // 打印100行空行
            System.out.println();
        }
    }
}