package banking;

import java.sql.*;

public class Db {
    Connection conn;


    public Db() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    void connect(String dbPath) throws SQLException {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
        }
    }


    void doStuff() throws SQLException {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            // statement.setQueryTimeout(30);
            statement.executeUpdate("drop table if exists card");
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    void saveAccount(Account account) throws SQLException {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String sql = new StringBuilder("insert into card(number, pin, balance) values ('")
                    .append(account.getCardNumber()).append("','")
//                    .append(account.getPinCode()).append("',")
                    .append(account.getOriginalPinCode()).append("',")
                    .append(account.getBalance())
                    .append(");").toString();
            statement.executeUpdate(sql);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    Account findAccount(String cardNumber) throws SQLException {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            try (ResultSet user = statement.executeQuery(new StringBuilder("select * from card where number = ")
                    .append(cardNumber).append(" limit 1;").toString())) {
                if (user.next()) {
                    Account account = new Account();
                    account.setId(user.getInt("id"));
                    account.setCardNumber(user.getString("number"));
//                    account.setPinCode(user.getString("pin"));
                    account.setOriginalPinCode(user.getString("pin"));
                    account.setBalance(user.getInt("balance"));
                    return account;
                }
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return null;
    }

    void addIncome(Account account, int value) {
        String sql = "UPDATE card SET balance = balance + ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, account.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    int getBalanceByCardID(int cardId) {
        String sql = "SELECT balance FROM card WHERE id = ?";
        int curBalance = 0;
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, cardId);
            ResultSet res = preparedStatement.executeQuery();
            curBalance = res.getInt("balance");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return curBalance;
    }

    void transfer(Account sender, Account recipient, int transferValue) {
        String updateSenderBalance = "UPDATE card SET balance = balance - ? WHERE id = ?";
        String updateRecipientBalance = "UPDATE card SET balance = balance + ? WHERE id = ?";
        try {
            conn.setAutoCommit(false);
            PreparedStatement send = conn.prepareStatement(updateSenderBalance);
            PreparedStatement receive = conn.prepareStatement(updateRecipientBalance);

            send.setInt(1, transferValue);
            send.setInt(2, sender.getId());
            send.executeUpdate();

            receive.setInt(1, transferValue);
            receive.setInt(2, recipient.getId());
            receive.executeUpdate();

            conn.commit();
            System.out.println("Success!");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void closeAccount(Account account) {
        String delAccountSql = "DELETE FROM card WHERE id = ?";
        PreparedStatement delAccount = null;
        try {
            delAccount = conn.prepareStatement(delAccountSql);
            delAccount.setInt(1, account.getId());
            delAccount.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    void disconnect() throws SQLException {
        try {
            conn.close();
        } finally {
            conn = null;
        }
    }
}
