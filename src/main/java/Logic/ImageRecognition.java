package Logic;

import Database.DB_TABLES;
import Database.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

    public Map<DB_TABLES, Integer> recognition(String source) throws SQLException {

        Connection connection = DatabaseUtils.getConnection();

        BinaryCodeComparator binaryCodeComparator = new BinaryCodeComparator();

        String[] queries = generateSQL();

        Map<DB_TABLES, Integer> result = new HashMap<>();

        if (!connection.isClosed()) {

            for (int i = 0; i < queries.length; i++) {

                ResultSet rs = connection.createStatement().executeQuery(queries[i]);

                while (rs.next()) {

                    if (binaryCodeComparator.compare(rs.getString(1), source) > 0) {

                        if(rs.getString(2).equals("TRUE")) {

                            DB_TABLES db_table = DB_TABLES.valueOf(queries[i].substring(14));

                            result.put(db_table, i+1);

                            return result;

                        } else {

                            break;

                        }
                    }
                }
            }
        }

        return result;
    }
}
