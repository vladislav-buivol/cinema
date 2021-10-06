package ru.cinema.models;

public class Ticket {
    private int id;
    private int sessionId;
    private Integer accountId;
    private int row;
    private int cell;
    private int hallId;

    public Ticket(int row, int cell, int hallId) {
        this.row = row;
        this.cell = cell;
        this.hallId = hallId;
    }

    public Ticket(int id, int sessionId, Integer accountId, int row, int cell, int hallId) {
        this.id = id;
        this.sessionId = sessionId;
        this.accountId = accountId;
        this.row = row;
        this.cell = cell;
        this.hallId = hallId;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }

    public int getHallId() {
        return hallId;
    }

    public int getId() {
        return id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }
}
