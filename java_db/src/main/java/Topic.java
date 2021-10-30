public class Topic {
    private String name;

    private Database db;

    public Topic(String name, Database db) {
        this.name = name;
        this.db = db;
    }

    public String getName() {
        return name;
    }
}
