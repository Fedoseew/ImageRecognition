package Logic;

import Config.DB_TABLES;
import Config.DatabaseUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ImageRecognition {

    private String[] generateSQL() {

        String[] result = new String[9];
        result[0] = "select * from " + DB_TABLES.source_one;
        result[1] = "select * from " + DB_TABLES.source_two;
        result[2] = "select * from " + DB_TABLES.source_three;
        result[3] = "select * from " + DB_TABLES.source_four;
        result[4] = "select * from " + DB_TABLES.source_five;
        result[5] = "select * from " + DB_TABLES.source_six;
        result[6] = "select * from " + DB_TABLES.source_seven;
        result[7] = "select * from " + DB_TABLES.source_eight;
        result[8] = "select * from " + DB_TABLES.source_nine;

        return result;
    }

    public int recognition(String source) throws SQLException {

        Connection connection = DatabaseUtils.getConnection();
        BinaryCodeComparator binaryCodeComparator = new BinaryCodeComparator();
        String[] queries = generateSQL();

        if (!connection.isClosed()) {

            System.out.println("INFO: Successful connection to the database.\n");

            for (int i = 0; i < queries.length; i++) {

                ResultSet rs = connection.createStatement().executeQuery(queries[i]);

                while (rs.next()) {

                    if (binaryCodeComparator.compare(rs.getString(1), source) > 0) {
                        System.out.println("\nSuccessfully! Your number is " + (i + 1) + ", right?");
                        return i;
                    }
                }
            }
        }

        System.out.println("The recognition process is completed without a result :( Try a different number.");

        return -1;
    }
}
