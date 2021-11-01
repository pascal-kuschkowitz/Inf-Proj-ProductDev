import static spark.Spark.get;
import static spark.Spark.post;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class Main {

    public static void main(final String[] args) throws ClassNotFoundException {
        Database db = new Database();

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("message", "Velocity World");
            return new ModelAndView(model, "home.html");
        }, new VelocityTemplateEngine());
        get("/users", (request, response) -> {
            ArrayList<User> users = db.getUsers();

            Map<String, Object> model = new HashMap<>();
            model.put("users", users);
            return new ModelAndView(model, "users.html");
        }, new VelocityTemplateEngine());
        get("/newPost", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "newPost.html");
        }, new VelocityTemplateEngine());
        get("/topics/:name", (request, response) -> {
            Topic topic = db.getTopic(request.params(":name"));
            Map<String, Object> model = new HashMap<>();
            model.put("topic", topic);
            return new ModelAndView(model, "topic.html");
        }, new VelocityTemplateEngine());
        get("/topics", (request, response) -> {
            ArrayList<Topic> topics = db.getTopics();
            Map<String, Object> model = new HashMap<>();
            model.put("topics", topics);
            return new ModelAndView(model, "topics.html");
        }, new VelocityTemplateEngine());
    }
}
