import org.apache.velocity.runtime.directive.StopCommand;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;

public class Post {
    private int postID;
    private String title, content;
    private int year, month, day;
    private String userName, topicName;

    private Database db;

    public Post(int postID, String title, String content, int year, int month, int day, String userName, String topicName, Database db) {
        this.postID = postID;
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.day = day;
        this.userName = userName;
        this.topicName = topicName;
        this.db = db;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getDate() {
        return "" + day + "." + month + "." + year;
    }
    public Topic getTopic() {
        return db.getTopic(topicName);
    }
    public User getUser() {
        return db.getUser(userName);
    }
}
