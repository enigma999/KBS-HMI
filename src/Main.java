import database.Stockitems;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Stockitems hallo = new Stockitems();
        hallo.printQueryResult("select * from orderlines");
    }
}