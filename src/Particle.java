import java.util.ArrayList;
import java.util.Collections;

public class Particle {



    private ArrayList<SignalTower> signalTowers;
    private ArrayList<Customer> customers;
    private ArrayList<SignalTower> velocitySignalTowers;
    private ArrayList<SignalTower> bestSignalTowers;





    public Particle(ArrayList<Customer> customers, ArrayList<SignalTower> signalTowers){
        this.signalTowers = signalTowers;
        this.customers = customers;
        this.bestSignalTowers = new ArrayList<SignalTower>();
        this.velocitySignalTowers = new ArrayList<SignalTower>();
        for(int i = 0; i< signalTowers.size(); i++)this.velocitySignalTowers.add(new SignalTower(new Location(0,0),PSO.RADIUS, i));
        for (int i = 0; i < this.getSignalTowers().size(); i++) {
            this.bestSignalTowers.add(new SignalTower(new Location(this.getSignalTowers().get(i).getLocation().getX(),this.getSignalTowers().get(i).getLocation().getY()),PSO.RADIUS, i));

        }
    }


    public Particle(Particle p){
        this.signalTowers = new ArrayList<SignalTower>();
        this.customers = p.customers;
        this.velocitySignalTowers = p.velocitySignalTowers;
        this.bestSignalTowers = p.bestSignalTowers;
        for (int i = 0; i < p.getSignalTowers().size(); i++) {
            this.signalTowers.add(new SignalTower(new Location(p.getSignalTowers().get(i).getLocation().getX(),p.getSignalTowers().get(i).getLocation().getY()),PSO.RADIUS, i));

        }
    }


    public int computeFitness(){
        int count = 0;
        for (Customer customer : this.customers){
            for (SignalTower signalTower : this.signalTowers){
                if(signalTower.isAccessible(customer)) {
//                    customer.setHasAccess(true);
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public int computeBestFitness(){
        int count = 0;
        for (Customer customer : this.customers){
            for (SignalTower signalTower : this.bestSignalTowers){
                if(signalTower.isAccessible(customer)) {
//                    customer.setHasAccess(true);
                    count++;
                    break;
                }
            }
        }
        return count;
    }



    public ArrayList<SignalTower> getSignalTowers() {
        return signalTowers;
    }

    public void setSignalTowers(ArrayList<SignalTower> signalTowers) {
        this.signalTowers = signalTowers;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<SignalTower> getVelocitySignalTowers() {
        return velocitySignalTowers;
    }

    public void setVelocitySignalTowers(ArrayList<SignalTower> velocitySignalTowers) {
        this.velocitySignalTowers = velocitySignalTowers;
    }

    public ArrayList<SignalTower> getBestSignalTowers() {
        return bestSignalTowers;
    }

    public void setBestSignalTowers(ArrayList<SignalTower> bestSignalTowers) {
        this.bestSignalTowers = bestSignalTowers;
    }
}
