package uk.bostock.leaderboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ScoreDao {
    private final Set<Score> store;

    public ScoreDao() {
        this.store = new HashSet<>();
    }

    public List<Score> getAll() {
        return new ArrayList<>(this.store);
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
    }
}