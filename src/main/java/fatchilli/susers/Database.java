package fatchilli.susers;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
    Connection createConnection() throws SQLException;
}
