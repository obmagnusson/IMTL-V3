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
    public boolean feasable;

    public Chromasome(int noTasks, int noAgents){
        DNAstring = new ArrayList<Integer>();
        for(int i = 0 ; i < noTasks ; i++ ){
            this.DNAstring.add(this.random(0, noAgents));
        }
        this.feasable = true;
    }

    public void SetFitness(int[][] costsMatrix){
        for(int i = 0; i < DNAstring.size()  ; i++){
            //Workload
            this.fitnessValue += costsMatrix[DNAstring.get(i)][i] ;
        }
    }

    public void Evaluate(ArrayList<Integer> workLoad, int noAgents,int [][] resourcesMatrix, ArrayList<Integer> contstraints){
        for(int f = 0 ; f < noAgents; f++){
            workLoad.add(0);
        }

        for(int m = 0; m < size() ; m++){
            for(int s = 0 ; s < noAgents ; s++){
                if(this.DNAstring.get(m) == s){
                    workLoad.set(s, (resourcesMatrix[this.DNAstring.get(m)][m]+ workLoad.get(s)));
                    //totalConstraints = totalConstraints + v3.resourcesMatrix[pop.population.get(i).get(m)][m];
                    System.out.print(resourcesMatrix[this.DNAstring.get(m)][m] + " ");
                }
            }
        }
        System.out.println();

        for(int x = 0 ; x < noAgents ; x++){
            // Estimate if the workload exceeds the contraints given.
            //System.out.println("Workload :" + workLoad.get(x));
            if(workLoad.get(x) >= contstraints.get(x)){
                this.feasable = false;
                break;
            }
        }

        if(this.feasable){


            for(int x = 0 ; x < noAgents ; x++){
                // Estimate if the workload exceeds the contraints given.
                System.out.println("Workload :" + workLoad.get(x)+ "----------- Contsraints:" + contstraints.get(x));

            }
            System.out.println("This DNA is feasable");}
        workLoad.clear();
    }

    public int GetFitness() {
        return this.fitnessValue;
    }
    public void Add(int n){
        this.DNAstring.add(n);
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
