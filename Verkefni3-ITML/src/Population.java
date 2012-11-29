import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Olafur
 * Date: 28.11.2012
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class Population {

    ArrayList<Chromasome> population;

    public Population(int population){
        this.population = new ArrayList<Chromasome>(population);
    }

    public void Remove(int i){
        this.population.remove(i);
    }

    public void AddChromasome(Chromasome chromasome){
        this.population.add(chromasome);
    }
    public int GetPopSize(){
       return this.population.size();
    }

    public void Add(Chromasome chrome){
        this.population.add(chrome);
    }

    public void Sort(){
       Collections.sort(this.population, new Comparator<Chromasome>() {
            public int compare(Chromasome o1, Chromasome o2) {
                return  Integer.compare(o1.GetFitness(),  o2.GetFitness());
            }
        });

    }

}
