public class DBinfo {
    private static final String DB_URI = "mongodb+srv://akwp0317:monkim9860@cluster0.n0b3o2l.mongodb.net/NewYears";
    private static final String DB = "NewYears";
    private static final String DB_C = "To-do";

    public static String getDB_URI() {
        return DB_URI;
    }

    public static String getDB() {
        return DB;
    }

    public static String getDB_C() {
        return DB_C;
    }
}
