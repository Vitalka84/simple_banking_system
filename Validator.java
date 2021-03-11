package banking;

import java.sql.SQLException;

public class Validator {
    Account account;
    Db db;

    public Validator(Account account, Db db) {
        this.account = account;
        this.db = db;
    }

    public Validator(Db db) {
        this.db = db;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean checkTransferRecipient(String recCardNumber) {
        if (account == null) {
            System.out.println("account not set");
            return true;
        }
        if (account.getCardNumber().equals(recCardNumber)) {
            System.out.println(ErrorCodes.e0002.getDescription() + "\n");
            return true;
        }
        if (!isLuhnAlgorithm(recCardNumber)) {
            System.out.println(ErrorCodes.e0003.getDescription() + "\n");
            return true;
        }
        Account recipient = new Account();
        recipient = db.findAccount(recCardNumber);
        if (recipient == null) {
            System.out.println(ErrorCodes.e0004.getDescription() + "\n");
            return true;
        }

        return false;
    }

    public boolean isLuhnAlgorithm(String cardNumber) {
        int[] cardDigits = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            cardDigits[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
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
            return false;
        }
        return true;
    }

    public boolean checkTransferValue(int transferValue) {
        try {
            account.setBalance(db.getBalanceByCardID(account.getId()));
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
