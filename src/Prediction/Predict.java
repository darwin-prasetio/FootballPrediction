package Prediction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Created by Pavilion on 06/04/2016.
 */
public class Predict extends JFrame{


    private JButton Predict;
    private JPanel MainPanel;
    private JComboBox HomeTeamSelect;
    private JComboBox AwayTeamSelect;
    private JLabel predictResult;

    public Predict() {
        Predict = new JButton();
        Predict.setText("Predict");
        predictResult = new JLabel();
        HomeTeamSelect = new JComboBox();
        AwayTeamSelect = new JComboBox();
        Predict.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                train();

            }
        });
    }

    private void train() {
        predictResult.setText("Result: Home Win");

    }

    public static void main(String[] args) {
        ReadFile reader = new ReadFile();
//        reader.setTrainDataSet("training.csv");

        Logistic logistic = new Logistic();
//        logistic.estimateCoefficients(reader.getInstances());

        reader.setTrainDataSet("testing.csv");
        logistic.parseInstances(reader.getInstances());
//        System.out.println("Accuracy: " + logistic.testAccuracy());
//        System.out.println("Coefficients");
//        for (int i = 0; i < logistic.getCoefficients().size(); i++) {
//            System.out.println(logistic.getCoefficients().get(i));
//        }
//
//        for(Team e: logistic.getTeams()) {
//            System.out.println(e.getName());
//        }
        Predict prediction = new Predict();
        prediction.MainPanel.setLayout(new GridLayout(3,6));
        JFrame frame = new JFrame("Football Predictor");
        frame.setContentPane(prediction.MainPanel);

        prediction.MainPanel.add(new JPanel());
        prediction.MainPanel.add(new JLabel("Football Predictor"));
        prediction.MainPanel.add(new JPanel());
        prediction.MainPanel.add(new JLabel("Home Team:"));
        prediction.MainPanel.add(new JLabel("Away Team:"));
        prediction.MainPanel.add(prediction.Predict);
        prediction.HomeTeamSelect = new JComboBox(logistic.getTeams());
        prediction.AwayTeamSelect = new JComboBox(logistic.getTeams());
        prediction.MainPanel.add(prediction.HomeTeamSelect);
        prediction.MainPanel.add(prediction.AwayTeamSelect);
        prediction.MainPanel.add(prediction.predictResult);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
