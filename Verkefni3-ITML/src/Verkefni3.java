import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public static void main(String[] args) {

        Verkefni3 v3 = new Verkefni3();

        ArrayList<Integer> sum = v3.Sum(v3.costsMatrix, v3.noAgents,v3.noTasks);
        for(Integer i : sum){
            System.out.println("Assignemnt Cost :"+i);
        }










    }
}
