package fatchilli.susers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSourceFactory;

public class DatabaseSetup implements AppLifecycleListener, Database {

    private final DataSource dataSource;

    public DatabaseSetup() {
        final Properties properties = new Properties();
        try (final InputStream is = this.getClass().getResourceAsStream("/db.properties")) {
            properties.load(is);
        } catch (IOException e) {
            throw new SystemException("Couldn't find file with db information?", e);
        }
        try {
            //normally datasource would be taken from JNDI or similar mechanism
            dataSource = JDBCDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new SystemException("DataSource couldn't be established", e);
        }

    }

    @Override
    public void onStart() {
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder stringBuilder = new StringBuilder();
            try (final BufferedReader br =
                    new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/init.sql"),
                            StandardCharsets.UTF_8))) {

                do {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    stringBuilder.append(line);
                } while (true);
            }
            final String sql = stringBuilder.toString();
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }

        } catch (SQLException | IOException e) {
            throw new SystemException("Couldn't create database", e);
        }
    }

    @Override
    public void onFinish() {
        //no op
    }

    @Override
    public Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
