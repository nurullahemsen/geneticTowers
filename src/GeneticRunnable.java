public class GeneticRunnable implements Runnable {
    private int index;

    public GeneticRunnable(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        Genetic.computeThreads(this.index);
    }
}
