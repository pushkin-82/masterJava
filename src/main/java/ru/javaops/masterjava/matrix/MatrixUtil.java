package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        executor.submit(
                () -> IntStream.range(0, matrixSize)
                        .parallel()
                        .forEach(i -> {
                            final int[] rowA = matrixA[i];
                            final int[] rowC = matrixC[i];

                            for (int idx = 0; idx < matrixSize; idx++) {
                                final int elA = rowA[idx];
                                final int[] rowB = matrixB[idx];
                                for (int col = 0; col < matrixSize; col++) {
                                    rowC[col] += elA * rowB[col];
                                }
                            }
                        })).get();

//        executor.submit(() -> IntStream.range(0, matrixSize)
//                        .parallel()
//                        .forEach(j -> multiplyMatrices(matrixA, matrixB, matrixSize, matrixC, j))).get();
        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            multiplyMatrices(matrixA, matrixB, matrixSize, matrixC, j);
        }

        return matrixC;
    }

    private static void multiplyMatrices(int[][] matrixA, int[][] matrixB, int matrixSize, int[][] matrixC, int j) {
        final int[] columnNeeded = new int[matrixSize];
        for (int k = 0; k < matrixSize; k++) {
            columnNeeded[k] = matrixB[k][j];
        }

        for (int i = 0; i < matrixSize; i++) {
            int[] rowNeeded = matrixA[i];
            int sum = 0;

            for (int k = 0; k < matrixSize; k++) {
                sum += rowNeeded[k] * columnNeeded[k];
            }
            matrixC[i][j] = sum;
        }
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
