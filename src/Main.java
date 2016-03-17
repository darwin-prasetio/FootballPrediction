import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
//        Logistic logistic = new Logistic();
//        logistic.setVariables(matrix);
//        List<List<Double>> result = logistic.matrixMultiplication(matrix,matrixB);
//
//        for(int i=0;i<result.size();i++) {
//            for(int j=0;j<result.get(0).size();j++) {
//                System.out.print(result.get(i).get(j) + " ");
//            }
//            System.out.println("");
//        }


    }
}
