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
            return new ModelAndView(model, "index.html");
        }, new VelocityTemplateEngine());
        get("/users", (request, response) -> {
            ArrayList<Database.User> users = db.get_users();

            Map<String, Object> model = new HashMap<>();
            model.put("users", users);
            return new ModelAndView(model, "users.html");
        }, new VelocityTemplateEngine());
    }
}
