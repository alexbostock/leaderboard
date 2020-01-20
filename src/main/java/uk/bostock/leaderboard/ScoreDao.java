package uk.bostock.leaderboard;

import java.io.IOError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ScoreDao {
    private final Set<Score> store;

    private final Connection db;

    public ScoreDao() throws ClassNotFoundException, SQLException {
        this.store = new HashSet<>();

        Class.forName("org.sqlite.JDBC");

        String url = "jdbc:sqlite:database.db";
        this.db = DriverManager.getConnection(url);

        Statement stmt = this.db.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS score ("
            + "nickname text NOT NULL,"
            + "score integer NOT NULL,"
            + "timestamp text NOT NULL"
        + ");");
    }

    public List<Score> getAll() {
        try (Statement stmt = this.db.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM score");

            List<Score> result = new ArrayList<>();

            while (rs.next()) {
                result.add(new Score(rs.getString("nickname"), rs.getInt("score")));
            }

            return result;
        } catch  (SQLException e) {
            throw new IOError(e);
        }
    }

    public List<Score> getByDateRange(final Date from, final Date to) {
        return this.store.stream()
            .filter(score -> from == null || from.before(score.getTimestamp()))
            .filter(score -> to == null || to.after(score.getTimestamp()))
            .collect(Collectors.toList());
    }

    public List<Score> getByScoreRange(final int from, final int to) {
        return this.store.stream()
            .filter(score -> from == -1 || from <= score.getScore())
            .filter(score -> to == -1 || to > score.getScore())
            .collect(Collectors.toList());
    }

    public List<Score> getTopNScores(final int n) {
        final TreeSet<Score> topN = new TreeSet<>((a, b) -> a.getScore() - b.getScore());

        for (Score score : this.store) {
            if (topN.size() < n) {
                topN.add(score);
            } else if (score.getScore() > topN.first().getScore()) {
                topN.add(score);
                topN.remove(topN.first());
            }
        }

        return new ArrayList<>(topN);
    }

    public void save(final Score score) {
        this.store.add(score);

        String sql = "INSERT INTO score(nickname, score, timestamp) VALUES(?, ?, datetime('now'));";

        try (PreparedStatement pstmt  = this.db.prepareStatement(sql)) {
            pstmt.setString(1, score.getNickname());
            pstmt.setInt(2, score.getScore());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }
}