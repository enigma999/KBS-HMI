package database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

// getCoordinaten() returnt de coordinaten van een bepaald StockItemID, de input is een int of int[]. De output is een int[] met coordinaten [x, y] of meerdere arrays met coordinaten [[x, y], [x, y]].
// getGewicht() returnt het gewicht van een bepaald StockItemID, de input is een int of int[]. De output is een int of een int[] met alle gewichten die bij de StockItemIDs horen.
// InsertOrder(), input is een int[] van stockitemids en een int[] van quantities, geen output

public class Stockitems extends Connectie {
    public int[] getCoordinaten(int stockitemid) throws SQLException {
        if (!this.isConnected())
            this.connect();
        ArrayList<ArrayList<String>> result = queryResult("select x, y from stockitemholding where StockItemID = " + stockitemid);
        ArrayList<String> coordinates = result.get(0);
        int[] intCoordinates = new int[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            intCoordinates[i] = Integer.parseInt(coordinates.get(i));
        }
        return intCoordinates;
    }
    public ArrayList<int[]> getCoordinaten(int[] stockitemid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<int[]> results = new ArrayList<>();

        for (int i = 0; i < stockitemid.length; i++) {
            int[] coordinates = getCoordinaten(stockitemid[i]);
            results.add(coordinates);
        }

        return results;
    }
}

    public int getGewicht(int stockitemid) throws SQLException {
        if (!this.isConnected())
            this.connect();
        ArrayList<ArrayList<String>> result = queryResult("select gewicht from stockitemholding where StockItemID = " + stockitemid);
        ArrayList<String> resultaat = result.get(0);

        return Integer.parseInt(resultaat.get(0));
    }

    public int[] getGewicht(int[] stockitemid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        int[] results = new int[stockitemid.length];

        for (int i = 0; i < stockitemid.length; i++) {
            results[i] = getGewicht(stockitemid[i]);
        }

        return results;
    }
    public void InsertOrder(int[] stockitemids, int[] quantities) throws SQLException {
        if (!this.isConnected())
            this.connect();

        String customerName = "Manual";
        LocalDate date = LocalDate.now();

        int customerID = findCustomerID(customerName);
        int newOrderID = insertOrder(date, customerID);

        for (int i = 0; i < stockitemids.length; i++) {
            int stockitemid = stockitemids[i];
            int quantity = quantities[i];

            ArrayList<String> stockitemInfo = getInfoStockitem(stockitemid);
            String stockitemName = stockitemInfo.get(0);
            double unitPrice = Double.parseDouble(stockitemInfo.get(1));

            insertOrderLine(newOrderID, stockitemid, stockitemName, quantity, unitPrice);
        }
    }
    private ArrayList<java.lang.String> getInfoStockitem(int stockitemid) throws SQLException{
        if (!this.isConnected())
            this.connect();
        ArrayList<ArrayList<java.lang.String>> result = queryResult("select stockitemname, unitprice from stockitem where StockItemID = " + stockitemid);
        return (result.get(0));
    }

    private int findCustomerID(String customerName) throws SQLException {
        PreparedStatement customerStatement = connection.prepareStatement("SELECT CustomerID FROM customer WHERE CustomerName = ?");
        customerStatement.setString(1, customerName);
        ResultSet customerResult = customerStatement.executeQuery();

        if (customerResult.next()) {
            return customerResult.getInt("CustomerID");
        } else {
            throw new RuntimeException("Customer not found for name: " + customerName);
        }
    }

    private int insertOrder(LocalDate date, int customerID) throws SQLException {
        PreparedStatement orderStatement = connection.prepareStatement("INSERT INTO ordertabel (orderdate, picked, customerid) VALUES (?, 0, ?)", Statement.RETURN_GENERATED_KEYS);
        orderStatement.setDate(1, java.sql.Date.valueOf(date));
        orderStatement.setInt(2, customerID);
        orderStatement.executeUpdate();

        ResultSet generatedKeys = orderStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }

        throw new SQLException("Failed to retrieve new order ID.");
    }

    private void insertOrderLine(int orderID, int stockitemid, String description, int quantity, double unitPrice) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO orderline (orderid, stockitemid, description, quantity, unitprice) VALUES (?, ?, ?, ?, ?)");
        statement.setInt(1, orderID);
        statement.setInt(2, stockitemid);
        statement.setString(3, description);
        statement.setInt(4, quantity);
        statement.setDouble(5, unitPrice);
        statement.executeUpdate();
    }

    public void UpdateOrder(int orderID, String name, String address, int quantity) {
    }
}
