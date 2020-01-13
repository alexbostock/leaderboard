package uk.bostock.leaderboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ScoreDao {
    private final Map<Date, Score> store;

    public ScoreDao() {
        this.store = new TreeMap<>();
    }

    public List<Score> getAll() {
        return new ArrayList<>(this.store.values());
    }

    public List<Score> getByDateRange(final Optional<Date> from, final Optional<Date> to) {
        return this.store.values().stream()
            .filter(score -> !from.isPresent() || from.get().before(score.getTimestamp()))
            .filter(score -> !to.isPresent() || to.get().after(score.getTimestamp()))
            .collect(Collectors.toList());
    }

    public List<Score> getByScoreRange(final Optional<Integer> from, final Optional<Integer> to) {
        return this.store.values().stream()
            .filter(score -> !from.isPresent() || from.get() <= score.getScore())
            .filter(score -> !to.isPresent() || to.get() > score.getScore())
            .collect(Collectors.toList());
    }

    public void save(final Score score) {
        this.store.put(score.getTimestamp(), score);
    }
}