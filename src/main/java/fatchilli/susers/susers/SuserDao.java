package fatchilli.susers.susers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import fatchilli.susers.Database;
import fatchilli.susers.SystemException;

public class SuserDao {

    private final Database db;

    public SuserDao(Database db) {
        this.db = db;
    }

    public void add(Suser suser) throws SuserAlreadyExistsException {
        Objects.requireNonNull(suser, "suser arg has to be not null");
        try (final Connection connection = db.createConnection()) {
            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SUSERS VALUES (?, ?, ?)");
            statement.setLong(1, suser.getUserId());
            statement.setString(2, suser.getGuid());
            statement.setString(3, suser.getUsername());
            statement.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SuserAlreadyExistsException(suser.getUserId());
        } catch (SQLException e) {
            throw new SystemException("Error adding user", e);
        }
    }

    public void deleteSusers() {
        try (final Connection connection = db.createConnection()) {
            final PreparedStatement statement = connection.prepareStatement("DELETE FROM SUSERS");
            statement.execute();
        } catch (SQLException e) {
            throw new SystemException("Error removing susers", e);
        }
    }

    public List<Suser> getSusers() {
        try (final Connection connection = db.createConnection()) {
            final PreparedStatement statement = connection
                    .prepareStatement("SELECT USER_ID, USER_GUID, USER_NAME\n"
                            + "FROM SUSERS");

            try (final ResultSet resultSet = statement.executeQuery()) {

                final ArrayList<Suser> results = new ArrayList<>();
                while (resultSet.next()) {
                    final long userId = resultSet.getLong(1);
                    final String guid = resultSet.getString(2);
                    final String username = resultSet.getString(3);
                    results.add(Suser.of(userId, guid, username));
                }
                return Collections.unmodifiableList(results);
            }
        } catch (SQLException e) {
            throw new SystemException("Error retreieving users user", e);
        }

    }
}
