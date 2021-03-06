package Prediction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Pavilion on 27/02/2016.
 * Class to handle read csv file
 */
public class ReadFile {

    private final String FOLDER="dataset/";
    private String TrainDataSet;
    private Vector<String> instances;

    public ReadFile() {
        instances = new Vector<String>();
    }

    public void setTrainDataSet(String dataSetName) {
        TrainDataSet=FOLDER+dataSetName;
        parseFile();
    }

    private void parseFile() {
        instances = new Vector<>();
        BufferedReader reader = null;
        String line="";
        try {
            reader = new BufferedReader(new FileReader(TrainDataSet));
            while((line = reader.readLine())!=null) {
                instances.add(line);
            }
            instances.remove(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector<String> getInstances() {
        return instances;
    }
}
