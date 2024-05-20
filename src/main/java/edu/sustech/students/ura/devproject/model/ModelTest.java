package edu.sustech.students.ura.devproject.model;

public class ModelTest {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        GridNumber grid = gameManager.grid;
        grid.printNumber();
        System.out.println();
        grid.moveDown();
        grid.printNumber();
    }
}
