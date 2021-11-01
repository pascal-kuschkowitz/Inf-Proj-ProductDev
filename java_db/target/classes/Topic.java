import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
}
