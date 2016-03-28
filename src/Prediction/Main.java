package Prediction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavilion on 24/02/2016.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        ReadFile reader = new ReadFile();
        reader.setTrainDataSet("training.csv");

        Logistic logistic = new Logistic();
        logistic.estimateCoefficients(reader.getInstances());

        List<List<Double>> coefficients = logistic.getCoefficients();

        reader.setTrainDataSet("testing.csv");
        logistic.parseInstances(reader.getInstances());
        System.out.println(logistic.testAccuracy());
//          List<List<Double>> matrix = new ArrayList<>();
//        List<List<Double>> matrixB = new ArrayList<>();
//
//        for(int i=0;i<4;i++) {
//            List<Double> row = new ArrayList<>();
//            row.add(1.0); row.add(2.0); row.add(3.0); row.add(4.0);
//            matrix.add(row);
//        }
//
//        for(int i=0;i<4;i++) {
//            List<Double> row = new ArrayList<>();
//            row.add(1.0); row.add(2.0); row.add(3.0); row.add(4.0);
//            matrixB.add(row);
//        }
//
//        System.out.println("\n");
//        logistic.setVariables(matrix);
//        List<List<Double>> multiplicationResult = logistic.matrixMultiplication(matrix,matrixB);
//        List<List<Double>> additionResult = logistic.matrixAddition(matrix,matrixB);
//        List<List<Double>> substractionResult = logistic.matrixSubtraction(matrix,matrixB);
//        List<List<Double>> transposedMatrix = logistic.transposeMatrix(matrix);
//        List<List<Double>> inverse = logistic.inverse(matrix);
//        System.out.println("hi");




    }
}
