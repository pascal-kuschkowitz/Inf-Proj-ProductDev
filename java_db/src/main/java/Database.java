import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    Connection m_connection = null;
    Statement m_statement = null;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        try {
            // create database connection
            m_connection = DriverManager.getConnection("jdbc:sqlite:social_blog.db");
            m_statement = m_connection.createStatement();
            m_statement.setQueryTimeout(30);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        create();
        fill();
    }

    public void close() {
        try {
            if (m_connection != null)
                m_connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void create() {
        // todo: add favourite topic to user
        update("drop table if exists user");
        update("drop table if exists post");
        update("drop table if exists belongs");
        update("drop table if exists topic");
        update("create table user (userName char(128), birthYear int, birthMonth int, birthDay int)");
        update("create table post (postId int, title char(128), content char(1000000), year int, month int, day int, userName char(128))");
        update("create table belongs (belongsId int, postId int, topicName char(128))");
        update("create table topic (topicName char(128))");
    }

    private void fill() {
        // todo: replace with abstraction
        update("insert into user values('chris', 1100, 12, 12)");
        update("insert into user values('pascal', 9999, 10, 1)");
        update("insert into user values('kathi', 1, 12, 24)");
    }

    public void update(String cmd) {
        try {
            m_statement.executeUpdate(cmd);
        } catch (SQLException e) {
            System.err.println(cmd);
            System.err.println(e.getMessage());
        }
    }

    public ResultSet query(String cmd) {
        try {
            return m_statement.executeQuery(cmd);
        } catch (SQLException e) {
            System.err.println(cmd);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<User> get_users() {
        try {
            ResultSet rs = query("select * from user");
            ArrayList<User> users = new ArrayList<User>();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("userName"),
                        rs.getInt("birthYear"),
                        rs.getInt("birthMonth"),
                        rs.getInt("birthDay")
                ));
            }
            return users;
        } catch (SQLException e) {
                System.err.println(e.getMessage());
                return null;
        }
    }

    public static class User {
        public String name;
        public int birth_year, birth_month, birth_day;

        public User(String name, int birth_year, int birth_month, int birth_day) {
            this.name = name;
            this.birth_year = birth_year;
            this.birth_month = birth_month;
            this.birth_day = birth_day;
        }
    }
}