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
            ResultSet rs = db.query("select topic.* from favourite, topic where favourite.userName == '" + name + "' and favourite.topicName == topic.topicName");
            ArrayList<Topic> topics = new ArrayList<Topic>();
            while (rs.next()) {
                topics.add(new Topic(
                        rs.getString("topicName"),
                        rs.getString("description"),
                        db
                ));
            }
            return topics;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Post> getPosts() {
        try {
            ResultSet rs = db.query("select * from post where userName == '" + name + "' order by year desc, month desc, day desc");
            ArrayList<Post> posts = new ArrayList<Post>();
            while (rs.next()) {
                posts.add(new Post(rs.getInt("postID"), rs.getString("title"), rs.getString("content"),
                        rs.getInt("year"), rs.getInt("month"), rs.getInt("day"), rs.getString("userName"),
                        rs.getString("topicName"), db));
            }
            return posts;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void addFavouriteTopic(Topic topic) {
        db.update("insert into favourite values('" + name + "', '" + topic.getName() + "')");
    }
}
