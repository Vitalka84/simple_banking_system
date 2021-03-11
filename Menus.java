package banking;

import java.util.Optional;
import java.util.Scanner;

public class Menus {
    Db db;
    Actions actions;
    Account account;
    Validator validator;

    public Menus(Db db) {
        this.db = db;
        this.actions = new Actions(db);
        this.validator = new Validator(db);
    }

    protected void mainMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n" +
                ">");
        String input = scanner.nextLine().replace("\\s+", "").replace(">", "");
        switch (input) {
            case "1":
                db.saveAccount(new Account().genAccount());
                mainMenu();
                break;
            case "2":
                login();
                break;
            case "0":
                System.out.println("Bye!");
                return;
            default:
                mainMenu();
                break;
        }

    }

    protected void authorizationMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit\n" +
                ">");
        String input = scanner.nextLine().replace("\\s+", "").replace(">", "");
        switch (input) {
            case "1":
                System.out.println("Balance: " + account.getBalance());
                authorizationMenu();
                break;
            case "2":
                if (account != null) {
                    actions.addIncome(account);
                    authorizationMenu();
                } else {
                    mainMenu();
                }
                break;
            case "3":
                System.out.print("Transfer\n Enter card number:\n>");
                String recipientCardNumber = scanner.nextLine().replace("\\s+", "").replace(">", "");
                validator.setAccount(account);
                if (validator.checkTransferRecipient(recipientCardNumber)) {
                    authorizationMenu();
                    break;
                }
                System.out.print("Enter how much money you want to transfer:\n>");
                int transferValue = scanner.nextInt();
                if (validator.checkTransferValue(transferValue)) {
                    authorizationMenu();
                    break;
                }
                Account recipient = db.findAccount(recipientCardNumber);
                actions.transfer(account, recipient, transferValue);
                validator.setAccount(null);
                authorizationMenu();
                break;
            case "4":
                if (account != null) {
                    actions.closeAccount(account);
                    validator.setAccount(null);
                }
                mainMenu();
                break;
            case "5":
                account.unAuthorization();
                account = null;
                validator.setAccount(null);
                mainMenu();
                break;
            case "0":
                account = null;
                validator.setAccount(null);
                System.out.println("Bye!");
                return;
            default:
                authorizationMenu();
                break;
        }
    }

    private void login() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your card number:\n>");
        String cardNumber = scanner.nextLine().replace("\\s+", "").replace(">", "");
        System.out.print("Enter your PIN:\n>");
        String pin = scanner.nextLine().replace("\\s+", "").replace(">", "");
        Optional<Account> myAccount = Optional.ofNullable(db.findAccount(cardNumber));
        if (myAccount.isPresent()) {
            myAccount.get().authorization(pin);
        }
        if (myAccount.isEmpty() || !myAccount.get().isAuthorized()) {
            System.out.println("Wrong card number or PIN!");
            mainMenu();
        } else {
            System.out.println("You have successfully logged in!");
            account = myAccount.get();
            authorizationMenu();
        }
    }
}
