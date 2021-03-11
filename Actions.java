package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class Actions {
    Db db;

    public Actions(Db db) {
        this.db = db;
    }

    public void addIncome(Account account) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter income:\n>");
        int incomeValue = scanner.nextInt();
        db.addIncome(account, incomeValue);
        account.setBalance(db.getBalanceByCardID(account.getId()));
        System.out.println("Income was added!");
    }

    public void transfer(Account sender, Account recipient, int transferValue) {
        db.transfer(sender, recipient, transferValue);
        sender.setBalance(sender.getId());
    }

    public void closeAccount(Account account) {
        db.closeAccount(account);
    }

    public Account refreshBalance(String dbPath, Account account) throws SQLException {
        int curBalance = db.getBalanceByCardID(account.getId());
        account.setBalance(curBalance);
        return account;
    }
}
