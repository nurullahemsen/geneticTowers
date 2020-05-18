import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Tüm genetik algoritmanın temelinde bulunan cross over ve mutation a tabi tutulan fitness değeri bulunan bir
 * kromozom sınıfı.
 */

public class Chromosome {
    private int fitness;

    private ArrayList<SignalTower> signalTowers;
    private ArrayList<SignalTower> unusedSignalTowers;
    private ArrayList<Customer> customers;

    /**
     * Parametrelerde aldığı değişkenlere göre random bir baz istasyonu listesi oluştutu ve bu liste ile birlikte
     * müşterilerin kapsanma sayısına göre hemen bir fitness değeri belirler. Ayrıca kullanılmamış baz istasyonları da
     * ayrı biir alanda tutulur.
     *
     * @param customers fitess değeri belirlenebilmesi için o anki müşteri listesi
     * @param signalTowers imkan dahilindeki tüm baz istasyonlarının listesi
     * @param actualNumber baz istasyonları içinden kaç tanesinin kromozomlarda tutulacağı
     */

    public Chromosome(ArrayList<Customer> customers, ArrayList<SignalTower> signalTowers, int actualNumber){
        Collections.shuffle(signalTowers);
        this.signalTowers = new ArrayList<SignalTower>();
        for(int i = 0; i< actualNumber; i++)this.signalTowers.add(signalTowers.get(i));

        this.unusedSignalTowers = new ArrayList<SignalTower>();
        for(int i = actualNumber; i< signalTowers.size(); i++)this.unusedSignalTowers.add(signalTowers.get(i));
        this.customers = customers;
        this.fitness = this.computeFitness();
    }

    public Chromosome(ArrayList<Customer> customers, ArrayList<SignalTower> selectedSignalTowers){
        this.customers = customers;
        this.signalTowers = new ArrayList<SignalTower>(selectedSignalTowers);
        ArrayList<SignalTower> tempList = new ArrayList<SignalTower>(Genetic.towerSet);
        tempList.removeAll(this.signalTowers);
        this.unusedSignalTowers = tempList;
        this.fitness = this.computeFitness();
    }

    /**
     * Verilen probability e göre bir kromozomdaki random bir baz istasyonu kullanılmayan baz istasyonları listesinden
     * random biriyle değiştirilir.
     *
     * @param probability mutasyon ihtimal değeri
     * @return mutasyona uğramış veya uğramamış kromozomu döndürür.
     */

    public Chromosome mutate(double probability){
        Random random =  new Random();
        try {
            if(random.nextDouble() < probability){
                if(this.unusedSignalTowers.size() == 0){
                    System.out.println("hello");
                }
                int indexToMutate = random.nextInt(this.signalTowers.size());
                int indexToMutateFrom = random.nextInt(this.unusedSignalTowers.size());
                SignalTower element = unusedSignalTowers.get(indexToMutateFrom);
                //Swap elements from lists
                unusedSignalTowers.set(indexToMutateFrom,signalTowers.set(indexToMutate,element));
            }
        }catch (Exception e){
            System.out.println(this.unusedSignalTowers.size());
            System.out.println(e.getMessage());
        }

        return this;
    }

    /**
     * Cross over da eşlenecek kromozomların baz istason listeleri eleman tekrarı olmayacak bir şekilde birleştirilir,
     * bu liste karıştırılır ve kromozom liste uzunluğu kadar ilk eleman alınır.
     *
     * Baz istasyonlarının listedeki konumları sonuca etki etmeyeceğinden böyle bir eşeyleme yöntemi uygulanmıştır.
     *
     * @param anotherChromosome eşeylenecek diğer kromozom objesi
     * @return ebeveyn kromozmların genlerini paylaşan birbirinden farklı iki adet yeni kromozom
     */

    public Chromosome[] crossover(Chromosome anotherChromosome){
        Chromosome[] products = new Chromosome[2];
        ArrayList<SignalTower> newList = new ArrayList<SignalTower>(this.signalTowers);
        for(SignalTower st : anotherChromosome.getSignalTowers()){
            if (!newList.contains(st)) newList.add(st);
        }
//        newList = sortTowerList(newList);
        Collections.shuffle(newList);

        ArrayList list0 = new ArrayList<SignalTower>(newList.subList(0,this.signalTowers.size()));

        products[0] = new Chromosome(this.customers, list0);

        Collections.shuffle(newList);

        ArrayList list1 = new ArrayList<SignalTower>(newList.subList(0,this.signalTowers.size()));

        products[1] = new Chromosome(this.customers, list1);

        return products;

    }

    /**
     * Bir koromozomun oluşturulmasıyla birlikte toplam kapsanan müşteri sayısının bulan fonksiyon. Bir müşteri bir kez
     * kapsanınca başka baz istasyonları ile bir defa daha kapsansa dahi artık kapsananalar sayısı arttırılmaz.
     *
     * @return kapsanan müşteri sayısı
     */

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

    public static ArrayList<SignalTower> sortTowerList(ArrayList<SignalTower> towers){
        Collections.sort(towers, new Comparator<SignalTower>() {
            @Override
            public int compare(SignalTower o1, SignalTower o2) {
                return o1.compareTo(o2);
            }
        });
        return towers;
    }


    public ArrayList<SignalTower> getSignalTowers() {
        return signalTowers;
    }

    public ArrayList<SignalTower> getUnusedSignalTowers() {
        return unusedSignalTowers;
    }

    public int getFitness() {

        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
