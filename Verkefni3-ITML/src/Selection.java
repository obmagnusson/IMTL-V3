import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Olafur
 * Date: 29.11.2012
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */

public class Selection{
    private Population pop;
    private Random r = new Random(System.currentTimeMillis());
    private int selectChance;

    public Selection(int selectChance) {
        this.selectChance = selectChance;
    }

    public void setPopulation(Population pop) {
        this.pop = pop;
    }

    //select the best Chromasome from the population or any Chromosom with chance 0.1%
    public Chromasome selectParent() {
        Chromasome best = pop.population.get(0);
        for(Chromasome c : pop.population) {
            if(r.nextInt(100) <= selectChance) {
                return c;
            }
        }
        return best;  //To change body of implemented methods use File | Settings | File Templates.
    }
}