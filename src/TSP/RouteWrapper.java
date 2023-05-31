package TSP;

import java.util.ArrayList;

public class RouteWrapper {
    private int[] locations;
    private double length;
    private ArrayList<Pakket> pakketjes;
    private boolean isComplete;

    public RouteWrapper(int[] locations, ArrayList<Pakket> pakketjes) {
        this.locations = locations;
        this.pakketjes = pakketjes;
        this.length = calculateRouteLength(locations, pakketjes);
        checkIsComplete(this.locations);
    }

    public int[] getLocations() {
        return locations;
    }

    public double getLength() {
        return length;
    }

    /**
     * Calculates the length of a route represented by an array of integers representing the indices
     * of Pakket objects in an ArrayList.
     * Points with an index of 0 in the route are skipped.
     *
     * @param route     An array of integers representing the indices of Pakket objects in an ArrayList.
     * @param pakketjes An ArrayList of Pakket objects.
     * @return The total length of the route.
     */
    public static double calculateRouteLength(int[] route, ArrayList<Pakket> pakketjes) {
        double length = 0;

        // Calculate distance from (0, 0) to the first point in the route
        int firstIndex = route[0] - 1;
        int x0 = 0;
        int y0 = 0;
        int x1 = pakketjes.get(firstIndex).getX();
        int y1 = pakketjes.get(firstIndex).getY();
        length += Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));

        for (int i = 0; i < route.length - 1; i++) {
            // Skip points with index 0
            if (route[i] != 0 && route[i + 1] != 0) {
                int index1 = route[i] - 1;
                int index2 = route[i + 1] - 1;

                int x2 = pakketjes.get(index1).getX();
                int y2 = pakketjes.get(index1).getY();
                int x3 = pakketjes.get(index2).getX();
                int y3 = pakketjes.get(index2).getY();

                length += Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
            }
        }

        return length;
    }

    public void checkIsComplete(int[] locations) {
        isComplete = true;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] == 0) {
                isComplete = false;
                break;
            }
        }
    }


}
