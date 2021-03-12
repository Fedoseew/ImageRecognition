package Database;

import Configurations.ApplicationConfiguration;
import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;


public final class DatabaseUtils {

    private static final EmbeddedDatabase db;

    static {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        db = builder
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("db")
                .addScript("db/create.sql")
                .addScript("db/insert.sql")
                .build();
    }

    public synchronized static void startDBManager() {
        String[] dbCredits = ApplicationConfiguration.getDatabaseCredits();
        DatabaseManagerSwing.main(dbCredits);

    }

    public synchronized static Connection getConnection() throws SQLException {
        return db.getConnection();
    }

    public synchronized static ResultSet selectQuery(String sql) {

        ResultSet resultSet = null;

        try {

            resultSet = getConnection().createStatement().executeQuery(sql);

        } catch (SQLException exception) {

            Logger.getGlobal().warning(exception.getSQLState() + exception.getLocalizedMessage());
            exception.printStackTrace();

        }

        return resultSet;
    }

    public synchronized static void insertQuery(String sql) {

        try {

           getConnection().createStatement().execute(sql);

        } catch (SQLException exception) {

            Logger.getGlobal().warning(exception.getSQLState() + exception.getLocalizedMessage());
            exception.printStackTrace();

        }

    }
}
