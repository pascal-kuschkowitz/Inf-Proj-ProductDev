import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
// postId int, title char(128), content char(1000000), year int, month int, day int, userName char(128))
public class Post {
    private int id;
    private String title, content;
    private int year, month, day;
    private String author;

    private Database db;

    public Post(int id, String title, String content, int year, int month, int day, Database db) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.day = day;
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
}
