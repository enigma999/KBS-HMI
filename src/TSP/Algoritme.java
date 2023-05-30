package TSP;

import java.util.ArrayList;

public interface Algoritme {
    ArrayList<Pakket> calculatePath(ArrayList<Pakket> pakketjesOriginal);

    String getNaam();
}
