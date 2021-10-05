package ru.cinema.store.psql;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class PsqlConnectionManager {

    private PsqlConnectionManager() {
    }

    private static final class Lazy {
        private static PsqlConnectionManager INST = new PsqlConnectionManager();
    }

    public static PsqlConnectionManager instOf() {
        return Lazy.INST;
    }

    public BasicDataSource getConnection() {
        BasicDataSource pool = new BasicDataSource();
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("cinemaDb.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
        return pool;
    }
}