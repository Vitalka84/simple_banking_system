package banking;

public class Main {
    public static void main(String[] args) throws Exception {
        String dbName = "default_db.s3db";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName") && i + 1 <= args.length) {
                dbName = args[i + 1];
            }
        }
        String dbPath = "/Users/vetal/Developing/study/java_rush/Simple Banking System1/Simple Banking System/task/" + dbName;
        Db db = new Db(dbPath);
        Menus menus = new Menus(db);
        menus.mainMenu();
    }
}