package TSP;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Magazijn {
    ArrayList<Pakket> pakketjes = new ArrayList<Pakket>();
    int MaxX;
    int MaxY;

    public Magazijn(int aantalPakketjes, int maxX, int maxY) {

        for(int i=0;i<aantalPakketjes;i++) {
            Pakket pakket=new Pakket();
            pakket.setNumber(i);
            int X = ThreadLocalRandom.current().nextInt(0, maxX + 1);
            int Y = ThreadLocalRandom.current().nextInt(0, maxY + 1);
            pakket.setX(X);
            pakket.setY(Y);
            pakketjes.add(pakket);
        }
        MaxX = maxX;
        MaxY = maxY;
    }

    public ArrayList<Pakket> getPakketjes() {
        return pakketjes;
    }

    @Override
    public String toString() {
        return "magazijn{" +
                "pakketjes=" + pakketjes +
                ", MaxX=" + MaxX +
                ", MaxY=" + MaxY +
                '}';
    }
}
