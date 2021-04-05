package Logic;

import Database.DB_TABLES;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public double getI0_Y() {

        AtomicInteger sumOfAllElements = new AtomicInteger();
        transitionMatrix.forEach(row -> {
            row.forEach(sumOfAllElements::addAndGet);
        });

        return
                (double) CombinatoricsUtils.factorial(sumOfAllElements.get()) /
                        (CombinatoricsUtils.factorial(
                                (transitionMatrix.get(0).get(0) + transitionMatrix.get(1).get(0))) *
                                CombinatoricsUtils.factorial(
                                        (transitionMatrix.get(0).get(1) + transitionMatrix.get(1).get(1))));
    }

    public double getI0_YX() {
        return ((double) CombinatoricsUtils.factorial(
                transitionMatrix.get(0).get(0) + transitionMatrix.get(0).get(1)) /
                (
                        CombinatoricsUtils.factorial(transitionMatrix.get(0).get(0)) *
                                CombinatoricsUtils.factorial(transitionMatrix.get(0).get(1))
                )) *
                ((double) CombinatoricsUtils.factorial(
                        (transitionMatrix.get(1).get(0) + transitionMatrix.get(1).get(1))) /
                        (
                                CombinatoricsUtils.factorial(transitionMatrix.get(1).get(0)) *
                                        CombinatoricsUtils.factorial(transitionMatrix.get(1).get(1))
                        ));
    }

    private double calculateInformative() {

        //HINT: log2(x) = log10(x) / log10(2)
        double informative0_Y = getI0_Y();
        double informative0_YX = getI0_YX();

        double informative = Math.log10(informative0_Y / informative0_YX) / Math.log10(2);

        BigDecimal bd = new BigDecimal(Double.toString(informative));
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public double getInformative() {
        return calculateInformative();
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nTransition matrix for table [").append(table)
                .append("] for column [").append(columnIndex).append("]:\n");

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

        stringBuilder.append("Informative: ").append(calculateInformative());

        return stringBuilder.toString();
    }
}
