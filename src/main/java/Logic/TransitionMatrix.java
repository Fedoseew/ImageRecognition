package Logic;

import Database.DB_TABLES;

import java.util.ArrayList;
import java.util.List;

public class TransitionMatrix {

    private final List<List<Integer>> transitionMatrix;

    private final DB_TABLES table;

    private final int columnIndex;

    public TransitionMatrix(DB_TABLES table, int columnIndex) {
        this.table = table;
        this.columnIndex = columnIndex;
        transitionMatrix = new ArrayList<>();
    }

    public List<List<Integer>> getTransitionMatrix() {

        return transitionMatrix;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nTransition matrix for table [").append(table)
                .append("] for column [").append(columnIndex).append("]:\n");

        for(int rowIndex = 0; rowIndex < transitionMatrix.size(); rowIndex++) {

            for(int indexInRow = 0; indexInRow < transitionMatrix.get(rowIndex).size(); indexInRow++) {

                int element = transitionMatrix.get(rowIndex).get(indexInRow);

                stringBuilder.append("(").append(rowIndex).append("->")
                        .append(indexInRow).append(": ").append(element).append(") ");
            }

            stringBuilder.append("\n");

        }

        return stringBuilder.toString();
    }

}
