package database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.List;
import database.Stockitems;

//rip Faker;

// getOrderStockID(), input is int(orderid), hij returnt een array met de stockitemids die bij de order horen.
// getOrderLines(), input is int(orderid), hij returnt een array met de orderlineids die bij de order horen.
// getOrderInfo(), input is int(orderid), hij return een array met de items, hoeveelheid en prijs die bij de order passen.
// getCustomerInfo(), input is int(orderid), returnt naam en adres.
// getOrderDate(), input is int(orderid), returnt orderid en orderdatum.
// printPakBon(), input is int(orderid), print de pakbon in de terminal.
// getPakBon(), input is int(orderid), returnt een array met alle informatie van de pakbon.
// fillCustomer(), input is een int van hoeveel namen je in de database wil toevoegen, vult de database met random addressen en namen.
// InsertOrder(), input is een int[] van stockitemids en een int[] van quantities, geen output

public class Order extends Connectie {
    public int[] getOrderStockID(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<String>> result = queryResult("SELECT StockItemID FROM orderline WHERE OrderID = " + orderid);
        int[] orderArray = new int[result.size()];

        for (int i = 0; i < result.size(); i++) {
            ArrayList<String> innerList = result.get(i);
            String value = innerList.get(0);
            orderArray[i] = Integer.parseInt(value);
        }

        return orderArray;
    }

    public int[] getOrderLines (int orderid) throws SQLException {
        if(!this.isConnected())
            this.connect();
        ArrayList<ArrayList<java.lang.String>> result = queryResult("select orderlineID from orderline where OrderID = " + orderid);
        int[] orderArray = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            ArrayList<String> innerList = result.get(i);
            String value = innerList.get(0);
            orderArray[i] = Integer.parseInt(value);
        }

        return orderArray;
    }

    public ArrayList<ArrayList<String>> getOrderInfo(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        int[] orderLineIDs = getOrderLines(orderid);

        ArrayList<ArrayList<String>> orderInfo = new ArrayList<>();

        for (int orderLineID : orderLineIDs) {
            ArrayList<ArrayList<String>> result = queryResult("SELECT description, quantity, unitprice FROM orderline WHERE OrderLineID = " + orderLineID);

            orderInfo.addAll(result);
        }

        return orderInfo;
    }

    public ArrayList<java.lang.String> getCustomerInfo (int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<java.lang.String>> result = queryResult("select CustomerID from ordertabel where OrderID = " + orderid);
        String customerId = result.get(0).get(0);
        ArrayList<ArrayList<java.lang.String>> result2 = queryResult("select CustomerName, Address from customer where CustomerID = " + customerId);
        return (result2.get(0));
    }

    public String[] getOrderDate(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<String>> orderDateResult = queryResult("SELECT OrderDate FROM ordertabel WHERE OrderID = " + orderid);
        String orderDate = orderDateResult.get(0).get(0);

        String[] orderDateArray = new String[2];
        orderDateArray[0] = String.valueOf(orderid);
        orderDateArray[1] = orderDate;

        return orderDateArray;
    }

    public void printPakBon(int orderid) throws SQLException {
        ArrayList<ArrayList<String>> orderInfo = getOrderInfo(orderid);
        ArrayList<String> customerInfo = getCustomerInfo(orderid);
        String[] orderDateArray = getOrderDate(orderid);

        System.out.println("OrderID en Datum: [" + orderid + ", " + orderDateArray[1] + "]");
        System.out.println("Klant en Adres: " + customerInfo);
        System.out.println("Producten, Aantal en Prijs: " + orderInfo);
    }

    public ArrayList<ArrayList<String>> getPakBon(int orderid) throws SQLException {
        ArrayList<ArrayList<String>> orderAndCustomerInfo = new ArrayList<>();

        ArrayList<ArrayList<String>> orderInfo = getOrderInfo(orderid);
        ArrayList<String> customerInfo = getCustomerInfo(orderid);
        String[] orderDateArray = getOrderDate(orderid);

        orderAndCustomerInfo.addAll(orderInfo);
        orderAndCustomerInfo.add(customerInfo);
        orderAndCustomerInfo.add(new ArrayList<>(Arrays.asList(orderDateArray)));



        return orderAndCustomerInfo;
    }

    public ArrayList<java.lang.String> getInfoStockitem(int stockitemid) throws SQLException{
        if (!this.isConnected())
            this.connect();
        ArrayList<ArrayList<java.lang.String>> result = queryResult("select stockitemname, unitprice from stockitems where StockItemID = " + stockitemid);
        return (result.get(0));
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
}
