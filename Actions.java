package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class Actions {
    public static void addIncome(String dbPath, Account account) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Db db = new Db();
        System.out.print("Enter income:\n>");
        int incomeValue = scanner.nextInt();
        db.connect(dbPath);
        db.addIncome(account, incomeValue);
        account.setBalance(db.getBalanceByCardID(account.getId()));
        db.disconnect();
        System.out.println("Income was added!");
        try {
            Main.authorizationMenu(dbPath, account);
        } catch (Exception e) {
            try {
                Main.mainMenu(dbPath);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void transfer(String dbPath, Account account) {
        Scanner scanner = new Scanner(System.in);
        Validator validator = new Validator(account, dbPath);
        boolean isError;
        System.out.print("Transfer\n Enter card number:\n>");
        String recipientCardNumber = scanner.nextLine().replace("\\s+", "").replace(">", "");
        isError = validator.checkTransferRecipient(recipientCardNumber);
        if (isError) {
            try {
                Main.authorizationMenu(dbPath, account);
            } catch (Exception e) {
                try {
                    Main.mainMenu(dbPath);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            return;
        }
        System.out.print("Enter how much money you want to transfer:\n>");
        int transferValue = scanner.nextInt();
        isError = validator.checkTransferValue(transferValue);
        if (isError) {
            try {
                Main.authorizationMenu(dbPath, account);
            } catch (Exception e) {
                try {
                    Main.mainMenu(dbPath);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            return;
        }
        Db db = new Db();
        Account recipient;
        try {
            db.connect(dbPath);
            recipient = db.findAccount(recipientCardNumber);
            db.transfer(account, recipient, transferValue);
            account.setBalance(account.getId());
            db.disconnect();
            Main.authorizationMenu(dbPath, account);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeAccount(String dbPath,Account account) throws SQLException {
        Db db = new Db();
        db.connect(dbPath);
        db.closeAccount(account);
        db.disconnect();
        try {
            Main.mainMenu(dbPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Account refreshBalance(String dbPath, Account account) throws SQLException {
        Db db = new Db();
        db.connect(dbPath);
        int curBalance = db.getBalanceByCardID(account.getId());
        db.disconnect();
        account.setBalance(curBalance);
        return account;
    }
}
