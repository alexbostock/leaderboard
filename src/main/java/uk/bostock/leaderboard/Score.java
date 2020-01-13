package uk.bostock.leaderboard;

import java.util.Date;

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
}