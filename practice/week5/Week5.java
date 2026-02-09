package practice.week5;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Week5 {
    public static void main(String[] args) {
        List<Fruit> fruits = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("data.csv"));
            String line;
            br.readLine(); // discard header
            int i = 0; // array counter
            while ((line = br.readLine()) != null) {
                // convert the `line` into String array
                String[] arr = line.split(",");
                Fruit f = new Fruit();
                f.name = arr[0];
                f.price = Float.parseFloat(arr[2]);
                f.color = arr[1];
                f.shelfLife = Integer.parseInt(arr[3]);
                fruits.add(f); // add the current fruit to current available slot of the array

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Fruit f = fruits.get(0);
        System.out.println(f.name);

    }
}

class Fruit {
    String name;
    String color;
    float price;
    int shelfLife;

}