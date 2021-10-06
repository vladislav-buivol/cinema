package ru.cinema.store.psql;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.cinema.models.Account;
import ru.cinema.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PsqlAccountStore implements Store<Account> {

    private final BasicDataSource pool;

    public PsqlAccountStore() {
        pool = PsqlConnectionManager.instOf().getConnection();
    }

    private static final class Lazy {
        private static final PsqlAccountStore INST = new PsqlAccountStore();
    }

    public static PsqlAccountStore instOf() {
        return Lazy.INST;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM account")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    accounts.add(createAccountFromResult(it));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM account WHERE id =(?)")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createAccountFromResult(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account save(Account account) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO account(username, email, phone) "
                             + "VALUES ((?),(?),(?))", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public Account delete(int id) {
        Account account = findById(id);
        if (account != null) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps = cn.prepareStatement("DELETE FROM account WHERE id=(?)")) {
                ps.setInt(1, id);
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return account;
        }
        return null;
    }

    private Account createAccountFromResult(ResultSet it) throws SQLException {
        return new Account(it.getInt("id"), it.getString("username"),
                it.getString("email"), it.getString("phone"));
    }
}

