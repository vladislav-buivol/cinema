package ru.cinema.store.psql.exeptions;

public class NoDataInDataBase extends RuntimeException {
    public NoDataInDataBase() {
        super();
    }

    public NoDataInDataBase(String errorMessage) {
        super(errorMessage);
    }
}
