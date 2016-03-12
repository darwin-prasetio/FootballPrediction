import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Pavilion on 24/02/2016.
 */
public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world");
//        ReadFile reader = new ReadFile();
//        reader.setTrainDataSet("1011.csv");
          List<List<Double>> matrix = new ArrayList<>();

        for(int i=0;i<4;i++) {
            List<Double> row = new ArrayList<>();
            row.add(1.0); row.add(2.0); row.add(3.0); row.add(4.0);
            matrix.add(row);
        }

        for(int i=0;i<matrix.size();i++) {
            for(int j=0;j<matrix.size();j++) {
                System.out.print(matrix.get(i).get(j));
            }
            System.out.println("");
        }

        System.out.println("\n");
        Logistic logistic = new Logistic();
        logistic.setVariables(matrix);
        logistic.transposeMatrix(logistic.getVariables());

    }
}
