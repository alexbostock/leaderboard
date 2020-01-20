package uk.bostock.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class ScoreDaoTest {
    @Test
    public void storesAndReturnsScores() throws ClassNotFoundException, SQLException {
        final ScoreDao scores = new ScoreDao("test1.db");
        scores.deleteAllRows();

        assertEquals(scores. getAll().size(), 0);

        scores.save(new Score("Alex", 100));

        assertEquals(scores.getAll().size(), 1);
        Score s = scores.getAll().get(0);
        assertEquals(s.getNickname(), "Alex");
        assertEquals(s.getScore(), 100);

        scores.save(new Score("Not Alex", 50));
        scores.save(new Score("Definitely Not Alex", 40));

        assertEquals(scores.getAll().size(), 3);

        assertEquals(scores.getByScoreRange(50, -1).size(), 2);
        assertEquals(scores.getByScoreRange(51, -1).size(), 1);
        assertEquals(scores.getByScoreRange(50, 100).size(), 1);
        assertEquals(scores.getByScoreRange(50, 101), scores.getByScoreRange(45, -1));

        assertEquals(scores.getAll(), scores.getByScoreRange(10, 101));
    }

    @Test
    public void ReturnsTopNScores() throws ClassNotFoundException, SQLException {
        final ScoreDao scores = new ScoreDao("test2.db");
        scores.deleteAllRows();

        for (int i = 0; i < 100; i++) {
            scores.save(new Score(Integer.toString(i), i));
        }

        assertEquals(scores.getTopNScores(13).size(), 13);
        assertEquals(scores.getTopNScores(4).get(3).getScore(), 96);
        assertEquals(scores.getTopNScores(1).get(0).getScore(), 99);
        assertEquals(scores.getTopNScores(2).get(0).getScore(), 99);
        assertEquals(scores.getTopNScores(2).get(1).getScore(), 98);
    }
}