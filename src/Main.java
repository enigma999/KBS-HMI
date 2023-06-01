import database.Order;
import database.Stockitems;

import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException {
        Stockitems hallo = new Stockitems();
        hallo.InsertOrder(new int[]{4, 7, 20}, new int[]{9, 10, 20} );
    }
}