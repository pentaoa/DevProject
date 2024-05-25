package edu.sustech.students.ura.devproject.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

/**
 * GridNumber
 * 块
 *
 * @version 1.0
 * 代表游戏中的数字块，可以上下左右移动，并且可以合并。
 */

public class GridNumber {
    private int steps;
    private final int X_COUNT;
    private final int Y_COUNT;
    private int obstaclenumber;

    private IntegerProperty[][] numbers;

    static Random random = new Random();

    public GridNumber(int xCount, int yCount) {
        //初始化游戏网格
        this.steps = 0;
        this.X_COUNT = xCount;
        this.Y_COUNT = yCount;
        numbers = new IntegerProperty[X_COUNT][Y_COUNT];
        this.initialNumbers();
        while(obstaclenumber==0)
        {
            int row = random.nextInt(4);
            int column = random.nextInt(4);
            if(numbers[row][column].get()==0)
            {
                numbers[row][column].setValue(-1);
                obstaclenumber++;
            }
        }
    }

    public void initialNumbers() {
        //这个方法初始化网格，要求：生成一个二一个四
        for (int row = 0; row < X_COUNT; row++) {
            for (int column = 0; column < Y_COUNT; column++) {
                numbers[row][column] = new SimpleIntegerProperty(0);
            }
        }
        int initialNumber = 0;
        int whether4exist = 0;
        while (initialNumber < 2) {
            int row = random.nextInt(X_COUNT);
            int column = random.nextInt(Y_COUNT);
            if (numbers[row][column].get() == 0 && whether4exist == 0) {
                numbers[row][column].setValue(4);
                whether4exist++;
                initialNumber++;
            } else if (numbers[row][column].get() == 0 && whether4exist != 0) {
                numbers[row][column].setValue(2);
                initialNumber++;
            }

        }
        this.steps=0;
           /* for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[i].length; j++) {
                    //todo: update generate numbers method
                    numbers[i][j] = random.nextInt(2) == 0 ? 1 : 0;
                }
            }*/
    }
    //todo: finish the method of four direction moving./*这几个移动都是先重新排序，保证数与数之间没有“0”，然后，检测这个数是否可以和下一个数
    //        拼在一起（前提是这个数本身以及他左边的数补全是零，并且这个数与下一个数相同），拼在一起后，执行常规的移动操作
    //        */
    public void generateNewNumbers(GridNumber model) {/*每次移动后可以生成一个2或者4*/
        int newNumber = 0;
        while (newNumber == 0) {
            int row = random.nextInt(model.getX_COUNT());
            int column = random.nextInt(model.getY_COUNT());
            if (model.getNumber(row, column).getValue() == 0) {
                model.setNumbers(row, column, random.nextInt(2) == 0 ? 2 : 4);
                newNumber++;
            }
        }
    }

    // TODO: 有空可以使用参数来降低代码复用率
    //移动的逻辑：要成功移动，才会产生新的网格并且增加“step”
    public void moveRight() {
        boolean moveSuccessfully = false;
        for (int row = 0; row < X_COUNT; row++) {
            // Step 1: Move all non-zero elements to the right
            int targetColumn = Y_COUNT - 1; // The target column for the next non-zero element
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1) {// Only move if it's not already in the correct position
                            targetColumn=column;
                        }else if (numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn--;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int column = Y_COUNT - 1; column > 0; column--) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row][column - 1].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row][column - 1].setValue(0);
                    moveSuccessfully = true;
                    column--; // Skip the next column since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetColumn = Y_COUNT - 1;
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1) {// Only move if it's not already in the correct position
                            targetColumn=column;
                        }else if (numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn--;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;
        }
    }

    public void moveLeft() {
        boolean moveSuccessfully = false;
        for (int row = 0; row < X_COUNT; row++) {
            // Step 1: Move all non-zero elements to the left
            int targetColumn = 0; // The target column for the next non-zero element
            for (int column = 0; column < Y_COUNT; column++) {

                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetColumn = column;
                        }
                        else if(numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn++;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int column = 0; column < Y_COUNT - 1; column++) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row][column + 1].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row][column + 1].setValue(0);
                    moveSuccessfully = true;
                    column++; // Skip the next column since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetColumn = 0;
            for (int column = 0; column < Y_COUNT; column++) {

                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetColumn = column;
                        }
                        else if(numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn++;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;
        }
    }

    public void moveUp() {
        boolean moveSuccessfully = false;
        for (int column = 0; column < Y_COUNT; column++) {
            // Step 1: Move all non-zero elements upwards
            int targetRow = 0; // The target row for the next non-zero element
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow++;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int row = 0; row < X_COUNT - 1; row++) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row + 1][column].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row + 1][column].setValue(0);
                    moveSuccessfully = true;
                    row++; // Skip the next row since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetRow = 0;
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow++;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;
        }
    }

    public void moveDown() {
        boolean moveSuccessfully = false;
        for (int column = 0; column < Y_COUNT; column++) {
            // Step 1: Move all non-zero elements downwards
            int targetRow = X_COUNT - 1; // The target row for the next non-zero element
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                        targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow--;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int row = X_COUNT - 1; row > 0; row--) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row - 1][column].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row - 1][column].setValue(0);
                    moveSuccessfully = true;
                    row--; // Skip the next row since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetRow = X_COUNT - 1;
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow--;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;

        }
    }
    public IntegerProperty[][] getNumbers(){
        return numbers;
    }

    public IntegerProperty getNumber(int i, int j) {
        return numbers[i][j];
    }

    public void setNumbers(int row, int column, int num) {
        numbers[row][column].setValue(num);
    }

    public int getX_COUNT() {
        return X_COUNT;
    }

    public int getY_COUNT() {
        return Y_COUNT;
    }
    public boolean isGameOver() {
        return !hasEmptyCells() && !canMoveUp() && !canMoveDown() && !canMoveLeft() && !canMoveRight();//当全是空格且不能上下左右移动时，判定失败(返回true)，加一个空格判断是为了提高判断效率
    }

    private boolean hasEmptyCells() {//检测有没有空格子
        for (int row = 0; row < X_COUNT; row++) {
            for (int col = 0; col < Y_COUNT; col++) {
                if (numbers[row][col].getValue() == 0) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean canMoveUp() {//之所以要再单独写一个判断能不能移动的方法，是为了减少耦合，
        /*检测这列是否可以移动，即，当上方存在空格且目前这个不是空格时，或者上方这个格子与目前这个
        格子相等时，表示可以向上移动，检测第0行没有意义，因为如果第0行为0，那这个判定条件以及包含在
        第一行的检测中，如果第0行不为0，那就会跳到下行的检测中（能否向上移动，要看下面的格子能不能
        和它合并），所以不如一开始就检测第二行，剩下几个判定方法的原理类似
        */
        for (int col = 0; col < Y_COUNT; col++) {
            for (int row = 1; row < X_COUNT; row++) {
                if (numbers[row][col].getValue() != 0 && (numbers[row - 1][col].getValue() == 0 || numbers[row - 1][col].getValue() == numbers[row][col].getValue())) {
                    return true; // 当前列存在可上移或合并的情况
                }
            }
        }
        return false;
    }
    private boolean canMoveDown() {
        for (int col = 0; col < Y_COUNT; col++) {
            for (int row = X_COUNT - 2; row >= 0; row--) {
                if (numbers[row][col].getValue() != 0 && (numbers[row + 1][col].getValue() == 0 || numbers[row + 1][col].getValue() == numbers[row][col].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean canMoveLeft() {
        for (int row = 0; row < X_COUNT; row++) {
            for (int col = 1; col < Y_COUNT; col++) {
                if (numbers[row][col].getValue() != 0 && (numbers[row][col - 1].getValue() == 0 || numbers[row][col - 1].getValue() == numbers[row][col].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean canMoveRight() {
        for (int row = 0; row < X_COUNT; row++) {
            for (int col = Y_COUNT - 2; col >= 0; col--) {
                if (numbers[row][col].getValue() != 0 && (numbers[row][col + 1].getValue() == 0 || numbers[row][col + 1].getValue() == numbers[row][col].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void printNumber() {
        for (IntegerProperty[] number : numbers) {
            for (IntegerProperty integerProperty : number) {
                System.out.print(integerProperty.get() + " ");
            }
            System.out.println();
        }
    }
    public int getSteps(){return steps;}
    public int checkWin(){//检查游戏目前最高分的格子是多少分
        int Max = 0;
        for(int row =0;row<this.numbers.length;row++){
            for(int column = 0;column<this.numbers[row].length;column++){
                if(this.numbers[row][column].getValue()>Max){
                    Max = this.numbers[row][column].getValue();
                }
            }
        }
        return Max;
    }
    public void resetGrid() {
        for (int row = 0; row < numbers.length; row++) {
            for (int column = 0; column < numbers[row].length; column++) {
                numbers[row][column].setValue(0);
            }
        }
        int initialNumber = 0;
        int whether4exist = 0;
        while (initialNumber < 2) {
            int row = random.nextInt(X_COUNT);
            int column = random.nextInt(Y_COUNT);
            if (numbers[row][column].get() == 0 && whether4exist == 0) {
                numbers[row][column].setValue(4);
                whether4exist++;
                initialNumber++;
            } else if (numbers[row][column].get() == 0 && whether4exist != 0) {
                numbers[row][column].setValue(2);
                initialNumber++;
            }

        }
        obstaclenumber = 0;
        while(obstaclenumber==0)
        {
            int row = random.nextInt(4);
            int column = random.nextInt(4);
            if(numbers[row][column].get()==0)
            {
                numbers[row][column].setValue(-1);
                obstaclenumber++;
            }
        }
        this.steps = 0;
    }
}
