package Logic;

import Configurations.ApplicationConfiguration;
import Database.DB_TABLES;
import Database.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ImageRecognition {

    private final String[] queries = generateSQL();

    private String[] generateSQL() {

        String[] result = new String[10];
        result[0] = "select * from " + DB_TABLES.source_zero;
        result[1] = "select * from " + DB_TABLES.source_one;
        result[2] = "select * from " + DB_TABLES.source_two;
        result[3] = "select * from " + DB_TABLES.source_three;
        result[4] = "select * from " + DB_TABLES.source_four;
        result[5] = "select * from " + DB_TABLES.source_five;
        result[6] = "select * from " + DB_TABLES.source_six;
        result[7] = "select * from " + DB_TABLES.source_seven;
        result[8] = "select * from " + DB_TABLES.source_eight;
        result[9] = "select * from " + DB_TABLES.source_nine;

        return result;
    }

    public Map<DB_TABLES, Integer> recognition(String source) throws SQLException {

        Connection connection = DatabaseUtils.getConnection();

        BinaryCodeComparator binaryCodeComparator = new BinaryCodeComparator();

        Map<DB_TABLES, Integer> result = new HashMap<>();

        if (!connection.isClosed()) {

            for (int countOfQuery = 0; countOfQuery < queries.length; countOfQuery++) {

                ResultSet rs = DatabaseUtils.selectQuery(queries[countOfQuery]);

                while (rs.next()) {

                    int conditionResult = binaryCodeComparator.compare(rs.getString(1), source);

                    if (conditionResult > 0) {

                        if (rs.getString(2).equals("FALSE")) {

                            break;

                        } else if (rs.getString(2).equals("TRUE")) {

                            DB_TABLES db_table = DB_TABLES.valueOf(queries[countOfQuery].substring(14));

                            result.put(db_table, countOfQuery);

                            return result;

                        }
                    }
                }
            }
            smartRecognition();
        }

        return result;

    }

    private List<List<List<Integer>>> smartRecognition() throws SQLException {

        ResultSet resultSet;

        //Все матрицы переходов:
        List<List<List<Integer>>> matrices = new ArrayList<>();

        // Информативности признаков:
        List<Double> informative = new ArrayList<>();


        for (String query : queries) {

            for (int index = 0; index < Math.pow(ApplicationConfiguration.getSizeOfGrid(), 2); index++) {

                resultSet = DatabaseUtils.selectQuery(query);

                // Матрица перехода:
                List<List<Integer>> transitionMatrix = new ArrayList<>();

                // Массив количества переходов элементов (0->0, 0->1, 1->0, 1->1)
                List<Integer> countOfTransitionElement = Arrays.asList(0, 0, 0, 0);

                while (resultSet.next()) {

                    if (resultSet.getString(1).charAt(index) == '0') {

                        if (resultSet.getString(2).equals("FALSE")) {

                            int tmp = countOfTransitionElement.get(0) + 1;
                            countOfTransitionElement.set(0, tmp);

                        } else if (resultSet.getString(2).equals("TRUE")) {

                            int tmp = countOfTransitionElement.get(1) + 1;
                            countOfTransitionElement.set(1, tmp);

                        }

                    } else if (resultSet.getString(1).charAt(index) == '1') {

                        if (resultSet.getString(2).equals("FALSE")) {

                            int tmp = countOfTransitionElement.get(2) + 1;
                            countOfTransitionElement.set(2, tmp);

                        } else if (resultSet.getString(2).equals("TRUE")) {

                            int tmp = countOfTransitionElement.get(3) + 1;
                            countOfTransitionElement.set(3, tmp);

                        }
                    }
                }

                transitionMatrix.add(Arrays.asList(
                        countOfTransitionElement.get(0),
                        countOfTransitionElement.get(1)
                ));

                transitionMatrix.add(Arrays.asList(
                        countOfTransitionElement.get(2),
                        countOfTransitionElement.get(3)
                ));

                matrices.add(transitionMatrix);
            }
        }
        return matrices;
    }
}
