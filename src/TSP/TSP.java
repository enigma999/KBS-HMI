package TSP;

import java.util.ArrayList;

public class TSP {

    public ArrayList<int[]> getBranchBound(ArrayList<int[]> coordList) {
        ArrayList<Pakket> pakkets = coordsToPakkets(coordList);
        pakkets = new BranchAndBound().calculatePath(pakkets);
        coordList = pakketsToCoords(pakkets);
        return coordList;
    }

    private ArrayList<int[]> pakketsToCoords(ArrayList<Pakket> pakkets) {
        ArrayList<int[]> coordList = new ArrayList<>();
        pakkets.forEach(pakket -> {
            int[] coords = new int[2];
            coords[0] = pakket.getX();
            coords[1] = pakket.getY();
            coordList.add(coords);
        });
        return coordList;
    }

    private ArrayList<Pakket> coordsToPakkets(ArrayList<int[]> coordList) {
        ArrayList<Pakket> pakkets = new ArrayList<Pakket>();
        coordList.forEach(coord -> {
            Pakket pakket = new Pakket();
            pakket.setX(coord[0]);
            pakket.setY(coord[1]);
            pakkets.add(pakket);
        });
        return pakkets;
    }
}
