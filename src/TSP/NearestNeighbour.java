package TSP;

import java.util.ArrayList;

public class NearestNeighbour implements Algoritme {
    private static int startX = 0;
    private static int startY = 0;

    public ArrayList<Pakket> calculatePath(ArrayList<Pakket> pakkets) {
        double afstand = 0;
        ArrayList<Pakket> pakketjes = (ArrayList<Pakket>) pakkets.clone();
        ArrayList<Pakket> path = new ArrayList<>();
        int length = pakketjes.size();
        for (int i = 0; i < length; i++) {
            Pakket pakket = getClosest(pakketjes);
            path.add(pakket);
            pakketjes.remove(pakket);
            afstand += getAfstand(pakket.getX(), pakket.getY());

            startX = pakket.getX();
            startY = pakket.getY();
        }
        return path;
    }


    public String getNaam() {
        return "Nearest Neighbour";
    }


        private static Pakket getClosest (ArrayList < Pakket > pakketjes) {
            double afstand = 1000000000;
            Pakket closestPacket = pakketjes.get(0);

            for (int i = 0; i < pakketjes.size(); i++) {

                int x = pakketjes.get(i).getX();
                int y = pakketjes.get(i).getY();

                if (getAfstand(x, y) < afstand) {
                    closestPacket = pakketjes.get(i);
                    afstand = getAfstand(x, y);
                }
            }

            return closestPacket;
        }

        private static double getAfstand ( int x, int y){
            int distanceX = Math.abs(x - startX);
            int distanceY = Math.abs(y - startY);
            double afstand = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
            return afstand;
        }

}

