package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    static String url;

    public Database(String url) {
        this.url = url;
        this.checkConnection();
    }

    private void checkConnection() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER AUTOINCREMENT," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0," +
                        "PRIMARY KEY (id));");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

