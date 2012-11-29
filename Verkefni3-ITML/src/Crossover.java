import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Olafur
 * Date: 29.11.2012
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
public class Crossover{
    private Random r = new Random(System.currentTimeMillis());

    public void crossover(Chromasome parent1, Chromasome parent2, Chromasome child1, Chromasome child2) {
        int crossOver1 = r.nextInt(parent1.size());
        for(int i = 0; i < parent1.size(); i++) {
            if(i <= crossOver1) {
                child1.Add(parent1.get(i));
                child2.Add(parent2.get(i));
            }
            else {
                child1.Add(parent2.get(i));
                child2.Add(parent1.get(i));
            }
        }
    }
}