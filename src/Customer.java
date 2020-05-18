/**
 * Hesaplamalarımızın hepsinde kapsanıp kapsanmadığı fitness değerlerini belirleyecek müşyeriler için basit bir sınıf
 */

public class Customer {
    private int no;
    private Location location;
    private boolean hasAccess;

    public Customer(Location location, int no) {
        this.no = no;
        this.location = location;
        this.hasAccess = false;
    }
    public Customer(Customer customer) {
        this.no = customer.no;
        this.location = customer.location;
        this.hasAccess = customer.hasAccess;
    }

    @Override
    public String toString() {
        return "Customer " + no + " @ " + location;
    }

    public boolean isAccessible(SignalTower tower){
        return tower.getRadius() > this.location.distance(tower.getLocation());
    }

    public void refresh(){
        this.hasAccess = false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
