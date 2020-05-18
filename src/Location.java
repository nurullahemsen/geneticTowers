import java.util.Random;

/**
 * Hem Baz istasyonlarında hemde müşterilerde kullanılacak x ve y eksenine sahip, birbiri arasındaki uzaklığı bulabilen
 * basit bir konum sınıfı.
 */

public class Location {
    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Location(double maxX, double maxY, Random random){
        this.x = random.nextDouble()*maxX;
        this.y = random.nextDouble()*maxY;
    }

    public double distance(Location anotherLocation){
        return Math.sqrt(Math.pow(anotherLocation.getX()- this.x,2) + Math.pow(anotherLocation.getY()- this.y,2));
    }

    @Override
    public boolean equals(Object obj) {
        return this.x == ((Location) obj).x && this.y == ((Location) obj).y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)",x,y);
    }
}
