package activity8;


public class MyRectangle2D {
    // Private fields
    private double x;
    private double y;
    private double width;
    private double height;

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // No-arg constructor that sets values for fields
    public MyRectangle2D() {
        x = 0;
        y = 0;
        width = 1;
        height = 1;
    }

    // Parameterized constructor
    public MyRectangle2D(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Method that returns area of rectangle
    public double getArea() {
        return width * height;
    }

    // Method that returns perimeter of rectangle
    public double getPerimeter() {
        return (2 * width) + (2 * height);
    }

    // Method checking if point is inside the rectangle
    public boolean contains(double x, double y) {
        return x >= this.x && x <= (this.x + this.width) &&
                y >= this.y && y <= (this.y + this.height);
    }

    // Method that checks if a rectangle is inside a rectangle
    public boolean contains(MyRectangle2D r) {
        return r.getX() >= this.x && r.getY() >= this.y &&
                (r.getX() + r.getWidth()) <= (this.x + this.width) &&
                (r.getY() + r.getHeight()) <= (this.y + this.height);

    }

    // Method that checks if a rectangle is overlapping another rectangle
    public boolean overlaps(MyRectangle2D r){
        return r.getX() < (this.x + this.width) &&
               (r.getX() + r.getWidth()) > this.x &&
               r.getY() < (this.y + this.height) &&
               (r.getY() + r.getHeight()) > this.y;
    }

}