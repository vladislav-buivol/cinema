package ru.cinema.models;

public class Session {
    int id;
    int movieId;

    public Session(int id, int movieId) {
        this.id = id;
        this.movieId = movieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
