package ru.cinema.store.psql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinema.models.Ticket;
import ru.cinema.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PsqlTicketStore implements Store<Ticket> {
    private static final Logger LOGGER = LogManager.getLogger(PsqlTicketStore.class);
    private final BasicDataSource pool;

    public PsqlTicketStore() {
        pool = PsqlConnectionManager.instOf().getConnection();
    }

    private static final class Lazy {
        private static final PsqlTicketStore INST = new PsqlTicketStore();
    }

    public static PsqlTicketStore instOf() {
        return PsqlTicketStore.Lazy.INST;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(createTicketFromResult(it));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public Ticket findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket WHERE id =(?)")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createTicketFromResult(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Ticket save(Ticket ticket) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO ticket(session_id, row, cell, account_id, hall_id) "
                             + "VALUES ((?),(?),(?),(?),(?))",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getSessionId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getAccountId());
            ps.setInt(5, ticket.getHallId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        LOGGER.info("Ticket purchased");
        return ticket;
    }

    @Override
    public Ticket delete(int id) {
        Ticket ticket = findById(id);
        if (ticket != null) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps = cn.prepareStatement("DELETE FROM ticket WHERE id=(?)")) {
                ps.setInt(1, id);
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ticket;
        }
        return null;
    }

    public List<Ticket> findByHallId(int hallId) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM ticket WHERE hall_id =(?)")) {
            ps.setInt(1, hallId);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(createTicketFromResult(it));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return tickets;
    }

    private Ticket createTicketFromResult(ResultSet it) throws SQLException {
        return new Ticket(it.getInt("id"), it.getInt("session_id"),
                it.getInt("session_id"),
                it.getInt("row"), it.getInt("cell"),
                it.getInt("hall_id"));
    }
}
