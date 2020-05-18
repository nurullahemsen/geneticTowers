/**
 * Kromozomların içnde belirli kombinasyonlarda tutulacak basit baz istasyonu objesi.
 */

public class SignalTower implements Comparable<SignalTower>{
    private int index;
    private String name;
    private Location location;
    private double radius;

    public SignalTower(Location location, double radius,int index) {
        this.index = index;
        this.name = "SignalTower" + index;
        this.location = location;
        this.radius = radius;
    }

    public SignalTower(SignalTower tower){
        this.location = tower.location;
        this.radius = tower.radius;
    }

    public boolean isAccessible(Customer customer){
        return this.radius > this.location.distance(customer.getLocation());
    }

    @Override
    public boolean equals(Object obj) {
        return this.location.equals(((SignalTower) obj).getLocation());
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return this.name + " at " + location ;
    }

    @Override
    public int compareTo(SignalTower o) {
        return this.name.compareTo(o.getName());
    }
}
