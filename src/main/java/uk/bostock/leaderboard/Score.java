package uk.bostock.leaderboard;

import java.util.Date;
import java.util.Objects;

public class Score {
    private final String nickname;
    private final int score;
    private final Date timestamp;

    public Score(final String nickname, final int score) {
        this.nickname = nickname;
        this.score = score;
        this.timestamp = new Date();
    }

    public String getNickname() {
        return this.nickname;
    }

    public int getScore() {
        return this.score;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Score)) {
            return false;
        }
        final Score score = (Score) o;

        return this.nickname.equals(score.nickname)
            && this.score == score.score
            && this.timestamp.equals(score.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nickname, this.score, this.timestamp);
    }
}