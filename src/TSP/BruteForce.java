package TSP;

import java.util.ArrayList;
import java.util.Collections;

public class BruteForce implements Algoritme {

    private static double minDistance = Double.MAX_VALUE;
    private static int[] minPath;

    public ArrayList<Pakket> calculatePath(ArrayList<Pakket> pakketjesOriginal) {
        ArrayList<Pakket> pakketjes = (ArrayList<Pakket>) pakketjesOriginal.clone();
        int n = pakketjes.size();
        int[] path = new int[n];
        boolean[] used = new boolean[n];
        minPath = new int[n];
        generatePermutationsHelper(pakketjes, path, used, 0, n);
        ArrayList<Pakket> sortedPakketjes = new ArrayList<>();
        for (int i = 0; i < minPath.length; i++) {
            sortedPakketjes.add(pakketjes.get(minPath[i]));
        }
        return sortedPakketjes;
    }


    public String getNaam() {
        return "BruteForce";
    }


    private static void generatePermutationsHelper(ArrayList<Pakket> pakketjes, int[] path, boolean[] used, int index, int n) {
        if (index == n) {
            double distance = calculateDistance(pakketjes, path);
            if (distance < minDistance) {
                minDistance = distance;
                System.arraycopy(path, 0, minPath, 0, n);
            }
            return;
        }
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                path[index] = i;
                used[i] = true;
                generatePermutationsHelper(pakketjes, path, used, index + 1, n);
                used[i] = false;
            }
        }
    }

    private static double calculateDistance(ArrayList<Pakket> pakketjes, int[] path) {
        double distance = 0.0;
        for (int i = 0; i < path.length - 1; i++) {
            int index1 = path[i];
            int index2 = path[i+1];
            Pakket pakket1 = pakketjes.get(index1);
            Pakket pakket2 = pakketjes.get(index2);
            double dx = pakket2.getX() - pakket1.getX();
            double dy = pakket2.getY() - pakket1.getY();
            distance += Math.sqrt(dx*dx + dy*dy);
        }
        return distance;
    }

}
