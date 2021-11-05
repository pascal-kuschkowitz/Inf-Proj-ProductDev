import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    Connection connection = null;
    Statement statement = null;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        try {
            // create database connection
            connection = DriverManager.getConnection("jdbc:sqlite:social_blog.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        create();
        fill();
    }

    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void update(String cmd) {
        try {
            statement.executeUpdate(cmd);
        } catch (SQLException e) {
            System.err.println(cmd);
            System.err.println(e.getMessage());
        }
    }

    public ResultSet query(String cmd) {
        try {
            return statement.executeQuery(cmd);
        } catch (SQLException e) {
            System.err.println(cmd);
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void create() {
        update("drop table if exists user");
        update("drop table if exists post");
        update("drop table if exists topic");
        update("drop table if exists favourite");

        update("create table user (userName char(128), birthYear int, birthMonth int, birthDay int)");

        update("create table post (postId int, title char(128), content char(1000000), year int, month int, day int, userName char(128), topicName char(128))");

        update("create table topic (topicName char(128), description char(512))");
        update("create table favourite (userName char(128), topicName char(128))");
    }

    private void fill() {
        User chris = createUser("chris", 1100, 12, 12);
        User pascal = createUser("pascal", 9999, 10, 1);
        User kathi = createUser("kathi", 1, 12, 24);
        User tim = createUser("tim", 1, 12, 24);

        Topic cheese = createTopic("cheese", "Everything there is to know about cheese.");
        Topic trains = createTopic("trains", "I like trains!");
        Topic peace = createTopic("peace", "Make Peace, not War.");

        chris.addFavouriteTopic(peace);
        chris.addFavouriteTopic(trains);
        pascal.addFavouriteTopic(trains);

        createPost("Why Trains are the Better Pets", "bam, I like cheese", 2021, 11, 1, chris, trains);
        createPost("Frontends Suck!", "Yes, they do!", 2021, 13, 1, pascal, peace);
        createPost("Backends are good!", "So what?", 2022, 13, 1, tim, peace);
    }

    // data loader
    public ArrayList<User> getUsers() {
        try {
            ResultSet rs = query("select * from user");
            ArrayList<User> users = new ArrayList<User>();
            while (rs.next()) {
                users.add(new User(rs.getString("userName"), rs.getInt("birthYear"), rs.getInt("birthMonth"),
                        rs.getInt("birthDay"), this));
            }
            return users;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public User getUser(String name) {
        try {
            ResultSet rs = query("select * from user where userName == '" + name + "'");
            return new User(rs.getString("userName"), rs.getInt("birthYear"), rs.getInt("birthMonth"),
                    rs.getInt("birthDay"), this);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Topic> getTopics() {
        try {
            ResultSet rs = query("select * from topic");
            ArrayList<Topic> topics = new ArrayList<Topic>();
            while (rs.next()) {
                topics.add(new Topic(
                        rs.getString("topicName"),
                        rs.getString("description"),
                        this
                ));
            }
            return topics;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Topic getTopic(String name) {
        try {
            ResultSet rs = query("select * from topic where topicName == '" + name + "'");
            return new Topic(
                    rs.getString("topicName"),
                    rs.getString("description"),
                    this
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Post> getPosts() {
        try {
            ResultSet rs = query("select * from post");
            ArrayList<Post> posts = new ArrayList<Post>();
            while (rs.next()) {
                posts.add(new Post(rs.getInt("postID"), rs.getString("title"), rs.getString("content"),
                        rs.getInt("year"), rs.getInt("month"), rs.getInt("day"), rs.getString("userName"),
                        rs.getString("topicName"), this));
            }
            return posts;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Post getPost(int postId) {
        try {
            ResultSet rs = query("select * from post where postId == '" + postId + "'");
            return new Post(rs.getInt("postID"), rs.getString("title"), rs.getString("content"), rs.getInt("year"),
                    rs.getInt("month"), rs.getInt("day"), rs.getString("userName"), rs.getString("topicName"), this);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public int getPostCount() {
        try {
            ResultSet rs = query("select count(*) from post");
            return rs.getInt("count(*)");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    // data writer
    public User createUser(String name, int birthYear, int birthMonth, int birthDay) {
        User user = new User(name, birthYear, birthMonth, birthDay, this);
        update("insert into user values('" + name + "', " + birthYear + ", " + birthMonth + ", " + birthDay + ")");
        return user;
    }

    public Topic createTopic(String name, String description) {
        Topic topic = new Topic(name, description, this);
        update("insert into topic values('" + name + "', '" + description + "')");
        return topic;
    }

    public Post createPost(String title, String content, int year, int month, int day, User user, Topic topic) {
        int new_id = getPostCount();
        String userName = user.getName();
        String topicName = topic.getName();
        Post post = new Post(new_id, title, content, year, month, day, userName, topicName, this);
        update("insert into post values(" + new_id + ", '" + title + "', '" + content + "', " + year + ", " + month
                + ", " + day + ", '" + userName + "', '" + topicName + "')");
        return post;
    }
}
