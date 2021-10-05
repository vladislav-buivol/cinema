package ru.cinema.store.psql;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.cinema.models.Session;
import ru.cinema.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PsqlSessionStore implements Store<Session> {

    private final BasicDataSource pool;

    public PsqlSessionStore() {
        pool = PsqlConnectionManager.instOf().getConnection();
    }

    private static final class Lazy {
        private static final PsqlSessionStore INST = new PsqlSessionStore();
    }

    public static PsqlSessionStore instOf() {
        return PsqlSessionStore.Lazy.INST;
    }

    @Override
    public List<Session> findAll() {
        List<Session> session = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM session")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    session.add(createSessionFromResult(it));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    @Override
    public Session findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM session WHERE id =(?)")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createSessionFromResult(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Session save(Session session) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Session delete(int id) {
        throw new UnsupportedOperationException();
    }

    private Session createSessionFromResult(ResultSet it) throws SQLException {
        return new Session(it.getInt("id"), it.getInt("movie_id"));
    }
}
