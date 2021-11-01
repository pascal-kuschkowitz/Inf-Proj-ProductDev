import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;

public class User {
    private String name;
    private int birthYear, birthMonth, birthDay;

    private Database db;

    public User(String name, int birthYear, int birthMonth, int birthDay, Database db) {
        this.name = name;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.db = db;
    }

    public String getName() {
        return name;
    }
    public int getBirthYear() {
        return birthYear;
    }
    public int getBirthMonth() {
        return birthMonth;
    }
    public int getBirthDay() {
        return birthDay;
    }
    public String getBirth() {
        return "" + birthDay + "." + birthMonth + "." + birthYear;
    }
    public ArrayList<Topic> getTopics() {
        try {
            ResultSet rs = db.query("select topic.topicName from favourite, topic where favourite.userName == '" + name + "' and favourite.topicName == topic.topicName");
            ArrayList<Topic> topics = new ArrayList<Topic>();
            while (rs.next()) {
                topics.add(new Topic(
                        rs.getString("topicName"),
                        db
                ));
            }
            return topics;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
