package banking;

import java.security.SecureRandom;

public class Account {
    private int id;
    private String cardNumber;
    private String pinCode;
    private double balance;
    private String originalPinCode;
    private boolean isAuthorized = false;

    public Account genAccount() throws Exception {
        Account account = new Account();
        account.setCardNumber(RandomCardNumberGenerator.generateMyBankingSystemCardNumber());
        account.setOriginalPinCode(genPinCode());
        account.setPinCode(Password.getSaltedHash(account.getOriginalPinCode()));
        account.setBalance(0);
        account.authorization(account.getOriginalPinCode());
        System.out.printf("Your card has been created\nYour card number:\n%s\nYour card PIN:\n%s\n", account.getCardNumber(), account.getOriginalPinCode());
        return account;
    }

    public Account() {
    }

    private String genPinCode() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(10000);
        return String.format("%04d", num);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public double getBalance() {
        return isAuthorized ? balance : null;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void unAuthorization() {
        this.isAuthorized = false;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setCardNumber(String cardNumber) {
        if (this.cardNumber == null) {
            this.cardNumber = cardNumber;
        }
    }

    public void setPinCode(String pinCode) {
        if (this.pinCode == null) {
            this.pinCode = pinCode;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalPinCode() {
        return originalPinCode;
    }

    public void setOriginalPinCode(String originalPinCode) {
        this.originalPinCode = originalPinCode;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void authorization(String pin) {
        try {
            if (Password.check(pin, this.pinCode)) {
                this.isAuthorized = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
