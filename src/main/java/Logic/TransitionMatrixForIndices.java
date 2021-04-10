package Logic;

import java.util.ArrayList;
import java.util.List;

class TransitionMatrixForIndices {
    private final int Xi;
    private final int Xj;

    private final List<List<Integer>> transitionMatrix;

    public TransitionMatrixForIndices(int Xi, int Xj) {

        transitionMatrix = new ArrayList<>(2);

        transitionMatrix.add(0, new ArrayList<>(2));
        transitionMatrix.add(1, new ArrayList<>(2));

        transitionMatrix.get(0).add(0, 0);
        transitionMatrix.get(0).add(1, 0);
        transitionMatrix.get(1).add(0, 0);
        transitionMatrix.get(1).add(1, 0);

        this.Xi = Xi;
        this.Xj = Xj;
    }

    public List<List<Integer>> getTransitionMatrix() {
        return transitionMatrix;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Transition matrix for X").append(Xi).append("|X").append(Xj).append("\n");

        matrixToString(stringBuilder, transitionMatrix);

        return stringBuilder.toString();
    }

    static void matrixToString(StringBuilder stringBuilder, List<List<Integer>> transitionMatrix) {
        for (int rowIndex = 0; rowIndex < transitionMatrix.size(); rowIndex++) {

            for (int indexInRow = 0; indexInRow < transitionMatrix.get(rowIndex).size(); indexInRow++) {

                int element = transitionMatrix
                        .get(rowIndex)
                        .get(indexInRow);

                stringBuilder
                        .append("(")
                        .append(rowIndex)
                        .append("->")
                        .append(indexInRow)
                        .append(": ")
                        .append(element)
                        .append(") ");
            }

            stringBuilder.append("\n");
        }
    }
}