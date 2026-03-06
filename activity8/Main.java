package activity8;

public class Main {
    public static void main(String[] args) {
     
        // Create a rectangle object
        MyRectangle2D rectangle = new MyRectangle2D(100, 100, 200, 200);

        // Find the area
        double area = rectangle.getArea();
        System.out.println("Area: " + area + " m²");

        // Find the perimeter
        double perimeter = rectangle.getPerimeter();
        System.out.println("Perimeter: " + perimeter + " m²");

        //Check is specified point is inside the rectangle
        boolean point = rectangle.contains(65, 30);
        if (point)
            System.out.println("The specified point is inside the rectangle.");
        else
             System.out.println("The specified point is not inside the rectangle.");
        
        // Check if the specified rectangle is inside the rectangle
        boolean cont = rectangle.contains(rectangle);
        if (cont)
            System.out.println("The specified rectangle is inside the rectangle.");
        else
            System.out.println("The specified rectangle is not inside the rectangle.");
            
        //Check if rectang


    }
    
}
