package database;
import java.sql.*;
import java.util.ArrayList;

// connect() connect met de database, als dit niet lukt staat de connectie op null
// disconnect() disconnect met de database, zet de connectie op null
// isConnected() checkt of de connectie niet op null staat
// queryResult(), de input is een String met een geldige query, de functie returnt de output van de query
// printQueryResult(), print het resultaat van de query in de terminal


public class Connectie {
    private final String hostname;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

    protected java.sql.Connection connection;
    public Connectie()
    {
        this.hostname = "localhost";
        this.port = "3306";
        this.username = "root";
        this.password = "";
        this.database = "nerdygadgets";
    }
    public void connect()
    {
        String url = "jdbc:mysql://"+this.hostname+":"+this.port+"/"+this.database;
        try
        {
            this.connection = DriverManager.getConnection(url, this.username, this.password);
        } catch(SQLException ex)
        {
            this.connection = null;
        }
    }

    public void disconnect()
    {
        try {
            this.connection.close();
        } catch (SQLException e) {
            this.connection = null;
        }
        this.connection = null;
    }

    public boolean isConnected()
    {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<ArrayList<String>> queryResult(String query) throws SQLException
    {
        if(!this.isConnected())
            this.connect();

        PreparedStatement ps = this.connection.prepareStatement(query);
        ResultSet statement = ps.executeQuery(query);
        ResultSetMetaData rsmd = statement.getMetaData();

        int numColumns = rsmd.getColumnCount();

        ArrayList<ArrayList<String>> results = new ArrayList<>();

        while (statement.next()) {
            ArrayList<String> row = new ArrayList<>();
            for (int i = 1; i <= numColumns; i++) {
                row.add(statement.getString(i));
            }
            results.add(row);
        }
        return results;
    }

    public void printQueryResult(String query) throws SQLException{
        if(!this.isConnected())
            this.connect();

        PreparedStatement ps = this.connection.prepareStatement(query);
        ResultSet statement = ps.executeQuery(query);

        ResultSetMetaData rsmd = statement.getMetaData();

        int numColumns = rsmd.getColumnCount();

        ArrayList<ArrayList<String>> results = new ArrayList<>();

        while (statement.next()) {
            ArrayList<String> row = new ArrayList<>();
            for (int i = 1; i <= numColumns; i++) {
                row.add(statement.getString(i));
            }
            results.add(row);
        }
        System.out.println(results);
    }
}
