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
    public ArrayList<Integer> reasourceUsage;
    public int fitnessValue;
    public boolean feasable;

    public Chromasome(int noTasks, int noAgents){
        DNAstring = new ArrayList<Integer>();
        for(int i = 0 ; i < noTasks ; i++ ){
            this.DNAstring.add(this.random(0, noAgents));
        }
        this.fitnessValue = 0;
        this.feasable = true;

    }

    public Chromasome (int size){
        DNAstring = new ArrayList<Integer>(size);
        this.fitnessValue = 0;
        this.feasable = true;
    }
    public void SetFitness(int[][] costsMatrix, ArrayList<Integer> constraints){
        int temp = 0;
        for(int i = 0; i < DNAstring.size()  ; i++){
            //Workload
            temp += costsMatrix[DNAstring.get(i)][i] ;
        }
        this.fitnessValue = temp;

        int penalty = 0;
        for(int d = 0; d < this.reasourceUsage.size(); d++){
            if(this.reasourceUsage.get(d) > constraints.get(d)){
                penalty += this.reasourceUsage.get(d) - constraints.get(d);
            }
        }
        fitnessValue = fitnessValue + penalty*2;

    }

    public void Evaluate(ArrayList<Integer> workLoad, int noAgents,int [][] resourcesMatrix, ArrayList<Integer> contstraints, int [][] costsMatrix){

        int temp = 0;
        for(int i = 0; i < DNAstring.size()  ; i++){
            //Workload
            temp += costsMatrix[DNAstring.get(i)][i] ;
        }
        this.fitnessValue = temp;

        for(int f = 0 ; f < noAgents; f++){
            workLoad.add(0);
        }

        for(int m = 0; m < size() ; m++){
            for(int s = 0 ; s < noAgents ; s++){
                if(this.DNAstring.get(m) == s){
                    workLoad.set(s, (resourcesMatrix[this.DNAstring.get(m)][m]+ workLoad.get(s)));
                    //totalConstraints = totalConstraints + v3.resourcesMatrix[pop.population.get(i).get(m)][m];
                    // System.out.print(resourcesMatrix[this.DNAstring.get(m)][m] + " ");
                }
            }
        }
        // System.out.println();

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
                //  System.out.println("Workload :" + workLoad.get(x)+ "----------- Contsraints:" + contstraints.get(x));
            }
        }
        else
        {
            int penalty = 0;
            for(int d = 0; d < workLoad.size(); d++){
                if(workLoad.get(d) > contstraints.get(d)){
                    penalty += workLoad.get(d) - contstraints.get(d);
                }
            }
            fitnessValue = fitnessValue + penalty*10;
        }
        reasourceUsage = new ArrayList<Integer>(workLoad.size());
        for(int i = 0; i < workLoad.size() ; i++){
            reasourceUsage.add(workLoad.get(i));
        }
        workLoad.clear();
    }

    public void Repair( int noAgents,int [][] resourcesMatrix, ArrayList<Integer> constraints, int [][] costsMatrix){

        ArrayList<Integer> agentsToFlip = new ArrayList<Integer>();
        if(!this.feasable)
        {
            for(int i = 0; i < constraints.size(); i++){
                //find overcapacity agents and register them in our list
                if(0 < (constraints.get(i) - reasourceUsage.get(i))){
                    agentsToFlip.add(i);
                }
            }
            for(int j = 0; j < agentsToFlip.size() ;j++){
                for(int i = 0; i < DNAstring.size() ;i++){
                    if(agentsToFlip.get(j).equals(DNAstring.get(i))){
                          this.DNAstring.set(i, (i+1)%noAgents);
                    }
                }
            }

            SetFitness(costsMatrix, constraints);
            setReasourceUsage(resourcesMatrix, noAgents);

        }
    }

    public void setReasourceUsage(int resourcesMatrix[][], int noAgents){
       // this.reasourceUsage = new ArrayList<Integer>(reasourceUsage.size());
        for(int m = 0; m < size() ; m++){
            for(int s = 0 ; s < noAgents ; s++){
                if(this.DNAstring.get(m) == s){
                   this.reasourceUsage.set(s, (resourcesMatrix[this.DNAstring.get(m)][m]+ this.reasourceUsage.get(s)));
                    //totalConstraints = totalConstraints + v3.resourcesMatrix[pop.population.get(i).get(m)][m];
                    // System.out.print(resourcesMatrix[this.DNAstring.get(m)][m] + " ");
                }
            }
        }
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

    public int compareTo(Object o) {
        return ((Chromasome)o).GetFitness() - this.fitnessValue;
    }

    public void set(int agent, int newAgent)
    {
        DNAstring.set(agent, newAgent);
    }

    public int random(int lower, int upper)
    {
        Random random = new Random();
        int r = random.nextInt(upper);
        if (r < lower)return random(lower, upper);
        else return r;
    }

    public String toString(){
        StringBuilder dnaToString = new StringBuilder();
        for(Integer i : DNAstring) {

            dnaToString.append(i);
            dnaToString.append(",");
        }
        dnaToString.append("Fittness :" + this.fitnessValue);
        return dnaToString.toString();
    }
}
