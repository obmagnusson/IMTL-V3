import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Olafur
 * Date: 28.11.2012
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class Chromasome {

    public ArrayList<Integer> DNAstring;
    public int fitnessValue;

    public Chromasome(int noTasks, int noAgents){
        DNAstring = new ArrayList<Integer>();
        for(int i = 0 ; i < noTasks ; i++ ){
              this.DNAstring.add(this.random(0, noAgents));
        }
    }

    public void Fitness(int [][] costsMatrix){
        for(int i = 0; i < DNAstring.size()  ; i++){
            //Workload
            this.fitnessValue += costsMatrix[DNAstring.get(i)][i] ;
        }
    }

    public int size(){
        return DNAstring.size();
    }

    public int get(int i){
        return DNAstring.get(i);
    }

    public int random(int lower, int upper)
    {
        Random random = new Random();
        int r = random.nextInt(upper);
        if (r < lower)return random(lower, upper);
        else return r;
    }
}
