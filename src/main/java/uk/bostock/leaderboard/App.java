package uk.bostock.leaderboard;

import com.google.gson.Gson;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Handler handler = new Handler();

        before((req, res) -> {
            res.type("application/json");
            res.header("Access-Control-Allow-Origin", "*");
        });

        get("/scores", (req, res) -> handler.getAllScores(), gson::toJson);
        get("/scores/top/:n", (req, res) -> handler.getTopNScores(req.params("n")), gson::toJson);
        post("/scores", (req, res) -> handler.save(req), gson::toJson);

        options("/scores", (req, res) -> {
            res.header("Access-Control-Allow-Methods", "GET, POST");
            res.header("Access-Control-Allow-Headers", "content-type");

            return "";
        });

        notFound("{\"status\": 404}");
        internalServerError("{\"status\": 500}");
    }
}