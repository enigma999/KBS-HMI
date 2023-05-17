package database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

// getCoordinaten() print de coordinaten van een bepaald StockItemID, de input is een int of int[]. De output is een array met coordinaten [x, y] of meerdere arrays met coordinaten [[x, y], [x, y]].
// randomXY() zorgt voor het vullen van de coordinaten met random inten (0-4) in de database. Geen input of output.

public class Stockitems extends Connectie {
    public ArrayList<java.lang.String> getCoordinaten(int stockitemid) throws SQLException {
        if (!this.isConnected())
            this.connect();
        ArrayList<ArrayList<java.lang.String>> result = queryResult("select x, y from stockitemholdings where StockItemID = " + stockitemid);
        return (result.get(0));
    }

    public ArrayList<ArrayList<String>> getCoordinaten(int[] stockitemid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<String>> results = new ArrayList<>();

        for (int i = 0; i < stockitemid.length; i++) {
            results.add(getCoordinaten(stockitemid[i]));
        }

        return results;
    }



    public void randomXY() throws SQLException {
        if (!this.isConnected())
            this.connect();

        Random rand = new Random();

        String selectStockItemIdsQuery = "SELECT stockitemid FROM stockitemholdings";
        PreparedStatement psStockItemIds = this.connection.prepareStatement(selectStockItemIdsQuery);
        ResultSet stockItemIdsResultSet = psStockItemIds.executeQuery();

        String updateXQuery = "UPDATE stockitemholdings SET x = ? WHERE stockitemid = ?";
        PreparedStatement psX = this.connection.prepareStatement(updateXQuery);

        String updateYQuery = "UPDATE stockitemholdings SET y = ? WHERE stockitemid = ?";
        PreparedStatement psY = this.connection.prepareStatement(updateYQuery);

        while (stockItemIdsResultSet.next()) {
            int stockItemId = stockItemIdsResultSet.getInt("stockitemid");

            int value = rand.nextInt(5);
            int value1 = rand.nextInt(5);

            psX.setInt(1, value);
            psX.setInt(2, stockItemId);
            psX.executeUpdate();

            psY.setInt(1, value1);
            psY.setInt(2, stockItemId);
            psY.executeUpdate();
        }
        disconnect();
    }
}
