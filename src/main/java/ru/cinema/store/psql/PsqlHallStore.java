package ru.cinema.store.psql;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.cinema.models.Hall;
import ru.cinema.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PsqlHallStore implements Store<Hall> {

    private final BasicDataSource pool;

    private PsqlHallStore() {
        this.pool = PsqlConnectionManager.instOf().getConnection();
    }

    private static class Lazy {
        private static final PsqlHallStore INST = new PsqlHallStore();
    }

    public static PsqlHallStore instOf() {
        return Lazy.INST;
    }

    @Override
    public List<Hall> findAll() {
        List<Hall> halls = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM hall")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    halls.add(createHallFromResult(it));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return halls;
    }

    @Override
    public Hall findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM hall WHERE id=(?)")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createHallFromResult(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Hall save(Hall hall) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO hall(total_rows, total_cells) " + "VALUES ((?),(?))",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hall.getRow());
            ps.setInt(1, hall.getCell());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hall;
    }

    @Override
    public Hall delete(int id) {
        Hall hall = findById(id);
        if (hall != null) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps = cn.prepareStatement("DELETE FROM hall WHERE id=(?)")) {
                ps.setInt(1, id);
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return hall;
        }
        return null;
    }

    private Hall createHallFromResult(ResultSet it) throws SQLException {
        return new Hall(it.getInt("id"), it.getInt("total_rows"),
                it.getInt("total_cells"), it.getLong("refreshtimeout"));
    }
}
