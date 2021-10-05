package ru.cinema.models;

import java.util.List;

public class Hall {
    private int id;
    private int row;
    private int cell;
    private long refreshTime;
    private List<Ticket> unavailablePlaces;

    public Hall(int id, int row, int cell, long refreshTime) {
        this.id = id;
        this.row = row;
        this.cell = cell;
        this.refreshTime = refreshTime;
    }

    public Hall(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public int getId() {
        return id;
    }

    public List<Ticket> getUnavailablePlaces() {
        return unavailablePlaces;
    }

    public void setUnavailablePlaces(List<Ticket> unavailablePlaces) {
        this.unavailablePlaces = unavailablePlaces;
    }
}
