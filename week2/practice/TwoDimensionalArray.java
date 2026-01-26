package week2;

public class TwoDimensionalArray {
    public static void main(String[] args) {
        String[][] clothColors = {
                { "red", "blue", "green" },
                { "orange", "yellow", "violet"}
        };
        for (int i = 0; i < clothColors.length; i++){
            for (int j = 0; j < clothColors[i].length; j++){
                System.out.printf("%-8s", clothColors[i][j]);
            }
            System.out.println();
        }
    }
}
