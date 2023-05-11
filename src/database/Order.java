package database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

//import com.github.javafaker.Faker;

// getOrderStockID(), input is int(orderid), hij returnt een array met de stockitemids die bij de order horen.
// getCustomerInfo(), input is int(orderid), returnt naam en adres.
// fillCustomer(), input is een int van hoeveel namen je in de database wil toevoegen, vult de database met random addressen en namen.

public class Order extends Connectie {
    public int[] getOrderStockID (int orderid) throws SQLException {
        if(!this.isConnected())
            this.connect();
        ArrayList<ArrayList<java.lang.String>> result = queryResult("select StockItemID from orderlines where OrderID = " + orderid);
        int[] orderArray = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            ArrayList<String> innerList = result.get(i);
            String value = innerList.get(0);
            orderArray[i] = Integer.parseInt(value);
        }

        return orderArray;
    }

    public ArrayList<java.lang.String> getCustomerInfo (int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<java.lang.String>> result = queryResult("select CustomerID from orders where OrderID = " + orderid);
        String customerId = result.get(0).get(0);
        ArrayList<ArrayList<java.lang.String>> result2 = queryResult("select CustomerName, Address from customer where CustomerID = " + customerId);
        return (result2.get(0));
    }

//    public void fillCustomer(int number_of_names) throws SQLException
//    {
//        if (!this.isConnected())
//            this.connect();
//
//        Faker faker = new Faker(new Locale("NL"));
//
//
//        String query = "INSERT INTO customer (customername, address) VALUES (?, ?)";
//        PreparedStatement ps = this.connection.prepareStatement(query);
//
//        for (int i = 0; i < number_of_names; i++) {
//            String name = faker.name().fullName();
//            String address = faker.address().streetAddress();
//            ps.setString(1, name);
//            ps.setString(2, address);
//            ps.executeUpdate();
//        }
//
//
//    }
}
