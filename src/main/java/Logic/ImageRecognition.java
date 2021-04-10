package Logic;

import Configurations.ApplicationConfiguration;
import Database.DB_TABLES;
import Database.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ImageRecognition {

    private final String[] queries = DatabaseUtils.selectAllFromDb();

    public Map<DB_TABLES, Integer> recognition(String source, int[] settings) throws SQLException {

        int alpha = settings[0];
        int betta = settings[1];
        int gamma = settings[2];

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
            smartRecognition(alpha, betta, gamma);
        }

        return result;
    }

    private void smartRecognition(int alpha, int betta, int gamma) throws SQLException {

        AtomicReference<ResultSet> resultSet = new AtomicReference<>();

        /* Все матрицы переходов: */
        List<List<TransitionMatrix>> allTransitionMatrices = new ArrayList<>();

        /* Информативности признаков (key - columnIndex, value - informative): */
        List<Map<Integer, Double>> informative = new ArrayList<>();

        /* Все признаки (key - номер таблицы (0..9), value - список признаков (key - номер признака, value - признак)): */
        Map<Integer, Map<Integer, String>> indices = new TreeMap<>();

        /* Цикл по таблицам: */
        for (int countOfQuery = 0; countOfQuery < queries.length; countOfQuery++) {
            Map<Integer, Double> tableInformative = new TreeMap<>();
            List<TransitionMatrix> tableTransitionMatrix = new ArrayList<>();
            indices.put(countOfQuery, new TreeMap<>());

            resultSet.set(DatabaseUtils.selectQuery(queries[countOfQuery]));

            /* Для уменьшения нагрузки кэшируем все строки из столбоцов source и isTrue в массивы соответственно: */
            List<String> sourceColumnData = new ArrayList<>();
            List<String> isTrueColumnData = new ArrayList<>();


            while (resultSet.get().next()) {
                sourceColumnData.add(resultSet.get().getString(1));
                isTrueColumnData.add(resultSet.get().getString(2));
            }

            /* Цикл по колонкам таблицы: */
            for (int columnIndex = 0; columnIndex < Math.pow(
                    ApplicationConfiguration.getSizeOfGrid(), 2); columnIndex++) {

                /* Матрица перехода для конкретной таблицы и колонки: */
                TransitionMatrix transitionMatrix = new TransitionMatrix(
                        DB_TABLES.valueOf(queries[countOfQuery].substring(14)), columnIndex + 1);

                /*
                 Массив количества переходов элементов, где индексы идут в следующем соотвествии:
                  0->0, 0->1, 1->0, 1->1 :
                */
                List<Integer> countOfTransitionElement = Arrays.asList(0, 0, 0, 0);

                StringBuilder indicesBuilder = new StringBuilder();

                /* Цикл по строкам: */
                for (
                        int sourceRowIndex = 0, isTrueRowIndex = 0;
                        sourceRowIndex < sourceColumnData.size() && isTrueRowIndex < isTrueColumnData.size();
                        sourceRowIndex++, isTrueRowIndex++
                ) {

                    /* Проверки на соответствие элемента числу 0 или 1 и во что он переходит: */
                    try {
                        indicesBuilder.append(sourceColumnData.get(sourceRowIndex).charAt(columnIndex));

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

                    } catch (Exception ignored) {
                    }
                }

                indices.get(countOfQuery).put(columnIndex + 1, indicesBuilder.toString());

                transitionMatrix
                        .getTransitionMatrix()
                        .add(Arrays.asList(countOfTransitionElement.get(0), countOfTransitionElement.get(1)
                        ));

                transitionMatrix
                        .getTransitionMatrix()
                        .add(Arrays.asList(countOfTransitionElement.get(2), countOfTransitionElement.get(3)
                        ));

                tableInformative.put(columnIndex + 1, transitionMatrix.getInformative());
                tableTransitionMatrix.add(transitionMatrix);
            }

            informative.add(tableInformative);
            allTransitionMatrices.add(tableTransitionMatrix);
        }

        // Откидывание признаков по параметру alpha:
        double alphaProcent;

        if (alpha > 0) {

            alphaProcent = allTransitionMatrices
                    .get(1)
                    .get(0)
                    .getI0_Y()
                    * ((double) alpha) / 100;

            Double finalAlphaProcent = alphaProcent;
            List.copyOf(informative)
                    .forEach(tableInformative -> {
                        Map.copyOf(tableInformative)
                                .forEach((column, informativeOfColumn) -> {
                                    if (informativeOfColumn < finalAlphaProcent) {
                                        tableInformative.remove(column);
                                    }
                                });
                    });
        }


        /* TODO: Расчёт расстояний между признаками:
         * P(Xi, Xj) = 1/2 * { I0(Xi|Xj) + I0(Xj|Xi) }
         */

        /* Расстояния (key - номер таблицы (0..9),
         * value - список сложных признаков (key - индекс сложного признака, value - расстояние)):
         */
        Map<Integer, List<Map<String, Double>>> metrics = new TreeMap<>();

        StringBuilder columnsBuilder = new StringBuilder();


        var ref = new Object() {
            int countOfNumber = 0;
        };

        informative.forEach(tableInformative -> {

            metrics.put(ref.countOfNumber, new ArrayList<>());

            tableInformative.forEach((column1, informativeOfColumn1) -> {

                tableInformative.forEach((column2, informativeOfColumn2) -> {

                    if (!column1.equals(column2)) {

                        // Проверка на дубликаты (HINT: признак 12 == 21):
                        AtomicBoolean duplicates = new AtomicBoolean(false);

                        metrics.forEach((key, value) -> value.forEach(x -> {
                            if (x.containsKey(String.valueOf(column1) + column2)
                                    || x.containsKey(String.valueOf(column2) + column1)) {
                                duplicates.set(x.containsKey(String.valueOf(column1) + column2)
                                        || x.containsKey(String.valueOf(column2) + column1));
                            }
                        }));

                        if (!duplicates.get()) {

                            // Расчёт по формуле, добавление в map с метриками и формирование сложных признаков:
                            columnsBuilder.append(column1).append(column2);

                            TransitionMatrixForIndices transitionMatrix = new TransitionMatrixForIndices(column1, column2);
                            String firstSource = indices.get(ref.countOfNumber).get(column1);
                            String secondSource = indices.get(ref.countOfNumber).get(column2);

                            for (int i = 0; i < firstSource.length(); i++) {

                                if (firstSource.charAt(i) == '0') {
                                    if (secondSource.charAt(i) == '0') {
                                        int old = transitionMatrix.getTransitionMatrix().get(0).get(0);
                                        transitionMatrix.getTransitionMatrix().get(0).set(0, old + 1);
                                    } else if (secondSource.charAt(i) == '1') {
                                        int old = transitionMatrix.getTransitionMatrix().get(0).get(1);
                                        transitionMatrix.getTransitionMatrix().get(0).set(1, old + 1);
                                    }

                                } else if (firstSource.charAt(i) == '1') {
                                    if (secondSource.charAt(i) == '0') {
                                        int old = transitionMatrix.getTransitionMatrix().get(1).get(0);
                                        transitionMatrix.getTransitionMatrix().get(1).set(0, old + 1);
                                    } else if (secondSource.charAt(i) == '1') {
                                        int old = transitionMatrix.getTransitionMatrix().get(1).get(1);
                                        transitionMatrix.getTransitionMatrix().get(1).set(1, old + 1);
                                    }
                                }
                            }

                            metrics
                                    .get(ref.countOfNumber)
                                    .add(Collections.singletonMap(columnsBuilder.toString(), 0.0));

                            columnsBuilder.delete(0, columnsBuilder.length());
                        }
                    }
                });
            });
            ref.countOfNumber++;
        });

        System.out.println(metrics);

        // TODO: Формирование сложных признаков:

        // TODO: Откидывание сложных признаков по параметру betta:

        // TODO: Переход в новое пространство признаков (параметр gamma):
    }
}
