package Config;

import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.SQLException;


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

    public static void startDBManager() {
        DatabaseManagerSwing.main(
                new String[]
                        {
                                "--url", "jdbc:hsqldb:mem:db",
                                "--user", "sa", "--password", ""
                        });

    }

    public static Connection getConnection() throws SQLException {
        return db.getConnection();
    }
}
