package uk.bostock.leaderboard;

import com.google.gson.Gson;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Handler handler = new Handler();

        get("/scores", (req, res) -> handler.getAllScores(), gson::toJson);
        get("/scores/top/:n", (req, res) -> handler.getTopNScores(req.params("n")), gson::toJson);
        post("/scores", (req, res) -> handler.save(req), gson::toJson);

        notFound("{\"status\": 404}");
        internalServerError("{\"status\": 500}");
    }
}