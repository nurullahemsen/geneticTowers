//import java.time.Instant;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Genetic {
    public static final double MUTATION_PROBABILITY = 0.1;
    public static final int MAX_GENERATION = 500;
//    public static final double CROSSOVER_PROBABILITY = 0.95;
    public static final int GENERATION_SIZE = 250;
    public static final int CUSTOMER_SIZE = 250;
    public static final int TOWER_LOCATIONS_SIZE = 100;
    public static final int TOWERS_SIZE = 50;
    public static final int RADIUS = 30;
    public static final int X = 400;
    public static final int Y = 400;
    public static final Random random = new Random();
    public static final ArrayList<Customer> customers = createConstantSet();
    public static final ArrayList<SignalTower> towerSet = createTowerSet();
    public static final int TRY_NUMBER = 4;
    private static int[] threadCases = new int[TRY_NUMBER];
    private static Chromosome[] bestChromosomes = new Chromosome[TRY_NUMBER];
    private static int maximumBestFitness = 0;


    public static void main(String[] args) {
        Chromosome maximumBest =new Chromosome(customers, towerSet, TOWER_LOCATIONS_SIZE);
        maximumBestFitness = maximumBest.getFitness();
        System.out.println("max best: "+maximumBestFitness);
//        printImage(maximumBest, "C:\\Users\\DankSide\\Desktop\\AllTowers.bmp");
        printImage(maximumBest, "AllTowers.bmp");
//        test();

        runThreads(TRY_NUMBER);
//        computeWithLoop(TRY_NUMBER);


    }

    public static void test(){
        double limited = 0;
        for(int i = 0; i < 1000; i++){
           limited += new Chromosome(customers, createTowerSet(), TOWER_LOCATIONS_SIZE).getFitness();
        }
        double unLimited = 0;
        for(int i = 0; i < 1000; i++){
            unLimited += new Chromosome(customers, createTowerSet(), TOWER_LOCATIONS_SIZE).getFitness();
        }
        System.out.println(unLimited/limited);
    }

    public static void runThreads(int threadNumber){
        Thread[] threads = new Thread[threadNumber];
        for(int i = 0; i< threadNumber; i++){
            Runnable r0 = new GeneticRunnable(i);

            threads[i] = new Thread(r0);
            threads[i].start();
        }
        for (Thread t : threads){
            try {
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        int absouluteBest = 0;
        Chromosome absoluteBestChromosome = null;
        for(int i = 0; i < threadCases.length; i++){
            if(threadCases[i] > absouluteBest){
                absouluteBest = threadCases[i];
                absoluteBestChromosome = bestChromosomes[i];
            }
        }
//        printImage(absoluteBestChromosome,"C:\\Users\\DankSide\\Desktop\\SelectedTowers.bmp" );
        printImage(absoluteBestChromosome,"SelectedTowers.bmp" );

        System.out.println("Absolute best fitness is: "+absouluteBest);
        System.out.println((double)absouluteBest/maximumBestFitness);
    }

    public static void computeThreads(int index){
        bestChromosomes[index] = bestCase();
        threadCases[index] = bestChromosomes[index].getFitness();
//        System.out.println("@Thread"+index+" BestCase: "+threadCases[index]);
    }

    public static void computeWithLoop(int loopNumber){

        Chromosome best = null;
        int bestFintess = 0;
        for(int i=0; i<loopNumber; i++){
            Chromosome c = bestCase();
            int fitness = c.getFitness();
            if(bestFintess< fitness){
                best = c;
                bestFintess = fitness;
            }
        }

        System.out.println("bestFitnes: "+ bestFintess);
    }

    public static Chromosome bestCase(){
        ArrayList<Chromosome> generation = randomChromosomes(GENERATION_SIZE);
        int best = 0;
        Chromosome bestCase = null;
        for(int i = 0; i < MAX_GENERATION; i++){
            ArrayList<Chromosome> newGeneration = newGeneration(generation);
            Chromosome elite = findElite(newGeneration);
            int eliteFitness = elite.getFitness();
            if(eliteFitness> best){
                best = eliteFitness;
                bestCase = elite;
                System.out.println("Generation "+i+"@: "+ eliteFitness);
            }
            newGeneration = sortChromosomes(newGeneration);
            generation = newGeneration;
//            generation = new ArrayList<Chromosome>(newGeneration.subList(0, GENERATION_SIZE));
        }
        return bestCase;
    }

    public static  ArrayList<Customer> createConstantSet(){
//        Customer[] cs = new Customer[CUSTOMER_SIZE];
        ArrayList<Customer> cs = new ArrayList<Customer>(0);
        for (int i = 0; i < CUSTOMER_SIZE; i++) cs.add(new Customer(new Location(X,Y, random), i));
        return cs;
    }

    public static   ArrayList<SignalTower> createTowerSet(){
//        SignalTower[] st = new SignalTower[TOWER_LOCATIONS_SIZE];
        ArrayList<SignalTower> st = new ArrayList<SignalTower>();
        for (int i = 0; i < TOWER_LOCATIONS_SIZE; i++) st.add(new SignalTower(new Location(X,Y, random),RADIUS, i));
        return st;
    }

    static Chromosome selectRandomWeighted(ArrayList<Chromosome> chromosomes, Random rnd) {
        chromosomes = new ArrayList<Chromosome>(chromosomes.subList(0,chromosomes.size()/10));
        int selected = 0;
        int size = chromosomes.size();
        double total = chromosomes.get(0).getFitness();

        for( int i = 1; i < size; i++ ) {
            total += chromosomes.get(i).getFitness();
            if( rnd.nextDouble() <= (chromosomes.get(i).getFitness() / total)) selected = i;
        }

        return chromosomes.get(selected);
    }

    static int naturalSelection(ArrayList<Chromosome> chromosomes, Random rnd) {
        int selected = 0;
        int size = chromosomes.size();
        double total = chromosomes.get(0).getFitness();

        for( int i = 1; i <size; i++ ) {
            total += chromosomes.get(i).getFitness();
            if( rnd.nextDouble() <= (chromosomes.get(i).getFitness() / total)) selected = i;
        }

        return selected;
    }

    static ArrayList<Chromosome> newGeneration(ArrayList<Chromosome> chromosomes){
        chromosomes = sortChromosomes(chromosomes);
        ArrayList<Chromosome> newGeneration = new ArrayList<Chromosome>();
        newGeneration.add(chromosomes.get(0));
        newGeneration.add(chromosomes.get(1));
        Random random = new Random();

        for (int i = 0; i < GENERATION_SIZE -1; i++){
            Chromosome c1 = selectRandomWeighted(chromosomes, random);
            Chromosome c2 = selectRandomWeighted(chromosomes, random);
            Chromosome[] newChromosomes = c1.crossover(c2);
            newGeneration.add(newChromosomes[0].mutate(MUTATION_PROBABILITY));
            newGeneration.add(newChromosomes[1].mutate(MUTATION_PROBABILITY));
        }
//        return newGeneration;
        return selection(newGeneration);

    }

    static  ArrayList<Chromosome> selection(ArrayList<Chromosome> population){
        ArrayList<Chromosome> newGeneration = new ArrayList<Chromosome>();
        population = new ArrayList<Chromosome>(population.subList(0,(int)(population.size()*0.6)));
        Random random = new Random();
        for(int i = 0; i < GENERATION_SIZE; i++){
            int index = naturalSelection(population, random);
            newGeneration.add(population.remove(index));
        }
        return newGeneration;
    }

    static ArrayList<Chromosome> randomChromosomes(int size){
        ArrayList<Chromosome> newList = new ArrayList<Chromosome>();
        for(int i = 0; i < size; i++){
            ArrayList<SignalTower> list = new ArrayList<SignalTower>(towerSet);
            newList.add(new Chromosome(customers, list, TOWERS_SIZE));
        }
        return newList;
    }

    static Chromosome findElite(ArrayList<Chromosome> chromosomes){
        Chromosome chromosome = chromosomes.get(0);
        for(Chromosome c: chromosomes){
            if(c.getFitness() > chromosome.getFitness()) chromosome = c;
        }
        return chromosome;
    }
    static ArrayList<Chromosome> sortChromosomes(ArrayList<Chromosome> chromosomes){
        Collections.sort(chromosomes, new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome o1, Chromosome o2) {
                int result = 0;
                if (o1.getFitness() == o2.getFitness()) result = 0;
                else if (o1.getFitness() > o2.getFitness()) result = -1;
                else if (o1.getFitness() < o2.getFitness()) result = 1;

                return result;
            }
        });
        return chromosomes;
    }

    private static BufferedImage map( int sizeX, int sizeY, Chromosome chromosome ){
        final BufferedImage res = new BufferedImage( sizeX, sizeY, BufferedImage.TYPE_INT_RGB );
        for (int x = 0; x < sizeX; x++){
            for (int y = 0; y < sizeY; y++){
                res.setRGB(x, y, Color.GRAY.getRGB() );
            }
        }
        Graphics2D g = (Graphics2D) res.getGraphics();
        g.setColor(Color.RED);
        g.setBackground(Color.RED);
        for(SignalTower tower : chromosome.getSignalTowers()){
            int x = (int)Math.floor(tower.getLocation().getX());
            int y = (int)Math.floor(tower.getLocation().getY());
            try {
                res.setRGB(x,y,Color.RED.getRGB());
                g.drawOval(x - RADIUS/2, y -RADIUS/2, RADIUS*2, RADIUS*2);
            }catch (Exception e){
//                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println(x);
                System.out.println(y);
            }
        }
        for (Customer customer : customers){
            int x = (int)Math.floor(customer.getLocation().getX());
            int y = (int)Math.floor(customer.getLocation().getY());
            try {
                res.setRGB(x,y,Color.BLUE.getRGB());
            }catch (Exception e){
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

    private static void savePNG( final BufferedImage bi, final String path ){
        try {
            RenderedImage rendImage = bi;
            ImageIO.write(rendImage, "bmp", new File(path));
//            ImageIO.write(rendImage, "PNG", new File(path));
            //ImageIO.write(rendImage, "jpeg", new File(path));
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    private static void printImage(Chromosome chromosome, String location){
        BufferedImage img = map( X, Y , chromosome);
        savePNG( img, location );
    }

}
