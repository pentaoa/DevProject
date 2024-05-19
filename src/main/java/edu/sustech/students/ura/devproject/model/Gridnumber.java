package edu.sustech.students.ura.devproject.model;

/**
 * Block
 * 块
 * @version 1.0
 * 代表游戏中的数字块，可以上下左右移动，并且可以合并。
 */



import java.util.Arrays;
import java.util.Random;


public class Gridnumber {
    private int steps;
    private final int X_COUNT;
    private final int Y_COUNT;

    private int[][] numbers;

    static Random random = new Random();

    public Gridnumber(int xCount, int yCount) {     //初始化游戏网格
        this.steps = 0;
        this.X_COUNT = xCount;
        this.Y_COUNT = yCount;
        this.numbers = new int[this.X_COUNT][this.Y_COUNT];
        this.initialNumbers();
    }

    public void initialNumbers() {//这个方法初始化网格，要求：生成一个二一个四
        for(int row =0;row<X_COUNT;row++)
        {
            for(int column = 0 ;column<Y_COUNT;column++)
            {
                numbers[row][column]=0;
            }
        }
        int initialnumber = 0;
        int whether4exist = 0;
        while(initialnumber<2) {
            int row = random.nextInt(X_COUNT);
            int column = random.nextInt(Y_COUNT);
            if(numbers[row][column]==0&&whether4exist==0){
                numbers[row][column]=4;
                whether4exist++;
                initialnumber++;
            }
            else if(numbers[row][column]==0&&whether4exist!=0){
                numbers[row][column]=2;
                initialnumber++;
            }

        }
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
    public void generateNewnumbers(Gridnumber model){/*每次移动后可以生成一个2或者4*/
        int newnumber = 0;
        while (newnumber==0){
            int row =random.nextInt(model.getX_COUNT());
            int column = random.nextInt(model.getY_COUNT());
            if(model.getNumber(row,column)==0){
                model.setNumbers(row,column,random.nextInt(2)==0?2:4);
                newnumber++;
            }
        }
    }
    //移动的逻辑：要成功移动，才会产生新的网格并且增加“step”
    public void moveRight() {
        boolean moveSuccessfully = false;
        for(int row = 0;row<X_COUNT;row++)
        {
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                boolean whetherAllZero2 = true;//判断自己以及左方是否全为0
                for (int j = column; j >= 0; j--) {
                    if (numbers[row][j] != 0) {
                        whetherAllZero2 = false;
                    }
                }
                if (whetherAllZero2 == false) {
                    while (numbers[row][column] == 0 && column != 0) {
                        for(int columntransit = column;columntransit>=0;columntransit--)
                        {
                            if(columntransit!=0) {
                                numbers[row][columntransit] = numbers[row][columntransit-1];
                            }
                            else
                            {
                                numbers[row][columntransit]=0;
                            }
                        }
                        moveSuccessfully =true;
                    }
                }
            }
            for(int column = Y_COUNT-1;column>=0;column--)
            {
                boolean whetherAllZero2 = true;//判断自己以及左方是否全为0
                for (int j = column; j >= 0; j--) {
                    if (numbers[row][j] != 0) {
                        whetherAllZero2 = false;
                    }
                }
                if (whetherAllZero2==false&&column != 0 && numbers[row][column] == numbers[row][column - 1]) {
                    numbers[row][column] *= 2;
                    for (int j = column - 1; j >= 0; j--) {
                        if (j != 0) {
                            numbers[row][j] = numbers[row][j - 1];
                        } else {
                            numbers[row][j] = 0;
                        }
                    }
                    moveSuccessfully =true;
                }
            }

        }
        if(moveSuccessfully==true)
        {
            generateNewnumbers(this);
            steps++;
        }
        /*for (int i = 0; i < numbers.length; i++) {
            numbers[i][1] += numbers[i][0];
            numbers[i][0] = 0;
        }*/
    }

    public void moveLeft(){
        boolean moveSuccessfully = false;
        for(int row = 0;row<X_COUNT;row++)
        {
            for (int column = 0; column < Y_COUNT; column++) {
                boolean whetherAllZero2 = true;//判断右方是否全为0
                for (int j = column; j < Y_COUNT; j++) {
                    if (numbers[row][j] != 0) {
                        whetherAllZero2 = false;
                    }
                }
                if (whetherAllZero2 == false) {
                    while (numbers[row][column] == 0 && column != Y_COUNT-1) {
                        moveSuccessfully = true;
                        for(int columntransit = column;columntransit<Y_COUNT;columntransit++)
                        {
                            if(columntransit!=Y_COUNT-1) {
                                numbers[row][columntransit] = numbers[row][columntransit+1];
                            }
                            else
                            {
                                numbers[row][columntransit]=0;
                            }
                        }
                    }
                }
            }
            for(int column = 0 ;column<Y_COUNT;column++)
            {
                boolean whetherAllZero2 = true;//判断右方是否全为0
                for (int j = column; j < Y_COUNT; j++) {
                    if (numbers[row][j] != 0) {
                        whetherAllZero2 = false;
                    }
                }
                if (whetherAllZero2==false&&column != Y_COUNT-1 && numbers[row][column] == numbers[row][column + 1]) {
                    moveSuccessfully = true;
                    numbers[row][column] *= 2;
                    for (int j = column +1; j < Y_COUNT; j++) {
                        if (j != Y_COUNT-1) {
                            numbers[row][j] = numbers[row][j + 1];
                        } else {
                            numbers[row][j] = 0;
                        }
                    }
                }
            }
        }
        if(moveSuccessfully==true){
            generateNewnumbers(this);
            steps++;
        }
    }
    public void moveUp(){
        boolean moveSuccessfully = false;
        for(int column = 0 ; column<Y_COUNT;column++)
        {
            for(int row = 0;row<X_COUNT;row++)
            {
                boolean whetherAllZero = true;
                for(int rowtransmit = row ;rowtransmit<X_COUNT;rowtransmit++)
                {
                    if(numbers[rowtransmit][column]!=0)
                    {
                        whetherAllZero =false;
                    }
                }
                if(whetherAllZero==false)
                {
                    while(row!=X_COUNT-1&&numbers[row][column]==0)
                    {
                        moveSuccessfully=true;
                        for(int rowtransmit = row;rowtransmit<X_COUNT;rowtransmit++)
                        {
                            if(rowtransmit!=X_COUNT-1)
                            {
                                numbers[rowtransmit][column]=numbers[rowtransmit+1][column];
                            }
                            else {
                                numbers[rowtransmit][column]=0;
                            }
                        }
                    }

                }
            }
            for(int row = 0;row<X_COUNT;row++){
                boolean whetherAllZero = true;
                for(int rowtransmit = row ;rowtransmit<X_COUNT;rowtransmit++)
                {
                    if(numbers[rowtransmit][column]!=0)
                    {
                        whetherAllZero =false;
                    }
                }
                if(whetherAllZero==false&&row!=X_COUNT-1&&numbers[row][column]==numbers[row+1][column]){
                    moveSuccessfully = true;
                    numbers[row][column]*=2;
                    for(int rowtransmit = row+1;rowtransmit<X_COUNT;rowtransmit++)
                    {
                        if(rowtransmit!=X_COUNT-1)
                        {
                            numbers[rowtransmit][column]=numbers[rowtransmit+1][column];
                        }
                        else {
                            numbers[rowtransmit][column]=0;
                        }
                    }
                }
            }
        }
        if(moveSuccessfully==true)
        {
            generateNewnumbers(this);
            steps++;
        }
    }
    public void moveDown(){
        boolean moveSuccessfully = false;
        for(int column = 0 ; column<Y_COUNT;column++)
        {
            for(int row = X_COUNT-1;row>=0;row--)
            {
                boolean whetherAllZero = true;//检测上方是否全为零
                for(int rowtransmit = row ;rowtransmit>=0;rowtransmit--)
                {
                    if(numbers[rowtransmit][column]!=0)
                    {
                        whetherAllZero =false;
                    }
                }
                if(whetherAllZero==false)
                {
                    while(row!=0&&numbers[row][column]==0)
                    {
                        moveSuccessfully = true;
                        for(int rowtransmit = row;rowtransmit>=0;rowtransmit--)
                        {
                            if(rowtransmit!=0)
                            {
                                numbers[rowtransmit][column]=numbers[rowtransmit-1][column];
                            }
                            else {
                                numbers[rowtransmit][column]=0;
                            }
                        }
                    }

                }
            }
            for(int row = X_COUNT-1;row>=0;row--){
                boolean whetherAllZero = true;//检测上方是否全为零
                for(int rowtransmit = row ;rowtransmit>=0;rowtransmit--)
                {
                    if(numbers[rowtransmit][column]!=0)
                    {
                        whetherAllZero =false;
                    }
                }
                if(whetherAllZero==false&&row!=0&&numbers[row][column]==numbers[row-1][column]){
                    moveSuccessfully = true;
                    numbers[row][column]*=2;
                    for(int rowtransmit = row-1;rowtransmit>=0;rowtransmit--)
                    {
                        if(rowtransmit!=0)
                        {
                            numbers[rowtransmit][column]=numbers[rowtransmit-1][column];
                        }
                        else {
                            numbers[rowtransmit][column]=0;
                        }
                    }
                }
            }
        }
        if(moveSuccessfully==true)
        {
            generateNewnumbers(this);
            steps++;
        }
    }
    public int getNumber(int i, int j) {
        return numbers[i][j];
    }
    public void setNumbers(int row , int column, int num){
        numbers[row][column]=num;
    }
    public int getX_COUNT(){return X_COUNT;}
    public int getY_COUNT(){return Y_COUNT;}

    public void printNumber() {
        for (int[] line : numbers) {
            System.out.println(Arrays.toString(line));
        }
    }
}
