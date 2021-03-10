package banking;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String dbName = "default_db.s3db";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName") && i + 1 <= args.length) {
                dbName = args[i + 1];
            }
        }
        String dbPath = "/Users/vetal/Developing/study/java_rush/Simple Banking System1/Simple Banking System/task/" + dbName;
        mainMenu(dbPath);
    }

    protected static void mainMenu(String dbPath) throws Exception {
        Db db = new Db();
        db.connect(dbPath);
        db.disconnect();
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n" +
                ">");
        String input = scanner.nextLine().replace("\\s+", "").replace(">", "");
        switch (input) {
            case "1":
                db.connect(dbPath);
                db.saveAccount(new Account().genAccount());
                db.disconnect();
                mainMenu(dbPath);
                break;
            case "2":
                login(dbPath);
                break;
            case "0":
                System.out.println("Bye!");
                return;
            default:
                mainMenu(dbPath);
                break;
        }

    }

    protected static void authorizationMenu(String dbPath, Account account) throws Exception {
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
                authorizationMenu(dbPath, account);
                break;
            case "2":
                Actions.addIncome(dbPath, account);
                break;
            case "3":
                Actions.transfer(dbPath, account);
                break;
            case "4":
                Actions.closeAccount(dbPath, account);
                break;
            case "5":
                account.unAuthorization();
                mainMenu(dbPath);
                break;
            case "0":
                System.out.println("Bye!");
                return;
            default:
                authorizationMenu(dbPath, account);
                break;
        }
    }

    private static void login(String dbPath) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Db db = new Db();
        System.out.print("Enter your card number:\n>");
        String cardNumber = scanner.nextLine().replace("\\s+", "").replace(">", "");
        System.out.print("Enter your PIN:\n>");
        String pin = scanner.nextLine().replace("\\s+", "").replace(">", "");
        db.connect(dbPath);
        Optional<Account> myAccount = Optional.ofNullable(db.findAccount(cardNumber));
        db.disconnect();
        if (myAccount.isPresent()) {
            myAccount.get().authorization(pin);
        }
        if (myAccount.isEmpty() || !myAccount.get().isAuthorized()) {
            System.out.println("Wrong card number or PIN!");
            mainMenu(dbPath);
        } else {
            System.out.println("You have successfully logged in!");
            authorizationMenu(dbPath, myAccount.get());
        }
    }
}