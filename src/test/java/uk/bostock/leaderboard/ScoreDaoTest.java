package uk.bostock.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ScoreDaoTest {
    @Test
    public void storesAndReturnsScores() {
        final ScoreDao scores = new ScoreDao();
        assertEquals(scores. getAll().size(), 0);

        scores.save(new Score("Alex", 100));

        assertEquals(scores.getAll().size(), 1);
        assertEquals(scores.getAll().get(0), new Score("Alex", 100));

        scores.save(new Score("Not Alex", 50));
        scores.save(new Score("Definitely Not Alex", 40));

        assertEquals(scores.getAll().size(), 3);

        assertEquals(scores.getByScoreRange(50, -1).size(), 2);
        assertEquals(scores.getByScoreRange(51, -1).size(), 1);
        assertEquals(scores.getByScoreRange(50, 100).size(), 1);
        assertEquals(scores.getByScoreRange(50, 101), scores.getByScoreRange(45, -1));

        assertEquals(scores.getAll(), scores.getByScoreRange(10, 101));
    }
}