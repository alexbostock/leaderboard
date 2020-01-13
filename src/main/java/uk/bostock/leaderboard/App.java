package uk.bostock.leaderboard;

import com.google.gson.Gson;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Handler handler = new Handler();

        get("/scores", (req, res) -> handler.getAllScores(), gson::toJson);
        post("/scores", (req, res) -> handler.save(req), gson::toJson);
    }
}