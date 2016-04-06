package Prediction;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavilion on 24/02/2016.
 */
public class Main {

    private JButton Test;
    private JPanel MainPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        ReadFile reader = new ReadFile();
        reader.setTrainDataSet("training.csv");

        Logistic logistic = new Logistic();
        logistic.estimateCoefficients(reader.getInstances());

        List<List<Double>> coefficients = logistic.getCoefficients();

        reader.setTrainDataSet("testing.csv");
        logistic.parseInstances(reader.getInstances());
        System.out.println("Accuracy: "+ logistic.testAccuracy());
        System.out.println("Coefficients");
        for(int i=0;i<logistic.getCoefficients().size();i++) {
            System.out.println(logistic.getCoefficients().get(i));
        }
    }
}
