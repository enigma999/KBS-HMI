package BPP;
import java.util.ArrayList;

public class BPP {
    private final int bincapaciteit = 10;

    public ArrayList<ArrayList<Integer>> bestFit(int[] gewicht, int[] stockitems) {
        int aantalItems = gewicht.length;
        int aantalBakken = 0;
        int[] resterendeRuimte = new int[aantalItems];
        ArrayList<ArrayList<Integer>> binInhoud = new ArrayList<>();

        for (int i = 0; i < aantalItems; i++) {
            int besteBakIndex = vindBesteBak(resterendeRuimte, gewicht[i], aantalBakken);
            if (besteBakIndex == -1) {
                resterendeRuimte[aantalBakken] = bincapaciteit - gewicht[i];
                aantalBakken++;
                ArrayList<Integer> nieuwBak = new ArrayList<>();
                nieuwBak.add(stockitems[i]);
                binInhoud.add(nieuwBak);
            } else {
                resterendeRuimte[besteBakIndex] -= gewicht[i];
                binInhoud.get(besteBakIndex).add(stockitems[i]);
            }
        }

        return binInhoud;
    }

    private int vindBesteBak(int[] resterendeRuimte, int itemGewicht, int aantalBakken) {
        int minResterendeRuimte = bincapaciteit + 1;
        int besteBakIndex = -1;

        for (int bakIndex = 0; bakIndex < aantalBakken; bakIndex++) {
            if (resterendeRuimte[bakIndex] >= itemGewicht && resterendeRuimte[bakIndex] - itemGewicht < minResterendeRuimte) {
                besteBakIndex = bakIndex;
                minResterendeRuimte = resterendeRuimte[bakIndex] - itemGewicht;
            }
        }

        return besteBakIndex;
    }
}
