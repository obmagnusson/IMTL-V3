import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Olafur
 * Date: 30.11.2012
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class Mutation {

    private int mutateChance;

    public Mutation(int mutateChance) {
        this.mutateChance = mutateChance;
    }

    public void mutate(Chromasome chrome) {
        Random r = new Random();
        for(int i = 0; i < chrome.size(); i++) {
            if(r.nextInt(1000) <= mutateChance) {
                chrome.set(i, (chrome.get(i)==0)?1:0);
                }
            }
        }
}
