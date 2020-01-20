package uk.bostock.leaderboard;

import java.io.IOError;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import spark.Request;

public class Handler {
    private final ScoreDao scores;
    private final Gson gson;

    public Handler() {
        try {
            this.scores = new ScoreDao();
            this.gson = new Gson();
        } catch (SQLException e) {
            System.err.println("Failed to load database.");
            throw new IOError(e);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load sqlite database driver.");
            throw new IOError(e);
        }
    }

    public Response<List<Score>> getAllScores() {
        return new Response<>(this.scores.getAll());
    }

    public Response<List<Score>> getTopNScores(String n) {
        if (!n.matches("\\d+")) {
            return new Response<>(404);
        }

        return new Response<>(this.scores.getTopNScores(Integer.parseInt(n)));
    }

    public Response<Void> save(final Request req) {
        RequestArgs args = gson.fromJson(req.body(), RequestArgs.class);
        if (args == null || args.score < 0) {
            return new Response<>(400);
        }

        scores.save(new Score(args.nickname, args.score));

        return new Response<>(200);
    };
}

class RequestArgs {
    String nickname;
    int score;
}