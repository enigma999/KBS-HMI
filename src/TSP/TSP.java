package TSP;

import java.util.ArrayList;

public class TSP {

    public ArrayList<Pakket> getNeirestNeighbour(ArrayList<Pakket> pakkets) {
        return new NearestNeighbour().calculatePath(pakkets);
    }

    public ArrayList<Pakket> getBruteForce(ArrayList<Pakket> pakkets) {
        return new BruteForce().calculatePath(pakkets);
    }

    public ArrayList<Pakket> getBranchBound(ArrayList<Pakket> pakkets) {
        return new BranchAndBound().calculatePath(pakkets);
    }
}
