import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Topic {
    private String name, description;

    private Database db;

    public Topic(String name, String description, Database db) {
        this.name = name;
        this.description = description;
        this.db = db;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public ArrayList<User> getUsers() {
        try {
            ResultSet rs = db.query("select user.* from favourite, user where favourite.topicName == '" + name + "' and favourite.userName == user.userName");
            ArrayList<User> users = new ArrayList<User>();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("userName"),
                        rs.getInt("birthYear"),
                        rs.getInt("birthMonth"),
                        rs.getInt("birthDay"),
                        db
                ));
            }
            return users;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Post> getPosts() {
        try {
            ResultSet rs = db.query("select * from post where topicName == '" + name + "'");
            ArrayList<Post> posts = new ArrayList<Post>();
            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("postID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("year"),
                        rs.getInt("month"),
                        rs.getInt("day"),
                        rs.getString("userName"),
                        rs.getString("topicName"),
                        db
                ));
            }
            return posts;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
