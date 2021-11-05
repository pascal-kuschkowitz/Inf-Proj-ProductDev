import static spark.Spark.get;
import static spark.Spark.post;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Object;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class Main {

    public static void main(final String[] args) throws ClassNotFoundException {
        System.out.println("Starting Webserver, open http://localhost:4567/test in a web browser.");
        System.out.println();
        System.out.println();
        System.out.println();
        Database db = new Database();

        get("/", (request, response) -> {
            ArrayList<Post> posts = db.getPosts();
            Map<String, Object> model = new HashMap<>();
            model.put("posts", posts);
            return new ModelAndView(model, "home.html");
        }, new VelocityTemplateEngine());

        get("/posts/:name", (request, response) -> {
            Post post = db.getPost(Integer.parseInt(request.params(":name")));
            Map<String, Object> model = new HashMap<>();
            model.put("post", post);
            return new ModelAndView(model, "post.html");
        }, new VelocityTemplateEngine());

        get("/test", (request, response) -> {
            ArrayList<User> users = db.getUsers();
            ArrayList<Topic> topics = db.getTopics();
            ArrayList<Post> posts = db.getPosts();
            Map<String, Object> model = new HashMap<>();
            model.put("users", users);
            model.put("topics", topics);
            model.put("posts", posts);
            return new ModelAndView(model, "test.html");
        }, new VelocityTemplateEngine());
        post("/query", (request, response) -> {
            String query = request.queryParams("query");
            Map<String, Object> model = new HashMap<>();
            model.put("query", query);
            return new ModelAndView(model, "query.html");
        }, new VelocityTemplateEngine());

        get("/topics", (request, response) -> {
            ArrayList<Topic> topics = db.getTopics();
            Map<String, Object> model = new HashMap<>();
            model.put("topics", topics);
            return new ModelAndView(model, "topics.html");
        }, new VelocityTemplateEngine());

        get("/topics/:name", (request, response) -> {
            Topic topic = db.getTopic(request.params(":name"));
            Map<String, Object> model = new HashMap<>();
            model.put("topic", topic);
            return new ModelAndView(model, "topic.html");
        }, new VelocityTemplateEngine());

        get("/users", (request, response) -> {
            ArrayList<User> users = db.getUsers();
            Map<String, Object> model = new HashMap<>();
            model.put("users", users);
            return new ModelAndView(model, "userss.html");
        }, new VelocityTemplateEngine());

        get("/users/:name", (request, response) -> {
            User user = db.getUser(request.params(":name"));
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            return new ModelAndView(model, "user.html");
        }, new VelocityTemplateEngine());

        get("/newPost", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "newPost.html");
        }, new VelocityTemplateEngine());
    }
}
