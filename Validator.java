package banking;

import java.sql.SQLException;

public class Validator {
    Account account;
    String dbPath;

    public Validator(Account account, String dbPath) {
        this.account = account;
        this.dbPath = dbPath;
    }

    public boolean checkTransferRecipient(String recCardNumber) {
        if (account.getCardNumber().equals(recCardNumber)) {
            System.out.println(ErrorCodes.e0002.getDescription() + "\n");
            return true;
        }
        int[] cardDigits = new int[recCardNumber.length()];
        for (int i = 0; i < recCardNumber.length(); i++) {
            cardDigits[i] = Integer.parseInt(recCardNumber.substring(i, i + 1));
        }
        for (int i = cardDigits.length - 2; i >= 0; i = i - 2) {
            int j = cardDigits[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            cardDigits[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < cardDigits.length; i++) {
            sum += cardDigits[i];
        }
        if (sum % 10 != 0) {
            System.out.println(ErrorCodes.e0003.getDescription() + "\n");
            return true;
        }
        Db db = new Db();
        Account recipient = new Account();
        try {
            db.connect(dbPath);
            recipient = db.findAccount(recCardNumber);
            db.disconnect();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (recipient == null) {
            System.out.println(ErrorCodes.e0004.getDescription() + "\n");
            return true;
        }

        return false;
    }

    public boolean checkTransferValue(int transferValue) {
        Db db = new Db();
        try {
            db.connect(dbPath);
            account.setBalance(db.getBalanceByCardID(account.getId()));
            db.disconnect();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (transferValue > account.getBalance()) {
            System.out.println(ErrorCodes.e0001.getDescription() + "\n");
            return true;
        }
        return false;
    }
}
