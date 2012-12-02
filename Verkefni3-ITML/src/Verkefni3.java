import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
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
    public String filename;
    public int noAgents;
    public int noTasks;
    public static Population pop;
    public int generations;

    public int popSize;
    public int selectChance;
    public int elite;
    public int mutateChance;

    public Selection selection;
    public Crossover crossover;
    public Mutation mutate;


    public ArrayList<Integer> dataBuffer;
    public ArrayList<Integer> constraints;
    public ArrayList<Integer> workload;

    public int [][] resourcesMatrix;
    public int [][] costsMatrix;

    public Verkefni3(){
    }

    public void ParseData(String filename){
        try {
            file = new ArrayList<String>();
            Scanner sc = new Scanner(new File(filename));
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
    }

    public int random(int lower, int upper)
    {
        Random random = new Random();
        int r = random.nextInt(upper);
        if (r < lower)return random(lower, upper);
        else return r;
    }
    public void Init(){
        workload = new ArrayList<Integer>(noAgents);
        pop = new Population(popSize);
        while(pop.GetPopSize() < popSize) {
            Chromasome chrome = new Chromasome(noTasks,noAgents);
            chrome.Evaluate(workload,noAgents,resourcesMatrix,constraints,costsMatrix);
            pop.AddChromasome(chrome);
        }
        selection = new Selection(selectChance);
        crossover = new Crossover();
        mutate = new Mutation(mutateChance);
    }

    public void ReadConfig(){
        Properties prop = new Properties();

        try {
            //load a properties file
            prop.load(new FileInputStream("config.properties"));

            //get the property value and print it out
           this.popSize =  Integer.parseInt(prop.getProperty("popSize"));
           this.selectChance = Integer.parseInt(prop.getProperty("selectChance"));
           this.elite = Integer.parseInt(prop.getProperty("elite"));
           this.mutateChance = Integer.parseInt(prop.getProperty("mutateChance"));
           this.filename = prop.getProperty("filename");
           this.generations = Integer.parseInt(prop.getProperty("generations"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {

        Verkefni3 v3 = new Verkefni3();
        v3.ReadConfig();
        v3.ParseData(v3.filename);
        v3.Init();
        int bestFitness = Integer.MAX_VALUE;

        for(int k = 0 ; k < 50000; k++){
            for(int j = 0 ; j < pop.population.size();j++){
                //Set the fitness for each chromasone
                pop.population.get(j).SetFitness(v3.costsMatrix,v3.constraints);
            }
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

                v3.crossover.crossover(parent1, parent2, child1, child2);
                v3.mutate.mutate(child1,v3.noAgents);
                v3.mutate.mutate(child2,v3.noAgents);
                child1.Evaluate(v3.workload,v3.noAgents,v3.resourcesMatrix,v3.constraints, v3.costsMatrix);
                child2.Evaluate(v3.workload,v3.noAgents,v3.resourcesMatrix,v3.constraints, v3.costsMatrix);

                if(!child1.feasable) {
                    child1.Repair(v3.noAgents,v3.resourcesMatrix,v3.constraints, v3.costsMatrix);
                }
                if(!child2.feasable) {
                    child2.Repair(v3.noAgents,v3.resourcesMatrix,v3.constraints, v3.costsMatrix);
                }

                if(child1.compareTo(child2) > 0) {
                    newPop.Add(child1);
                }
                else {
                    newPop.Add(child2);
                }
            }
            pop = newPop;

            if(bestFitness > pop.population.get(0).fitnessValue || k == 1 ||  k == 49999){
            System.out.println("Generation :"+ k);
            System.out.println("Best :");
            System.out.println(pop.population.get(0));
            System.out.println("Fitness :" + pop.population.get(0).fitnessValue);
            pop.population.get(0).Evaluate(v3.workload, v3.noAgents, v3.resourcesMatrix, v3.constraints, v3.costsMatrix);

            if(pop.population.get(0).feasable){  System.out.println("Feasable : True" );  }
            else{  System.out.println("Feasable : False" ); }

            System.out.println();
            System.out.print("Resource Usage :");
            for(int p = 0 ; p < pop.population.get(0).reasourceUsage.size() ;p++){
                System.out.print(pop.population.get(0).reasourceUsage.get(p)+" ");
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------------" );
            bestFitness =  pop.population.get(0).fitnessValue;
            }
        }
    }
}
