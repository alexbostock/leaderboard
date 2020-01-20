package uk.bostock.leaderboard;

import java.io.IOError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreDao {
    private final Connection db;
    private DateFormat dateFormat;

    public ScoreDao() throws ClassNotFoundException, SQLException {
        this("database.db");
    }

    public ScoreDao(String dbFile) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        String url = "jdbc:sqlite:" + dbFile;
        this.db = DriverManager.getConnection(url);

        Statement stmt = this.db.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS score ("
            + "nickname text NOT NULL,"
            + "score integer NOT NULL,"
            + "timestamp text NOT NULL"
        + ");");

        this.dateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
    }

    public List<Score> getAll() {
        try (Statement stmt = this.db.createStatement()) {
            return parseResult(stmt.executeQuery("SELECT * FROM score"));
        } catch  (SQLException e) {
            throw new IOError(e);
        }
    }

    public List<Score> getByScoreRange(final int from, final int to) {
        if (from == -1 && to == -1) {
            return getAll();
        } else if (from == -1) {
            return getByMaxScore(to);
        } else if (to == -1) {
            return getByMinScore(from);
        }

        String sql = "SELECT * FROM score WHERE ? <= score AND score < ?";

        try (PreparedStatement pstmt = this.db.prepareStatement(sql)) {
            pstmt.setInt(1, from);
            pstmt.setInt(2, to);
            return parseResult(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    private List<Score> getByMinScore(final int min) {
        String sql = "SELECT * FROM score WHERE ? <= score";

        try (PreparedStatement pstmt = this.db.prepareStatement(sql)) {
            pstmt.setInt(1, min);
            return parseResult(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    private List<Score> getByMaxScore(final int max) {
        String sql = "SELECT * FROM score WHERE ? <= score";

        try (PreparedStatement pstmt = this.db.prepareStatement(sql)) {
            pstmt.setInt(1, max);
            return parseResult(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    public List<Score> getTopNScores(final int n) {
        String sql = "SELECT * FROM score ORDER BY score DESC LIMIT ?";

        try (PreparedStatement pstmt = this.db.prepareStatement(sql)) {
            pstmt.setInt(1, n);
            return parseResult(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    public void save(final Score score) {
        String sql = "INSERT INTO score(nickname, score, timestamp) VALUES(?, ?, datetime('now'));";

        try (PreparedStatement pstmt  = this.db.prepareStatement(sql)) {
            pstmt.setString(1, score.getNickname());
            pstmt.setInt(2, score.getScore());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    public void deleteAllRows() {
        String sql = "DELETE from score";

        try (Statement stmt = this.db.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    private List<Score> parseResult(ResultSet rs) throws SQLException {
        List<Score> result = new ArrayList<>();

        while (rs.next()) {
            String nickname = rs.getString("nickname");
            int score = rs.getInt("score");
            Date timestamp = this.parseTimestamp(rs.getString("timestamp"));
            result.add(new Score(nickname, score, timestamp));
        }

        return result;
    }

    private Date parseTimestamp(String t) {
        try {
            return this.dateFormat.parse(t);
        } catch (ParseException e) {
            System.err.println("Corrupt database: failed to parse timestamp");
            throw new IOError(e);
        }
    }
}