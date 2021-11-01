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

    private void create() {
        update("drop table if exists user");
        update("drop table if exists post");
        update("drop table if exists belongs");
        update("drop table if exists topic");
        update("drop table if exists favourite");

        update("create table user (userName char(128), birthYear int, birthMonth int, birthDay int)");

        update("create table post (postId int, title char(128), content char(1000000), year int, month int, day int, userName char(128))");
        update("create table belongs (postId int, topicName char(128))");

        update("create table topic (topicName char(128))");
        update("create table favourite (userName char(128), topicName char(128))");
    }

    private void fill() {
        // TODO: replace with abstraction
        update("insert into user values('chris', 1100, 12, 12)");
        update("insert into user values('pascal', 9999, 10, 1)");
        update("insert into user values('kathi', 1, 12, 24)");

        update("insert into topic values('cheese')");
        update("insert into topic values('trains')");
        update("insert into topic values('peace')");

        update("insert into favourite values('chris', 'peace')");
        update("insert into favourite values('chris', 'trains')");
        update("insert into favourite values('chris', 'peace')");
        update("insert into favourite values('pascal', 'trains')");
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

    public ArrayList<User> getUsers() {
        try {
            ResultSet rs = query("select userName, birthYear, birthMonth, birthDay from user");
            ArrayList<User> users = new ArrayList<User>();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("userName"),
                        rs.getInt("birthYear"),
                        rs.getInt("birthMonth"),
                        rs.getInt("birthDay"),
                        this
                ));
            }
            return users;
        } catch (SQLException e) {
                System.err.println(e.getMessage());
                return null;
        }
    }

    public User getUser(String name) {
        try {
            ResultSet rs = query("select userName, birthYear, birthMonth, birthDay from user where userName == " + name);
            return new User(
                rs.getString("userName"),
                rs.getInt("birthYear"),
                rs.getInt("birthMonth"),
                rs.getInt("birthDay"),
                this
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Topic> getTopics() {
        try {
            ResultSet rs = query("select topicName from topic");
            ArrayList<Topic> topics = new ArrayList<Topic>();
            while (rs.next()) {
                topics.add(new Topic(
                        rs.getString("topicName"),
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
            ResultSet rs = query("select * from topic where topicName == " + name);
            return new Topic(
                rs.getString("topicName"),
                this
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
