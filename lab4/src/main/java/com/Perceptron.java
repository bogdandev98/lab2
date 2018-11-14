package com;

import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;

/**
 * Клас для отримання перцептрону
 *
 * @author Ovsienko
 */
public class Perceptron {
    private ArrayList<Double> w;

    /**
     * Конструктор
     *
     * @param length довжина перцептрону
     */
    public Perceptron(int length) {
        w = new ArrayList<Double>();
        for (int i = 0; i < length; i++) {
            w.add(0.0);
        }
    }

    /**
     * Метод для розрахунку відповіді перцептрону
     *
     * @param matrix набір прикладів
     * @return повертає перцептрон
     */
    public RealVector vectorizedForwardPass(RealMatrix matrix) {
        int n = matrix.getRowDimension();
        int m = matrix.getColumnDimension();
        RealVector result = MatrixUtils.createRealMatrix(n, 1).getColumnVector(0);
        for (int i = 0; i < n; i++) {
            double S = 0;
            for (int j = 0; j < m - 1; j++) {
                S = S + matrix.getEntry(i, j) * w.get(j);
            }
            if (S > 0)
                result.setEntry(i,  1.0);
            else
                result.setEntry(i,  0.0);
        }
        return result;
    }

    /**
     * Метод для оновлення вагових коефіцієнтів перцептрону
     *
     * @param vector вектор активації входів перцептрону
     * @param ytrue  правильна відповідь
     * @return повертає оновлені вагові коефіцієнти
     */
    public double trainOnSingleExample(RealVector vector, int ytrue, double nu) {
        int m = vector.getDimension();

        double S = 0;
        for (int j = 0; j < m - 1; j++) {
            S = S + vector.getEntry( j) * w.get(j);
        }
        double e = vector.getEntry( m - 1) - ytrue;
        ArrayList<Double> oldW = new ArrayList<Double>();
        for (int i = 0; i < m - 1; i++)
            oldW.add(w.get(i));
        for (int i = 0; i < m - 1; i++) {
            if (vector.getEntry( m - 1) == 1 && ytrue == 0) {
                w.set(i, oldW.get(i) + nu * vector.getEntry( i));
            }
            if (vector.getEntry( m - 1) == 0 && ytrue == 1)
                w.set(i, oldW.get(i) - nu * vector.getEntry( i));
        }
        return e;
    }

    /**
     * Метод для послідовного тренування перцептрону
     *
     * @param matrix  матриця
     * @param ytrue   вектор
     * @param maxStep максимальна кількість кроків
     */
    public void trainUntilConvergence(RealMatrix matrix, RealVector ytrue, int maxStep) {
        double E = 0.5;
        double e;
        int n = matrix.getRowDimension();
        int m = matrix.getColumnDimension();
        double nu = 0.01;

        RealMatrix minmax = MatrixUtils.createRealMatrix(2, m - 1);
        for (int i = 0; i < m - 1; i++) {
            double maxs = 0.0;
            for (double localMax : matrix.getColumn(i)) {
                if (localMax > maxs)
                    maxs = localMax;
            }
            minmax.setEntry(0, i, maxs);
        }

        for (int i = 0; i < m - 1; i++) {
        double mins = Double.MAX_VALUE;
        for (double localMin : matrix.getColumn(i)) {
            if (localMin < mins)
                mins = localMin;
        }
        minmax.setEntry(1, i, mins);
    }
    RealMatrix mid = MatrixUtils.createRealMatrix(1, m - 1);
        for (int i = 0; i < m - 1; i++)
            mid.setEntry(0, i, (minmax.getEntry(0, i) + minmax.getEntry(1, i)) / 2);
        for (int i = 0; i < n; i++) {
        for (int j = 0; j < m - 1; j++) {
            if (matrix.getEntry(i, j) > mid.getEntry(0, j)) {
                matrix.setEntry(i, j, 1.0);
            } else {
                matrix.setEntry(i, j, -1.0);
            }
        }
    }
    int j = 0;
        while (E > 0 && j < maxStep) {
        //RealVector y = vectorizedForwardPass(matrix);
        E = 0;
        for (int i = 0; i < n; i++) {
            e = trainOnSingleExample(matrix.getRowVector(i), (int) ytrue.getEntry(i), nu);
            if (e != 0)
                E = E + 1;
            nu = nu - nu / 10;
        }
        j++;
    }
}

    /**
     * Метод для виведення результатів роботи натренованого перцептрону
     *
     * @param vector_p вектор перцептрону
     * @param vector_y вектор правильних відповідей
     */
    public String result(RealVector vector_p, RealVector vector_y) {
        double TP = 0.0;
        double TN = 0.0;
        double FP = 0.0;
        double FN = 0.0;
        for (int i = 0; i < vector_p.getDimension(); i++) {
            if (vector_p.getEntry(i) == 1.0 && vector_y.getEntry(i) == 1.0) {
                TP++;
            }
            if (vector_p.getEntry(i) == 0.0 && vector_y.getEntry(i) == 0.0) {
                TN++;
            }
            if (vector_p.getEntry(i) == 1.0 && vector_y.getEntry(i) == 0.0) {
                FP++;
            }
            if (vector_p.getEntry(i) == 0.0 && vector_y.getEntry(i) == 1.0) {
                FN++;
            }
        }
        String result = new String("");
        result+="True ( positive - " + (int) TP + "  negative - " + (int) TN + ")\n False (positive - " + (int) FP+" negative - " + (int) FN+")\n";
        double accuracy = ((TP + TN) / (TP + FP + FN + TN)) * 100;
        result+="Accuracy = " + (int)accuracy + "%";

        return result;
    }
}