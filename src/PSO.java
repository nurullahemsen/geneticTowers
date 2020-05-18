import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PSO {


    public static final int CUSTOMER_SIZE = 300;
    public static final int TOWER_LOCATIONS_SIZE = 50;
    public static final int RADIUS = 35;
    public static final int X = 500;
    public static final int Y = 500;
    public static final Random random = new Random();
    public static final ArrayList<Customer> customers = createConstantSet();
    public static final double c1 = 2.5;
    public static final double c2 = 1.5;
    public static final double w = 0.7;
    public static double r1;
    public static double r2;
    public static Particle bestCase;
    public static final ArrayList<Particle> particles = randomParticles(200);


    public static void main(String[] args) {

        allCustomers(customers);
        bestCase = new Particle(bestParticle(particles));
        printImage(bestCase, "PSOInitialBest.bmp");
        int i = 0;
        while (i < 500)
        {
            for (Particle p : particles) {
                velocityChange(p);
            }
            if(i%50==0 || i==499) {
                System.out.println(i + ".iteration  " + bestCase.computeFitness());
            }
            i++;
        }
        printImage(bestCase, "PSOFinalBest.bmp");
        bestSolution(bestCase);

    }



    public static ArrayList<Customer> createConstantSet() {
//        Customer[] cs = new Customer[CUSTOMER_SIZE];
        ArrayList<Customer> cs = new ArrayList<Customer>(0);
        for (int i = 0; i < CUSTOMER_SIZE; i++) cs.add(new Customer(new Location(X, Y, random), i));
        return cs;
    }

    public static ArrayList<SignalTower> createTowerSet() {
//        SignalTower[] st = new SignalTower[TOWER_LOCATIONS_SIZE];
        ArrayList<SignalTower> st = new ArrayList<SignalTower>();
        for (int i = 0; i < TOWER_LOCATIONS_SIZE; i++) st.add(new SignalTower(new Location(X, Y, random), RADIUS, i));
        return st;
    }

    static ArrayList<Particle> randomParticles(int size) {
        ArrayList<Particle> newList = new ArrayList<Particle>();
        for (int i = 0; i < size; i++) {
            ArrayList<SignalTower> list = createTowerSet();
            newList.add(new Particle(customers, list));
        }
        return newList;
    }

    static void velocityChange(Particle p) {
        r1 = random.nextDouble();
        r2 = random.nextDouble();

        for (int i = 0; i < p.getVelocitySignalTowers().size(); i++) {
            p.getVelocitySignalTowers().get(i).getLocation().setX((w * (p.getVelocitySignalTowers().get(i).getLocation().getX())) +
                    (c1 * r1 * (p.getBestSignalTowers().get(i).getLocation().getX() - p.getSignalTowers().get(i).getLocation().getX())) +
                    (c2 * r2 * (bestCase.getSignalTowers().get(i).getLocation().getX() - p.getSignalTowers().get(i).getLocation().getX())));

            p.getVelocitySignalTowers().get(i).getLocation().setY((w * (p.getVelocitySignalTowers().get(i).getLocation().getY())) +
                    (c1 * r1 * (p.getBestSignalTowers().get(i).getLocation().getY() - p.getSignalTowers().get(i).getLocation().getY())) +
                    (c2 * r2 * (bestCase.getSignalTowers().get(i).getLocation().getY() - p.getSignalTowers().get(i).getLocation().getY())));

            if (p.getSignalTowers().get(i).getLocation().getX() + p.getVelocitySignalTowers().get(i).getLocation().getX() < 0) {
                p.getSignalTowers().get(i).getLocation().setX(0);
            } else if (p.getSignalTowers().get(i).getLocation().getX() + p.getVelocitySignalTowers().get(i).getLocation().getX() > X) {
                p.getSignalTowers().get(i).getLocation().setX(400);
            } else {
                p.getSignalTowers().get(i).getLocation().setX(p.getSignalTowers().get(i).getLocation().getX() + p.getVelocitySignalTowers().get(i).getLocation().getX());
            }
            if (p.getSignalTowers().get(i).getLocation().getY() + p.getVelocitySignalTowers().get(i).getLocation().getY() < 0) {
                p.getSignalTowers().get(i).getLocation().setY(0);
            } else if (p.getSignalTowers().get(i).getLocation().getY() + p.getVelocitySignalTowers().get(i).getLocation().getY() > Y) {
                p.getSignalTowers().get(i).getLocation().setY(400);
            } else {
                p.getSignalTowers().get(i).getLocation().setY(p.getSignalTowers().get(i).getLocation().getY() + p.getVelocitySignalTowers().get(i).getLocation().getY());
            }


        }


        if (p.computeFitness() > p.computeBestFitness()) {
            for (int i = 0; i < p.getSignalTowers().size(); i++) {
                p.getBestSignalTowers().get(i).getLocation().setX(p.getSignalTowers().get(i).getLocation().getX());
                p.getBestSignalTowers().get(i).getLocation().setY(p.getSignalTowers().get(i).getLocation().getY());
            }
        }

        if (p.computeFitness() > bestCase.computeFitness()) {
            bestCase = new Particle(p);


        }

    }


    static Particle bestParticle(ArrayList<Particle> particles) {

        Particle b = particles.get(0);
        for (int i = 1; i < particles.size(); i++) {

            if (particles.get(i).computeFitness() > b.computeFitness()) {
                b = particles.get(i);
            }
        }
        return b;
    }


    static void bestSolution(Particle p) {

        System.out.println("Best locations of stations");

        for (int i = 1; i < p.getSignalTowers().size(); i++) {

            System.out.println(p.getSignalTowers().get(i).toString());
        }

        System.out.println("Best Fitness: "+ p.computeFitness());

    }

    static void allCustomers(ArrayList<Customer> customers) {

        System.out.println("All customer locations");

        for (int i = 1; i < customers.size(); i++) {

            System.out.println(customers.get(i).toString());
        }

    }


    private static BufferedImage map(int sizeX, int sizeY, Particle chromosome) {
        final BufferedImage res = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                res.setRGB(x, y, Color.GRAY.getRGB());
            }
        }
        Graphics2D g = (Graphics2D) res.getGraphics();
        g.setColor(Color.RED);
        g.setBackground(Color.RED);
        for (SignalTower tower : chromosome.getSignalTowers()) {
            int x = (int) Math.floor(tower.getLocation().getX());
            int y = (int) Math.floor(tower.getLocation().getY());
            try {
                res.setRGB(x, y, Color.RED.getRGB());
                g.drawOval(x - RADIUS / 2, y - RADIUS / 2, RADIUS * 2, RADIUS * 2);
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println(x);
                System.out.println(y);
            }
        }g.setColor(Color.BLUE);
        g.setBackground(Color.BLUE);
        for (Customer customer : customers) {
            int x = (int) Math.floor(customer.getLocation().getX());
            int y = (int) Math.floor(customer.getLocation().getY());
            try {
//                res.setRGB(x, y, Color.BLUE.getRGB());
                g.drawOval(x - 1, y -1, 2, 2);
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println(x);
                System.out.println(y);
            }

        }
//        Graphics2D g = (Graphics2D) res.getGraphics();
//        g.setColor(Color.RED);
//        g.setBackground(Color.RED);
//        g.drawOval(30,30,15,15);
        return res;
    }

    private static void savePNG(final BufferedImage bi, final String path) {
        try {
            RenderedImage rendImage = bi;
            ImageIO.write(rendImage, "bmp", new File(path));
//            ImageIO.write(rendImage, "PNG", new File(path));
            //ImageIO.write(rendImage, "jpeg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printImage(Particle chromosome, String location) {
        BufferedImage img = map(X, Y, chromosome);
        savePNG(img, location);
    }

}



