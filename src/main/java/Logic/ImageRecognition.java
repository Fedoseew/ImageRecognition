package Logic;

import Configurations.ApplicationConfiguration;
import Database.DB_TABLES;
import Database.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ImageRecognition {

    private final String[] queries = DatabaseUtils.selectAllSourcesQueries();

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

    private void smartRecognition() throws SQLException {

        ResultSet resultSet;

        /* Все матрицы переходов: */
        List<List<List<Integer>>> allTransitionMatrices = new ArrayList<>();

        /* Информативности признаков (key - columnIndex, value - informative): */
        Map<Integer, Double> informative = new HashMap();

        /* Цикл по таблицам: */
        for (String query : queries) {

            resultSet = DatabaseUtils.selectQuery(query);

            /* Для уменьшения нагрузки кешируем все строки из столбоцов source и isTrue в массивы соответственно: */
            List<String> sourceColumnData = new ArrayList<>();
            List<String> isTrueColumnData = new ArrayList<>();

            while (resultSet.next()) {

                sourceColumnData.add(resultSet.getString(1));
                isTrueColumnData.add(resultSet.getString(2));

            }

            /* Цикл по колонкам таблицы: */
            for (int columnIndex = 0; columnIndex < Math.pow(
                    ApplicationConfiguration.getSizeOfGrid(), 2); columnIndex++) {

                /* Матрица перехода для конкретной таблицы и колонки: */
                TransitionMatrix transitionMatrix = new TransitionMatrix(
                        DB_TABLES.valueOf(query.substring(14)), columnIndex + 1);

                /*
                 Массив количества переходов элементов, где индексы идут в следующем соотвествии:
                  0->0, 0->1, 1->0, 1->1 :
                */
                List<Integer> countOfTransitionElement = Arrays.asList(0, 0, 0, 0);

                /* Цикл по строкам: */
                for (
                        int sourceRowIndex = 0, isTrueRowIndex = 0;
                        sourceRowIndex < sourceColumnData.size() && isTrueRowIndex < isTrueColumnData.size();
                        sourceRowIndex++, isTrueRowIndex++
                ) {

                    /* Проверки на соответствие элемента числу 0 или 1 и во что он переходит:: */
                    if (sourceColumnData.get(sourceRowIndex).charAt(columnIndex) == '0') {

                        if (isTrueColumnData.get(isTrueRowIndex).equals("FALSE")) {

                            int tmp = countOfTransitionElement.get(0) + 1;
                            countOfTransitionElement.set(0, tmp);

                        } else if (isTrueColumnData.get(isTrueRowIndex).equals("TRUE")) {

                            int tmp = countOfTransitionElement.get(1) + 1;
                            countOfTransitionElement.set(1, tmp);

                        }

                    } else if (sourceColumnData.get(sourceRowIndex).charAt(columnIndex) == '1') {

                        if (isTrueColumnData.get(isTrueRowIndex).equals("FALSE")) {

                            int tmp = countOfTransitionElement.get(2) + 1;
                            countOfTransitionElement.set(2, tmp);

                        } else if (isTrueColumnData.get(isTrueRowIndex).equals("TRUE")) {

                            int tmp = countOfTransitionElement.get(3) + 1;
                            countOfTransitionElement.set(3, tmp);

                        }
                    }

                }

                transitionMatrix.getTransitionMatrix().add(Arrays.asList(
                        countOfTransitionElement.get(0),
                        countOfTransitionElement.get(1)
                ));

                transitionMatrix.getTransitionMatrix().add(Arrays.asList(
                        countOfTransitionElement.get(2),
                        countOfTransitionElement.get(3)
                ));

                informative.put(columnIndex+1, transitionMatrix.calculateInformative());

                allTransitionMatrices.add(transitionMatrix.getTransitionMatrix());

                System.out.println(transitionMatrix);

            }
            System.out.println(informative);
        }
    }
}
