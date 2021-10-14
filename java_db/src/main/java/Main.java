import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class Main {
    public static void main(final String[] args) {
        get("/hello", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("message", "Velocity World");

            // The vm files are located under the resources directory
            return new ModelAndView(model, "hello.vm");
        }, new VelocityTemplateEngine());
    }
}
