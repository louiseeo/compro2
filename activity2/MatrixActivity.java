package activity2;

import java.util.Scanner;

public class MatrixActivity {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // problem 1
        double matrixNumbers1[][] = new double[3][4]; // initialize a 2d array with 3 rows and 4 columns
        System.out.println("Enter a 3-by-4 matrix row by row: "); // prompt user to enter a 3x4 matrix
        for (int i = 0; i < matrixNumbers1.length; i++) {
            for (int j = 0; j < matrixNumbers1[i].length; j++) {
                matrixNumbers1[i][j] = input.nextDouble(); // reads the matrix
            }
        }

        for (int columns = 0; columns < matrixNumbers1[0].length; columns++) {
            // prints the sum of each column
            System.out
                    .println("Sum of the elements at column " + columns + " is " + sumColumn(matrixNumbers1, columns));
        }

        // add a space for another problem
        System.out.println();

        // problem 2
        double matrixNumbers2[][] = new double[4][4]; // initialize a 2d array with 4 rows and 4 columns
        System.out.println("Enter a 4-by-4 matrix row by row: ");
        for (int i = 0; i < matrixNumbers2.length; i++) {
            for (int j = 0; j < matrixNumbers2[i].length; j++) {
                matrixNumbers2[i][j] = input.nextDouble(); // reads the matrix
            }
        }
        // prints the sum of the major diagonal
        System.out.println("Sum of the elements in the major diagonal is " + sumMajorDiagonal(matrixNumbers2));

    }

    // method for problem 1
    public static double sumColumn(double[][] m, int columnIndex) {
        double sum = 0;
        for (int rows = 0; rows < m.length; rows++) {
            sum += m[rows][columnIndex];
        }
        return sum;
    }

    // method for problem 2
    public static double sumMajorDiagonal(double[][] m) {
        double sum = 0;
        for (int d = 0; d < m.length; d++) {
            sum += m[d][d];
        }
        return sum;

    }
}