package uk.bostock.leaderboard;

public class Response<T> {
    private final int status;
    private final T data;

    public Response(final T data) {
        this.data = data;
        this.status = data == null ? 500 : 200;
    }

    public Response(final int status) {
        this.data = null;
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public T getData() {
        return this.data;
    }
}