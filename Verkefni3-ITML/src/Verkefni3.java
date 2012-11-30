import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Olafur
 * Date: 20.11.2012
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public class Verkefni3 {
    public ArrayList file;
    public int noAgents;
    public int noTasks;
    public static Population pop;

    public int popSize = 100;
    public int selectChance = 20;
    public int elite = 25;
    public int mutateChance = 5;

    public Selection selection;
    public Crossover crossover;
    public Mutation mutate;


    public ArrayList<Integer> dataBuffer;
    public ArrayList<Integer> constraints;

    public int [][] resourcesMatrix;
    public int [][] costsMatrix;

    public Verkefni3(){

        try {
            file = new ArrayList<String>();
            Scanner sc = new Scanner(new File("D3-15.dat"));
            while (sc.hasNextLine())    {
                file.add(sc.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        dataBuffer =  new ArrayList<Integer>();
        String [] integers = null;
        String data = file.toString();
        String[] lines = data.split(",");

        for(String line : lines){
            if(line.startsWith("[;;") ){continue;}
            integers = line.split(" ");

            for ( String integer : integers){
                if(integer.isEmpty()){continue;}
                if(integer.startsWith("[")){continue;}
                if(integer.startsWith("]")){continue;}

                dataBuffer.add((Integer.parseInt(integer)));
            }
        }
        // System.out.println( integers[11] );
        noAgents = dataBuffer.get(0);
        noTasks = dataBuffer.get(1);
        constraints = new ArrayList<Integer>();
        for (int c = 3; c < noAgents+3 ; c++){
            constraints.add(dataBuffer.get(c));
        }
        for(Integer i : constraints){
            System.out.println("constraints :"+i);
        }

        System.out.println("No Agents :"+noAgents);
        System.out.println("No Tasks  :"+noTasks);

        resourcesMatrix = new int[noAgents][noTasks];
        costsMatrix = new int[noAgents][noTasks];

        int resourceCount = noAgents+3 ;
        for(int n = 0; n < noAgents ;n++){
            for(int m = 0; m < noTasks ;m++){
                resourcesMatrix[n][m] = dataBuffer.get(resourceCount);
                resourceCount++;
            }
        }
        for(int n = 0; n < noAgents ;n++){
            System.out.println();
            for(int m = 0; m < noTasks ;m++){
                System.out.print(resourcesMatrix[n][m] + " ");
            }
        }
        System.out.println( );
        System.out.println("---------------------------------------------" );
        int costCount = resourceCount ;
        for(int n = 0; n < noAgents ;n++){
            for(int m = 0; m < noTasks ;m++){
                costsMatrix[n][m] = dataBuffer.get(costCount);
                costCount++;
            }
        }
        for(int n = 0; n < noAgents ;n++){
            System.out.println();
            for(int m = 0; m < noTasks ;m++){
                System.out.print(costsMatrix[n][m] + " ");
            }
        }
        System.out.println();
    }

    public int random(int lower, int upper)
    {
        Random random = new Random();
        int r = random.nextInt(upper);
        if (r < lower)return random(lower, upper);
        else return r;
    }
    public ArrayList<Integer> Sum(int[][]  matrix, int noAgents, int noTasks){
        int sum = 0 ;
        ArrayList<Integer> sumMatrix = new ArrayList<Integer>();
        for(int i = 0 ; i < noAgents ; i++){
            for( int k = 0; k < noTasks; k++){
                // System.out.println("Cost matrix : "+ matrix[i][k] );
                sum += matrix[i][k];
            }
            sumMatrix.add(sum);
            sum = 0;
        }
        return sumMatrix;
    }

    public void Init(){
        pop = new Population(popSize);
        for(int i = 0; i < popSize; i++) {
            Chromasome chrome = new Chromasome(noTasks,noAgents);
            // chr.randomize(problem);
            // chr.evaluate(problem);
            // chr.Repair(problem);
            // chr.evaluate(problem);
            // updateMetrics(chr);
            pop.AddChromasome(chrome);
        }
         selection = new Selection(selectChance);
         crossover = new Crossover();
         mutate = new Mutation(mutateChance);
    }


    public static void main(String[] args) {

        Verkefni3 v3 = new Verkefni3();

        v3.Init();

        System.out.println( );
        for(int k = 0 ; k < 5; k++){
            System.out.println("Generation :"+ k);
        System.out.println("Genotypes : ");
        for(int j = 0 ; j < pop.population.size();j++){
            //Set the fitness for each chromasone
            pop.population.get(j).SetFitness(v3.costsMatrix);
        }
        System.out.println("-------------------------------------------------------------------" );
        pop.Sort();
        //Print out current population
        for(int j = 0 ; j < pop.population.size();j++){
            for(int i = 0 ; i < pop.population.get(j).size(); i++){

                System.out.print( pop.population.get(j).get(i)+"," );
            }
            System.out.println("----Fitness:, "+pop.population.get(j).fitnessValue);
        }
        // Assignment : 2 ,2 ,1 ,1 ,0 ,1 ,1 ,1 ,2 ,1 ,2 ,1 ,0 ,0 ,1
        // 64 38 26 36 91 39 91 97 44 61 15 63 57 50 56
        // 25 28 27 54 58 68 20 96 75 53 49 33 73 47 43
        // 77 50 66 21 16 38 8 39 89 95 70 104 45 41 53


           pop.Sort();
             Population newPop = new Population(v3.popSize);
             v3.selection.setPopulation(pop);
             int theElite = pop.population.size() * v3.elite / 100;
             for(int i = 0; i < theElite; i++) {
                 newPop.Add(pop.population.get(i));
             }
             while(newPop.population.size() < v3.popSize) {

                 Chromasome parent1 = v3.selection.selectParent();
                 Chromasome parent2 = v3.selection.selectParent();
                 Chromasome child1 = new Chromasome(parent1.size());
                 Chromasome child2 = new Chromasome(parent1.size());
                 child1.SetFitness(v3.costsMatrix);
                 child2.SetFitness(v3.costsMatrix);
                 v3.crossover.crossover(parent1, parent2, child1, child2);
                 v3.mutate.mutate(child1);
                 v3.mutate.mutate(child2);
                // child1.Evaluate();//evaluate(problem);
                // child2.Evaluate();//evaluate(problem);
                // if(child1.Repair(problem)) {
                //     child1.evaluate(problem);
                // }
                // if(child2.Repair(problem)) {
               //      child2.evaluate(problem);
                // }
                 if(child1.compareTo(child2) < 0) {
                     newPop.Add(child1);
                   //  updateMetrics(child1);
                 }
                 else {
                     newPop.Add(child2);
                   //  updateMetrics(child2);

                 }
             }
             pop = newPop;

           ArrayList<Integer> workLoad = new ArrayList<Integer>();
           System.out.println("Best :");
           System.out.println(pop.population.get(0));
           System.out.println("Fitness :" + pop.population.get(0).fitnessValue);
           pop.population.get(0).Evaluate(workLoad, v3.noAgents, v3.resourcesMatrix, v3.constraints);
           if(pop.population.get(0).feasable){
           System.out.println("Feasable : True" );
           }
           else{
           System.out.println("Feasable : False" );
           }
            System.out.println("-------------------------------------------------------------------" );
         }

        ArrayList<Integer> workLoad = new ArrayList<Integer>();

        System.out.println();

 /*       for(int i = 0 ; i < pop.GetPopSize(); i++){

            System.out.println("Population nr:" +i);
            pop.population.get(i).Evaluate(workLoad ,v3.noAgents,v3.resourcesMatrix,v3.constraints);

        }*/
    }
}
