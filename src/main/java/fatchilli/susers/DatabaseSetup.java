package fatchilli.susers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSourceFactory;

public class DatabaseSetup implements AppLifecycleListener, Database {

    private final DataSource dataSource;

    public DatabaseSetup() {
        final Properties properties = new Properties();
        try (final InputStream is = this.getClass().getResource("/db.properties").openStream()) {
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
            final String sql = Files.lines(Paths.get(this.getClass().getResource("/init.sql").toURI()))
                    .collect(Collectors.joining());
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }

        } catch (SQLException | URISyntaxException | IOException e) {
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
