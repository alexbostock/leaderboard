package uk.bostock.leaderboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreDao {
    private final Set<Score> store;

    public ScoreDao() {
        this.store = new HashSet<>();
    }

    public List<Score> getAll() {
        return new ArrayList<>(this.store);
    }

    public List<Score> getByDateRange(final Optional<Date> from, final Optional<Date> to) {
        return this.store.stream()
            .filter(score -> !from.isPresent() || from.get().before(score.getTimestamp()))
            .filter(score -> !to.isPresent() || to.get().after(score.getTimestamp()))
            .collect(Collectors.toList());
    }

    public List<Score> getByScoreRange(final Optional<Integer> from, final Optional<Integer> to) {
        return this.store.stream()
            .filter(score -> !from.isPresent() || from.get() <= score.getScore())
            .filter(score -> !to.isPresent() || to.get() > score.getScore())
            .collect(Collectors.toList());
    }

    public void save(final Score score) {
        this.store.add(score);
    }
}