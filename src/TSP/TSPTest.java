package TSP;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class TSPTest {

    public AlgValues execTSP(Magazijn magazijn, Algoritme algoritme) {
        AlgValues algValues = new AlgValues();
        final long startTime = System.nanoTime();
        ArrayList<Pakket> path = algoritme.calculatePath(magazijn.getPakketjes());
        final long endTime = System.nanoTime();
        algValues.setTime(endTime-startTime);
        algValues.setDistance(calculatePathLength(path));
        return algValues;
    }

    private static double calculatePathLength(ArrayList<Pakket> pakketjes) {
        double lengthOfPath = 0;
        int firstPakketX = pakketjes.get(1).getX();
        int firstPakketY = pakketjes.get(1).getX();
        lengthOfPath = sqrt(firstPakketX * firstPakketY);
        for (int i = 0; i < pakketjes.size() - 1; i++) {
            int startX = pakketjes.get(i).x;
            int startY = pakketjes.get(i).y;
            int endX = pakketjes.get(i + 1).x;
            int endY = pakketjes.get(i + 1).y;
            int deltaX = startX + endX;
            int deltaY = startY + endY;
            double deltaDirectline = sqrt(deltaY * deltaY + deltaX * deltaX);
            lengthOfPath = lengthOfPath + deltaDirectline;

        }
        return lengthOfPath;
    }
}