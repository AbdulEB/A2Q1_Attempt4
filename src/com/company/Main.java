package com.company;

/* Author: Abdul El Badaoui
 * Student Number: 5745716
 * Description: The following program runs a backtracking algorithm to find the number of inequivalent latin squares for
 * a given n.
 * */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static Scanner userInput;
    static int n;
    static ArrayList<int [][]> checkingMatrix;
    static long count;
    static int fillCount;
    static PrintWriter fileOutput;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        n = 0;
        count =0;
        checkingMatrix = new ArrayList<>(20000000);
        userInput = new Scanner(System.in);
        //while loop to input a valid integer for an existing file path name or insert your own
        while (n>8 || n<3 ){
            System.out.println("Please enter an integer value from 3 to 11 for the latin squares");
            //check if an integer was input
            while (!userInput.hasNextInt()) {
                System.out.println("That's not a preferred number!");
                userInput.next();
            }
            n = userInput.nextInt();
        }
        fileOutput = new PrintWriter("a2q1out for n="+n+".txt", "UTF-8");// output file creation
        long startTime = System.nanoTime();//start time
        int [][]latinSquare =  new int[n][n];
        //initializing the latin square in reduced form
        for (int i = 1; i<=n; i++){
            for (int j = 1; j<=n; j++){
                if (i==1){
                    latinSquare[i-1][j-1] = j;
                }
                else if (j==1){
                    latinSquare[i-1][j-1] = i;
                }
                else{
                    latinSquare[i-1][j-1]=0;
                }
            }
        }


        //initialize the fillCount to 0
        fillCount =0;
        // uses the fillCount to increment the row and column to passes to the next cell
        int row = ((fillCount/(n-1))+1);
        int column = ((fillCount%(n-1))+1);
        //run the backTrack method
        backTrack(row, column, latinSquare);
        fileOutput.println("The number of inequivalent Squares are " +count);
        long endTime   = System.nanoTime();//end time
        long totalTime = endTime - startTime;//total time
        //put time in seconds
        double seconds = (double)totalTime / 1000000000.0;
        //print time the algorithm ran for in respect to n
        fileOutput.println("The time to run for n = "+n+ ", is " +seconds+ " seconds");
        fileOutput.close();
    }
    //backtrack method
    public static void backTrack(int row, int column, int [][] latinSquare){

        if (fillCount==(n-1)*(n-1)){//if latin square is filled check to see if it is an equivalent exiting latin square
            if(!checkSquares(latinSquare)){//if not equivalent
                count++;//increment count
                if (count <= 4) {//if count is less than four print the four found latin Square
                fileOutput.println(Arrays.deepToString(latinSquare).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
                fileOutput.println("------------------------------------------------------");
                }

            }
        }
        else{
            //for loop to identify if candidate can be placed at the current cell
            for (int num = 1; num<=n ; num++){
                latinSquare[row][column] = checkCandidate(row, column, num, latinSquare);
                if (latinSquare[row][column] != 0){//current cell is filled
                    //increment fillCount and go to the next cell
                    fillCount++;
                    row = ((fillCount/(n-1))+1);
                    column = ((fillCount%(n-1))+1);
                    //run the backTrack method for the next cell
                    backTrack(row, column, latinSquare);
                    //decrement fillCount, and return to the previous cell
                    fillCount--;
                    row=((fillCount/(n-1))+1);
                    column=((fillCount%(n-1))+1);
                    //reassign latinSquare back to 0
                    latinSquare[row][column] = 0;

                }
            }

        }
    }
    //method to check if the candidate can be placed at the current cell in the latin square
    public static int checkCandidate(int row, int column, int candidate, int [][] currentSquare){

        for (int column_index =0; column_index<column; column_index++){
            if (currentSquare[row][column_index]==candidate){
                return 0;
            }
        }
        //checks column if candidate already exists
        for (int row_index =0; row_index<row; row_index++){
            if (currentSquare[row_index][column]==candidate){
                return 0;
            }
        }

        return candidate;
    }
    //method check the square if it is inequivalent to all existing inequivalent latin squares
    public static boolean checkSquares(int [][] currentSquare){
        int [][] squareToCompareTo = squareCopy(currentSquare);//make a copy of the passed in latin square
        //if list is empty the for latin square is inequivalent
        if (checkingMatrix.isEmpty()){
            checkingMatrix.add(squareToCompareTo);
            return false;
        }

        //also go through the list of the exisiting inequivalent latin square and run a compare method
        // to check if they are different
        for (int i = 0; i<checkingMatrix.size(); i++){
            int [][] squareToCompareAgainst = squareCopy(checkingMatrix.get(i));
            if (comparingSquares(squareToCompareAgainst, squareToCompareTo)) return true;

        }
        //make another copy so java references doesn't interfere with your square checking
        int [][] squareToCompareTo2 = squareCopy(currentSquare);

        checkingMatrix.add(squareToCompareTo2);//added to the list


        return false;//return false
    }
    //make a copy of the latin square so they won't be lost in java references and tobe used when
    //grabbing squares from the checkMatrix list
    public static int [][] squareCopy(int [][] squareToCopy){
        int [][] duplicateSquare = new int[squareToCopy.length][];
        for(int j = 0; j < squareToCopy.length; j++){
            duplicateSquare[j] = squareToCopy[j].clone();
        }
        return duplicateSquare;
    }
    //boolean method to check if the squares are different
    public static boolean comparingSquares(int[][] squareOriginal, int [][] squareToCheck){

        int [][] copingSquare = squareCopy(squareToCheck);//make a copy of the square
        for (int i = 1; i<n; i++){
            copingSquare = orderingRow(copingSquare, i);//method to permutate a given row to ascending order
            if(areEqualSquares(squareOriginal, copingSquare)) return true;//if the same return true
            copingSquare = squareCopy(squareToCheck);//reset the copied square
        }



        for (int i = 1; i<n; i++){
            copingSquare = orderingColumn(copingSquare, i);//method to permutate a given column to ascending order
            if(areEqualSquares(squareOriginal, copingSquare)) return true;//if the same return true
            copingSquare = squareCopy(squareToCheck);//reset the copied square
        }

        return false;//return false since the squares are different
    }
    //method to permutate the row in ascending order
    public static int [][] orderingRow(int [][] rearrangedSquare, int row){
        //calls a method to swap columns to place cell value in their respective column index
        for (int i = 0; i<n; i++){
            if(rearrangedSquare[row][i] != i+1){
                for (int j=i; j<n; j++){
                    if (rearrangedSquare[row][j] == i+1){
                        rearrangedSquare = permutateColumn(rearrangedSquare, i, j);
                        break;
                    }
                }
            }
        }

        //calls a method to swap rows to place cell value in the first column to their respective row index
        for (int i = 0; i<n; i++){
            if(rearrangedSquare[i][0] != i+1){
                for (int j=i; j<n; j++){
                    if (rearrangedSquare[j][0] == i+1){
                        rearrangedSquare = permutateRow(rearrangedSquare, i, j);
                        break;
                    }
                }
            }
        }



        return rearrangedSquare;//return the rearranged square
    }
    //method to permutate the columns in ascending order
    public static int [][] orderingColumn(int [][] rearrangedSquare, int column){
        //calls a method to swap rows to place cell value in their respective row index
        for (int i = 0; i<n; i++){
            if(rearrangedSquare[i][column] != i+1){
                for (int j=i; j<n; j++){
                    if (rearrangedSquare[j][column] == i+1){
                        rearrangedSquare = permutateRow(rearrangedSquare, i, j);
                        break;
                    }
                }
            }
        }
        //calls a method to swap columns to place cell value in the first row to their respective column index
        for (int i = 0; i<n; i++){
            if(rearrangedSquare[0][i] != i+1){
                for (int j=i; j<n; j++){
                    if (rearrangedSquare[0][j] == i+1){
                        rearrangedSquare = permutateColumn(rearrangedSquare, i, j);
                        break;
                    }
                }
            }
        }
      return rearrangedSquare;//return the rearranged square
    }
    //method to swap the columns in the square
    public static int[][] permutateColumn(int[][] square, int columnI, int columnJ){

        for (int i = 0; i<n; i++){
            int temp = square[i][columnI];
            square[i][columnI] = square[i][columnJ];
            square[i][columnJ] = temp;
        }

        return square;
    }
    //,ethod to swap the rows in the square
    public static int[][] permutateRow(int[][] square, int rowI, int rowJ){

        for (int i = 0; i<n; i++){
            int temp = square[rowI][i];
            square[rowI][i] = square[rowJ][i];
            square[rowJ][i] = temp;
        }

        return square;
    }
    //method to check if the square is equal
    public static boolean areEqualSquares(int [][] square1, int [][] square2){
//        System.out.println(Arrays.deepToString(square1).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
//        System.out.println("------------------------------------------------------");
//        System.out.println(Arrays.deepToString(square2).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
//        System.out.println("------------------------------------------------------");
        for (int i = 0; i<n; i++){
            for (int j = 0; j<n; j++){
                if (square1[i][j] != square2[i][j]) return false;
            }
        }

        return true;
    }

}
